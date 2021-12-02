package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smartlunches.Model.Products;
import com.example.smartlunches.Prevelent.Prevelent;
import com.example.smartlunches.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class AdminAddNewItemFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String TempPid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_add_new_item, container , false);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Items"); //firebase of the main data base of Products

        Paper.init(getActivity());

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = view.findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getPid());
                        holder.txtProductPrice.setText("Rs." + model.getPrice());
                        if(model.isItemveg()){
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity() , R.drawable.ic_baseline_veg_mark));
                            holder.itemvegornonveg.setText("Veg");
                        }
                        else
                        if(!(model.isItemveg())){
                            holder.itemvegornonveg.setBackground(ContextCompat.getDrawable(getActivity() , R.drawable.ic_baseline_nonveg_mark));
                            holder.itemvegornonveg.setText("Non Veg");
                        }
                        if(model.getAvailable()){
                            holder.available.setChecked(true);
                        }
                        Picasso.get().load(model.getImage())
                                .resize(400,400).into(holder.imageView);

                        holder.imageView.setLongClickable(true);
                        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                TempPid = model.getPid();
                                builder.setTitle("Warning Option")
                                .setCancelable(false)
                                .setMessage("Do you want delete this Item Permanently?")
                                .setPositiveButton( "Yes" , (DialogInterface.OnClickListener) dialogclick).setNegativeButton("No" , (DialogInterface.OnClickListener) dialogclick).show();
                                return true;
                            }
                        });


                        holder.available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                final boolean available;
                                HashMap<String , Object> productmap = new HashMap<>();
                                available = isChecked;
                                productmap.put("available" , available);
                                ProductsRef.child(model.getPid()).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    DialogInterface.OnClickListener dialogclick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    ProductsRef.child(TempPid).getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}