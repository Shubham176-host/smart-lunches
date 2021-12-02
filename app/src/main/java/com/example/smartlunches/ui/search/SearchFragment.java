package com.example.smartlunches.ui.search;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartlunches.CartActivity;
import com.example.smartlunches.Model.Products;
import com.example.smartlunches.Prevelent.Prevelent;
import com.example.smartlunches.R;
import com.example.smartlunches.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {

    RecyclerView popularRecyclerView, recommendedRecyclerView, recyclerView;
    private DatabaseReference ProductsRef;
    String productID;
    private Button addTocart;
    private TextView productName,productPrice , productVeg  , producttype;
    private ImageView productImage , backbtnimg;
    private String price ,pimage;
    private ElegantNumberButton numberButton;
    boolean availab = false , productveg = false;
    List<String> item_list = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Items"); //firebase of the main data base of Products

        popularRecyclerView = view.findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManagerp = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView.setLayoutManager(layoutManagerp);

        recommendedRecyclerView = view.findViewById(R.id.recommended_recycler);
        RecyclerView.LayoutManager layoutManagerr = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recommendedRecyclerView.setLayoutManager(layoutManagerr);

        recyclerView = view.findViewById(R.id.all_menu_recycler);
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity() , 2 , GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayout);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options1 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("rating").startAt("4"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter1 =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        Picasso.get().load(model.getImage())
                                .resize(400, 400).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productID = model.getPid();
                                ietmdetailspopup();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_recycler_items, parent, false);
                        return new ProductViewHolder(view);
                    }
                };
        popularRecyclerView.setAdapter(adapter1);
        adapter1.startListening();

        FirebaseRecyclerOptions<Products> options2 =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("rating").startAt("3"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter2 =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Rs." + model.getPrice());
                        holder.item_rating.setText(model.getRating());
                        if (model.isItemveg()) {
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_veg_mark));
                            holder.itemvegornonveg.setText("Veg");
                        } else if (!(model.isItemveg())) {
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_nonveg_mark));
                            holder.itemvegornonveg.setText("Non Veg");
                        }
                        Picasso.get().load(model.getImage())
                                .resize(400, 400).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productID = model.getPid();
                                ietmdetailspopup();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };
        recommendedRecyclerView.setAdapter(adapter2);
        adapter2.startListening();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Rs." + model.getPrice());
                        holder.item_rating.setText(model.getRating());
                        if (model.isItemveg()) {
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_veg_mark));
                            holder.itemvegornonveg.setText("Veg");
                        } else if (!(model.isItemveg())) {
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_nonveg_mark));
                            holder.itemvegornonveg.setText("Non Veg");
                        }
                        Picasso.get().load(model.getImage())
                                .resize(400, 400).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                productID = model.getPid(); ietmdetailspopup();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void ietmdetailspopup()
    {
        getProductDetails(productID);
        AlertDialog dialog;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(false);
        final View popupview = getLayoutInflater().inflate(R.layout.activity_food_details , null);
        alertDialog.setView(popupview);
        dialog = alertDialog.create();
        dialog.show();

        addTocart = (Button) popupview.findViewById(R.id.addtocart_btn);
        productImage = (ImageView)  popupview.findViewById(R.id.item_img);
        productVeg = (TextView)  popupview.findViewById(R.id.item_vegornon);
        productName = (TextView)  popupview.findViewById(R.id.item_name);
        productPrice = (TextView)  popupview.findViewById(R.id.item_cost);
        numberButton = (ElegantNumberButton)  popupview.findViewById(R.id.numberbutton);
        backbtnimg = popupview.findViewById(R.id.backbtnimg);
        producttype = popupview.findViewById(R.id.item_type);

        backbtnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(availab)
                    addingToCartList();
            }
        });
    }

    private void getProductDetails(String productID)
    {
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

        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Items");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Products products = dataSnapshot.getValue(Products.class);

                productName.setText(products.getPname());
                productPrice.setText("Rs." + products.getPrice());
                producttype.setText(item_list.get((int) products.getType()));
                Picasso.get().load(products.getImage()).into(productImage);
                if(products.isItemveg()){
                    productVeg.setBackground(ContextCompat.getDrawable(getActivity() , R.drawable.ic_baseline_veg_mark));
                    productVeg.setText("Veg");
                    productveg = true;
                }
                else
                if(!(products.isItemveg())){
                    productVeg.setBackground(ContextCompat.getDrawable(getActivity() , R.drawable.ic_baseline_nonveg_mark));
                    productVeg.setText("Non Veg");
                    productveg = false;
                }
                if(!products.getAvailable()){
                    addTocart.setBackgroundResource(R.drawable.add_to_cart_bg);
                    addTocart.setText("Currently Unavailable");
                    availab = false;
                }
                else
                if(products.getAvailable()){
                    availab = true;
                }

                price = products.getPrice();

                pimage = products.getImage();
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
        cartMap.put("itemveg" ,productveg );

        cartListRef.child("User Cart").child(Prevelent.currentonlineUser.getUsn())
                .child("Items").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(getActivity(), CartActivity.class);
                            intent.putExtra("pid", productID);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
