package com.example.smartlunches.ui.Settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartlunches.HomeActivity;
import com.example.smartlunches.LoginActivity;
import com.example.smartlunches.Model.Users;
import com.example.smartlunches.Prevelent.Prevelent;
import com.example.smartlunches.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, useremailid;
    private TextView userusn ,userbranch;
    private Button saveTextBtn;
    private Object DatabaseReference;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private static final int PICK_IMAGE = 100;
    private int requestCode;
    private int resultCode;

    //private Switch switchdark;
    //final LoadingDialog loadingBar = new LoadingDialog(getActivity());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Image");
        profileImageView = (CircleImageView) view.findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) view.findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) view.findViewById(R.id.settings_phone_number);
        useremailid = (EditText) view.findViewById(R.id.settings_emailid);
        userusn = view.findViewById(R.id.settings_usn);
        userbranch = view.findViewById(R.id.settings_branch);
        saveTextBtn = (Button) view.findViewById(R.id.update_account_settings_btn);
        if (!Prevelent.loggedin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, useremailid, userbranch, userusn);
        }
        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Toast.makeText(getActivity(), "Use a Image below 140kb", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Bigger file can lead to app crash", Toast.LENGTH_SHORT).show();
                checker = "clicked";
                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .getIntent(getContext());

                startActivityForResult(intent , CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imageUri = result.getUri();
                profileImageView.setImageURI(imageUri);
            }
            else
                if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Log.e("error" , String.valueOf(error));
                }

        }
        else
        {
            Toast.makeText(getActivity(), "Error, Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        userMap.put("email", useremailid.getText().toString());
        userMap.put("phone", userPhoneEditText.getText().toString());
        userMap.put("branch", userbranch.getText().toString());
        ref.child(Prevelent.currentonlineUser.getUsn()).updateChildren(userMap);

        startActivity(new Intent(getActivity(), HomeActivity.class));
        Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            Toast.makeText(getActivity(), "Name is mandatory.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(useremailid.getText().toString())) {
            Toast.makeText(getActivity(), "Email is mandatory.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
            Toast.makeText(getActivity(), "Phone is mandatory.", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
       // loadingBar.enableloading();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevelent.currentonlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", fullNameEditText.getText().toString());
                        userMap.put("email", useremailid.getText().toString());
                        userMap.put("phone", userPhoneEditText.getText().toString());
                        userMap.put("branch", userbranch.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevelent.currentonlineUser.getUsn()).updateChildren(userMap);

                        //loadingBar.disableloading();

                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        Toast.makeText(getActivity(), "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                       // loadingBar.disableloading();
                        Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText, final TextView userbranch, final TextView
            userusn) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(Prevelent.currentonlineUser.getUsn());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Users userData = dataSnapshot.getValue(Users.class);

                    //String name = dataSnapshot.child("name").getValue().toString();
                    //String phone = dataSnapshot.child("phone").getValue().toString();

                    fullNameEditText.setText(userData.getName());
                    //userPhoneEditText.setText(phone);
                    userusn.setText(Prevelent.currentonlineUser.getUsn());
                    userbranch.setText(userData.getBranch());
                    userPhoneEditText.setText(userData.getPhone());
                    //userbranch.setText();


                    if (dataSnapshot.child("email").exists()) {
                        String address = dataSnapshot.child("email").getValue().toString();

                        addressEditText.setText(address);

                        if (dataSnapshot.child("image").exists()) {
                            String image = dataSnapshot.child("image").getValue().toString();

                            Picasso.get().load(image).into(profileImageView);
                        }
                    } else if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Check your Internet connectivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}