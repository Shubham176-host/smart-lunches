package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smartlunches.Model.LoadingDialog;
import com.example.smartlunches.Model.Users;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressEditText;

public class SignupActivity extends AppCompatActivity {

    private Button signup;
    private EditText userusn , username, userpassword , usercpassword;
    final LoadingDialog loadingBar = new LoadingDialog(this);
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userusn = findViewById(R.id.usn);
        username = findViewById(R.id.UserName);
        userpassword = findViewById(R.id.password);
        usercpassword = findViewById(R.id.cpassword);
        signup = findViewById(R.id.signupbtn);

        signup.setOnClickListener(v ->
            CreateAccount()
        );
    }

        private void CreateAccount()
        {
            String usn =userusn.getText().toString();
            String name = username.getText().toString();
            String password =userpassword.getText().toString();
            String Cpassword =usercpassword.getText().toString();

            usn = usn.toUpperCase();

            if(TextUtils.isEmpty(usn))
            {
                Toast.makeText(SignupActivity.this, "Please Enter your usn....", Toast.LENGTH_SHORT).show();
            }
            else
            if(TextUtils.isEmpty(name))
            {
                Toast.makeText(SignupActivity.this, "Please Enter your phone number....", Toast.LENGTH_SHORT).show();
            }
            else
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(SignupActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
            }
            else
            if(!(Cpassword.equals(password)))
            {
                Toast.makeText(SignupActivity.this, "Confirm Password does not match", Toast.LENGTH_SHORT).show();
            }
            else
            if(TextUtils.isDigitsOnly(password))
            {
                Toast.makeText(SignupActivity.this, "Password must contain alphabets", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String branch;
                if(usn.charAt(5) == 'C') {
                    branch ="Computer Science Engineering";
                }
                else
                if(usn.charAt(5) == 'I') {
                    branch = "Information Science Engineering";
                }
                else
                if(usn.charAt(5) == 'E' && usn.charAt(6) == 'C') {
                    branch = "Electronics and Communication Engineering";
                }else
                if(usn.charAt(5) == 'E' && usn.charAt(6) == 'E') {
                    branch = "Electrical and Electronic Engineering";
                }
                else
                if(usn.charAt(5) == 'M') {
                    branch = "Mechanical Engineering";
                }
                else
                {
                    branch = "IV";
                }
                if(branch.equals("IV")) {
                    Toast.makeText(this, "Enter a valid usn", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.enableloading();
                    ValidatePhoneNumber(usn.toUpperCase(), name, password, branch);
                }
            }
        }

        private void ValidatePhoneNumber(final String usn, final String name, final String password , final  String branch)
        {
            Log.i("validate phone number " , "exceuting" );
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    Log.i("valiodate databse" , "exceuting" );
                    if(!(dataSnapshot.child("User").child(usn).exists()))
                    {
                        Log.i("databse mapping" , "exceuting" );

                        HashMap<String, Object> userdataMap =new HashMap<>();
                        userdataMap.put("name",name);
                        userdataMap.put("password",password);
                        userdataMap.put("usn",usn);
                        userdataMap.put("branch",branch);

                        RootRef.child("User").child(usn).setValue(userdataMap)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful())
                                    {
                                        Log.i("databse" , "success" );

                                        Toast.makeText(SignupActivity.this, "Your account is created successfully", Toast.LENGTH_SHORT).show();

                                        loadingBar.disableloading();
                                        Intent intent =new Intent(SignupActivity.this , LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.disableloading();

                                        Toast.makeText(SignupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(SignupActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else
                    if((dataSnapshot.child("User").child(usn).exists()))
                    {
                        Log.i("databse add" , "secess" );

                        Toast.makeText(SignupActivity.this, "Account with this "+usn+" already exists", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignupActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(SignupActivity.this , LoginActivity.class);
                        loadingBar.disableloading();
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("databse mapping" , "fail" );
                    loadingBar.disableloading();

                }
            });
        }

    public void onLoginClick(View view) {
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.fade_in);
    }
}
