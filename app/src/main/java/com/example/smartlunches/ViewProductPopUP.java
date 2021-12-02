package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartlunches.Model.Products;
import com.example.smartlunches.Prevelent.Prevelent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewProductPopUP extends AppCompatActivity {

    private Button addTocart;
    private TextView productName,productDiscription,productPrice , productType ;
    private ImageView productImage, productVeg;
    private String productID,price ,pimage;
    private ElegantNumberButton numberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        productID = getIntent().getStringExtra("pid");

        addTocart = (Button) findViewById(R.id.addtocart_btn);
        productImage = (ImageView) findViewById(R.id.item_img);
        productVeg = (ImageView) findViewById(R.id.item_vegornonveg);
        productName = (TextView) findViewById(R.id.item_name);
        productDiscription = (TextView) findViewById(R.id.item_id);
        productPrice = (TextView) findViewById(R.id.item_cost);
        numberButton = (ElegantNumberButton) findViewById(R.id.numberbutton);

//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        getWindow().setLayout((int) (width * .8), (int) (height * .8));
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.gravity = Gravity.CENTER;
//        params.x = 0;
//        params.y = -20;
//
//        getWindow().setAttributes(params);

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProductDetails(productID);
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Items");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText("â‚¹" + products.getPrice());
                    productDiscription.setText(products.getPid());
                    Picasso.get().load(products.getImage()).into(productImage);

                    price = products.getPrice();

                    pimage = products.getImage().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        final String productid = productID.toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pimage", pimage);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", price);
        cartMap.put("quantity", numberButton.getNumber());

        cartListRef.child("User Cart").child(Prevelent.currentonlineUser.getUsn())
                .child("Items").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ViewProductPopUP.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(ViewProductPopUP.this, CartActivity.class);
                            intent.putExtra("pid", productID);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ViewProductPopUP.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}

