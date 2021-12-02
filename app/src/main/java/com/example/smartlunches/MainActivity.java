package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlunches.Model.Users;
import com.example.smartlunches.Prevelent.Prevelent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 4000;
    //variables
    Animation topAnim, bottomAnim,fadein;
    GifImageView image1;
    ImageView image2;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Paper.init(this);
//Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        fadein=AnimationUtils.loadAnimation(this,R.anim.fade_in);
        //hooks
        image1=findViewById(R.id.logo_image);
        image2=findViewById(R.id.imageView3);
        slogan=findViewById(R.id.textView);

        image1.setAnimation(topAnim);
        image2.setAnimation(fadein);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Checkuserdetails();
            }
        },SPLASH_SCREEN);

    }
    private void Checkuserdetails() {
        String UserUsnKey = Paper.book().read(Prevelent.UserUsnKey);
        String UserPasswordKey = Paper.book().read(Prevelent.UserPasswordKey);
        if (UserUsnKey != "" && UserPasswordKey != "" && !Prevelent.loggedin)
        {
                if(!TextUtils.isEmpty(UserUsnKey) && !TextUtils.isEmpty(UserPasswordKey))
                {
                    AllowAccess(UserUsnKey, UserPasswordKey);
                }
                else
                {
                    startActivity(new Intent(MainActivity.this , HomeActivity.class));
                }
        }
        else
        {
            startActivity(new Intent(MainActivity.this , HomeActivity.class));
        }
    }
    private void AllowAccess (final String usn, final String password){
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("User").child(usn).exists()) {
                    Users userData = dataSnapshot.child("User").child(usn).getValue(Users.class);
                    if (userData.getUsn().equals(usn)) {
                        if (userData.getPassword().equals(password))
                        {
                            Prevelent.currentonlineUser = userData;
                            Prevelent.loggedin = true;
                            startActivity(new Intent(MainActivity.this , HomeActivity.class));
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Retry Login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with " + usn + " does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}