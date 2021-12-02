package com.example.smartlunches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartlunches.Model.AdminOrders;
import com.example.smartlunches.ui.Orders.OrdersFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminOrdersFragment extends Fragment {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private  String orderstatus = "pending";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_admin_orders, null);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = view.findViewById(R.id.admin_order_list);
        ordersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, OrdersFragment.AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, OrdersFragment.AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersFragment.AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        if(model.getOrderstatus())
                            orderstatus = "recieved";
                        else
                            orderstatus = "pending";

                        holder.userName.setText("Name: " + model.getName());
                        holder.userusn.setText("Usn: " + model.getUsn());
                        holder.userTotalPrice.setText("Amount =  Rs." + model.getTotalAmount());
                        holder.userDate.setText("Date: " + model.getDate());
                        holder.userTime.setText("Time: "+model.getTime());
                        holder.userproductid.setText("ID = " + model.getProductid());
                        holder.orderstatus.setText(orderstatus);
                        holder.userSize.setText(model.getSize());
                        holder.userusn.setText(model.getUsn());
                        Picasso.get().load(model.getOrderimg()).resize(200, 200).into(holder.orderimg);
                        holder.order_show_qr.setVisibility(View.INVISIBLE);
                    }

                    @NonNull
                    @Override
                    public OrdersFragment.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new OrdersFragment.AdminOrdersViewHolder(view);
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }
}

