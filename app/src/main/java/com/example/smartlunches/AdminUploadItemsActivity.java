package com.example.smartlunches;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartlunches.Model.LoadingDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AdminUploadItemsActivity extends Fragment {

    private String timeslot, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductRating, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private CheckBox timemorning ,timenoon ,timeeveng , timenight , itemtypeveg ,itemtypenonveg;
    private int time1 =0 , time2 =0,time3 =0,time4 =0;
    private Spinner itemtype;
    private boolean selected = false;
    private long item_selected;
    private boolean itemveg ;
    private CheckBox checkBox;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_upload_items, container , false);

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Items Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Items");

        AddNewProductButton = view.findViewById(R.id.add_new_product);
        InputProductImage = view.findViewById(R.id.select_product_image);
        InputProductName =  view.findViewById(R.id.product_name);
        InputProductPrice =  view.findViewById(R.id.product_price);
        InputProductRating = view.findViewById(R.id.product_rating);
        itemtype = view.findViewById(R.id.item_type);
        timemorning = view.findViewById(R.id.time_morning);
        timeeveng = view.findViewById(R.id.time_evening);
        timenight = view.findViewById(R.id.time_night);
        timenoon = view.findViewById(R.id.time_noon);
        itemtypeveg = view.findViewById(R.id.item_veg);
        itemtypenonveg = view.findViewById(R.id.item_nonveg);

        List<String> item_list = new ArrayList<String>();
        item_list.add(0 , "Chose item type");
        item_list.add(1 , "Snacks");
        item_list.add(2 , "Dosa");
        item_list.add(3 , "Rice & Noodles");
        item_list.add(4 , "Meals");
        item_list.add(5 , "Starters");
        item_list.add(6 , "Breads");
        item_list.add(7 , "Main Course");
        item_list.add(8 , "Juices");
        item_list.add(9 , "Others");

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter(getActivity() , android.R.layout.simple_spinner_item , item_list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemtype.setAdapter(arrayAdapter);

        itemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Chose item type")){
                    selected = false;
                }else{
                    item_selected = parent.getItemIdAtPosition(position);
                    selected = true;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .getIntent(getContext());

                startActivityForResult(intent , CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);            }
        });
        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
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
                ImageUri = result.getUri();
                InputProductImage.setImageURI(ImageUri);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Log.e("error" , String.valueOf(error));
            }
        }else{
            Toast.makeText(getActivity(), "Error, Try Again.", Toast.LENGTH_SHORT).show();
        }
    }
    private void ValidateProductData()
    {
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        if(!selected){
            Toast.makeText(getActivity(), "Select a item type....", Toast.LENGTH_SHORT).show();
        }
        if (ImageUri == null)
        {
            Toast.makeText(getActivity(), "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(getActivity(), "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(getActivity(), "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
            if(TextUtils.isEmpty(InputProductRating.getText().toString())){
                Toast.makeText(getActivity(), "Please write product rating...", Toast.LENGTH_SHORT).show();
            }
        else
        if(!timemorning.isChecked() && !timenoon.isChecked() && !timenight.isChecked() && !timeeveng.isChecked()){
            Toast.makeText(getActivity(), "Please select the available timing", Toast.LENGTH_SHORT).show();
        }
        else
        if(!itemtypeveg.isChecked() && !itemtypenonveg.isChecked()){
            Toast.makeText(getActivity(), "Please select the item type", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(timemorning.isChecked()){
                time1 =1;
            }
            if(timenoon.isChecked()){
                time2 =1;
            }
            if(timeeveng.isChecked()){
                time3 =1;
            }
            if(timenight.isChecked()){
                time4 =1;
            }
            itemveg = itemtypeveg.isChecked();
            StoreProductInformation();
        }
    }


    private void StoreProductInformation()
    {
        //load
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getActivity(), "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(getActivity(), "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("type", item_selected);
        productMap.put("itemveg", itemveg);
        productMap.put("rating" , InputProductRating.getText().toString());

        HashMap<String, Object> timeslot = new HashMap<>();
        timeslot.put("Morning", time1);
        timeslot.put("Afternoon", time2);
        timeslot.put("Evening", time3);
        timeslot.put("Night", time4);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            ProductsRef.child(productRandomKey).child("Time").updateChildren(timeslot).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(getActivity(), AdminActivity.class);
                                        startActivity(intent);

                                        Toast.makeText(getActivity(), "Product is added successfully..", Toast.LENGTH_SHORT).show();

                                    }else{

                                        String message = task.getException().toString();
                                        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}