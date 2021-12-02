package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlunches.Model.Cart;
import com.example.smartlunches.Model.LoadingDialog;
import com.example.smartlunches.Model.Products;
import com.example.smartlunches.Prevelent.Prevelent;
import com.example.smartlunches.ViewHolder.CartViewHolder;
import com.example.smartlunches.ui.home.HomeFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button continueshopping, proceedtopay, updatePrice;
    private TextView textTotalAmt;
    private String productid= "", productid2= "" , orderimg;
    private ImageView productimage , delete_img ;
    private float TotalPrice = 0%.2f;
    final LoadingDialog loadingBar = new LoadingDialog(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productid = getIntent().getStringExtra("pid");

        recyclerView = findViewById(R.id.cart_list);
        productimage = findViewById(R.id.product_image_details);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        updatePrice = findViewById(R.id.update_price_btn);
        proceedtopay = findViewById(R.id.proceed_to_pay);
        textTotalAmt = findViewById(R.id.total_price);

        textTotalAmt.setText("Rs." + String.valueOf(TotalPrice));

        updatePrice.setVisibility(View.INVISIBLE);
        if(!Prevelent.loggedin){
            startActivity(new Intent(CartActivity.this , LoginActivity.class));
        }
        else
        {
            ShowDetails();
        }
        updatePrice.setOnClickListener(v -> textTotalAmt.setText("Rs." + String.valueOf(TotalPrice)));

        proceedtopay.setOnClickListener(v -> {

            if(TotalPrice == 0)
            {
                Toast.makeText(CartActivity.this, "Add items to cart", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.enableloading();
                ConfirmOrder();
            }
        });
        updatePrice.callOnClick();
    }

    private void ShowDetails() {
        final DatabaseReference cartLisRef = FirebaseDatabase.getInstance().getReference().child("User Cart");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartLisRef.child(Prevelent.currentonlineUser.getUsn())
                                .child("Items"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Items").child(model.getPid());
                holder.txtProductQuantity.setText("Q: " + model.getQuantity());
                holder.txtProductPrice.setText("Rs." +model.getPrice());
                holder.txtProductName.setText(model.getPname());
                if(model.isItemveg()){
                    holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(CartActivity.this, R.drawable.ic_baseline_veg_mark));
                    holder.itemvegornonveg.setText("Veg");
                }
                else
                if(!(model.isItemveg())){
                    holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(CartActivity.this , R.drawable.ic_baseline_nonveg_mark));
                    holder.itemvegornonveg.setText("Non Veg");
                }

                String productid1 = "";
                productid1= model.getPid();
                productid2 = (productid1 +" | " +productid2 );
                Picasso.get().load(model.getPimage()).resize(200, 200).into(holder.imageView);
                orderimg = String.valueOf(model.getPimage());

                float oneTypeProductTPrice = ((Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity()));
                TotalPrice = TotalPrice + oneTypeProductTPrice;
                updatePrice.callOnClick();
                holder.delete_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartLisRef
                                .child(Prevelent.currentonlineUser.getUsn())
                                .child("Items")
                                .child(model.getPid()).getRef()
                                .removeValue()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        float oneTypeProductTPrice1 = ((Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity()));
                                        TotalPrice = TotalPrice - oneTypeProductTPrice1;

                                        Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                        textTotalAmt.setText("Rs." + TotalPrice);
                                        updatePrice.callOnClick();
                                    }
                                });

                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        updatePrice.callOnClick();
    }

    @Override
    public void onBackPressed() {
        new Intent(CartActivity.this, HomeActivity.class);
        finish();
    }
    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final long cuurentitmeinmili = System.currentTimeMillis();

        final DatabaseReference cartLisRef = FirebaseDatabase.getInstance().getReference().child("User Cart").child(Prevelent.currentonlineUser.getUsn()).child("Items");

        cartLisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {

                    Cart cart = snapshot.child(ds.getKey()).getValue(Cart.class);

                    final String OrderId = saveCurrentTime + cuurentitmeinmili + ds.getKey();

                    final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                            .child("Orders")
                            .child(OrderId);

                    HashMap<String, Object> ordersMap = new HashMap<>();
                    assert cart != null;
                    ordersMap.put("orderid", OrderId);
                    ordersMap.put("productid", cart.getPid());
                    ordersMap.put("orderimg", cart.getPimage());
                    ordersMap.put("name", Prevelent.currentonlineUser.getName());
                    ordersMap.put("usn", Prevelent.currentonlineUser.getUsn());
                    ordersMap.put("date", saveCurrentDate);
                    ordersMap.put("time", saveCurrentTime);
                    ordersMap.put("orderstatus", false);
                    ordersMap.put("totalAmount", cart.getPrice());

                    ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                final DatabaseReference userorder= FirebaseDatabase.getInstance().getReference().child("User").child(Prevelent.currentonlineUser.getUsn()).child("Order").child(OrderId);
                                userorder.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("User Cart")
                                                .child(Prevelent.currentonlineUser.getUsn())
                                                .child("Items")
                                                .child(cart.getPid())
                                                .getRef()
                                                .removeValue()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        loadingBar.disableloading();
                                                        Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(CartActivity.this , PaymentSuccessfulActivity.class));
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

