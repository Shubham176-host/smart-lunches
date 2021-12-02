package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smartlunches.Model.LoadingDialog;
import com.example.smartlunches.Model.Users;
import com.example.smartlunches.Prevelent.Prevelent;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressEditText;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText userusn , userpassword;
    Button loginbtn;
    final LoadingDialog loadingBar = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.loginbtn);
        userusn = findViewById(R.id.usn);
        userpassword = findViewById(R.id.password);

        Paper.init(this);

        loginbtn.setOnClickListener(v -> {
            LoginUser();
        });
    }

        private void LoginUser()
        {
            String usn = userusn.getText().toString();
            String password= userpassword.getText().toString();

            usn = usn.toUpperCase();

            if(usn.equals("8050685189") || usn.equals("7204957656"))
            {
                Intent intent = new Intent(LoginActivity.this  , AdminActivity.class);
                startActivity(intent);
            }
            else
            if(TextUtils.isEmpty(usn))
            {
                Toast.makeText(LoginActivity.this, "Please Enter your usn number....", Toast.LENGTH_SHORT).show();
            }
            else
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(LoginActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
            }
            else
            if(TextUtils.getTrimmedLength(usn) < 9)
            {
                Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                AllowAccess(usn, password);
                loadingBar.enableloading();
            }
        }

        private void AllowAccess(final String usn, final String password)
        {
            final DatabaseReference RootRef;
            RootRef= FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("User").child(usn).exists())
                    {
                        Users userData = dataSnapshot.child("User").child(usn).getValue(Users.class);

                        if (userData.getUsn().equals(usn))
                        {
                            if (userData.getPassword().equals(password))
                            {
                                Paper.book().write(Prevelent.UserUsnKey, usn);
                                Paper.book().write(Prevelent.UserPasswordKey, password);
                                Toast.makeText(LoginActivity.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.disableloading();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevelent.currentonlineUser = userData; //s
                                Prevelent.loggedin = true;
                                startActivity(intent);

                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                loadingBar.disableloading();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error in get method", Toast.LENGTH_SHORT).show();
                            loadingBar.disableloading();
                        }
                    }
                    else
                        {
                        Toast.makeText(LoginActivity.this, "Account with " + usn + " does not exist", Toast.LENGTH_SHORT).show();
                            loadingBar.disableloading();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    loadingBar.disableloading();

                }

            });
        }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this,SignupActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.fade_in);
    }
}
