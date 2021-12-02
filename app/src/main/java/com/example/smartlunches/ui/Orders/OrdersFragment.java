package com.example.smartlunches.ui.Orders;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlunches.LoginActivity;
import com.example.smartlunches.Model.AdminOrders;
import com.example.smartlunches.Prevelent.Prevelent;
import com.example.smartlunches.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private ImageView qrimg;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private String orderstatus = "pending";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ordersList = view.findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!Prevelent.loggedin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            ordersRef = FirebaseDatabase.getInstance().getReference().child("User").child(Prevelent.currentonlineUser.getUsn()).child("Order");

            FirebaseRecyclerOptions<AdminOrders> options =
                    new FirebaseRecyclerOptions.Builder<AdminOrders>()
                            .setQuery(ordersRef, AdminOrders.class)
                            .build();

            FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                    new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                            if(model.getOrderstatus())
                                orderstatus = "recieved";
                            else
                                orderstatus = "pending";
                            holder.userName.setText("Name: " + model.getName());
                            holder.userusn.setText("Usn: " + model.getUsn());
                            holder.userTotalPrice.setText("Amount =  Rs." + model.getTotalAmount());
                            holder.userDate.setText("Date: " + model.getDate());
                            holder.userTime.setText("Time: " + model.getTime());
                            holder.userproductid.setText("ID = " + model.getProductid());
                            holder.orderstatus.setText(orderstatus);
                            holder.userSize.setText(model.getSize());
                            holder.userusn.setText(model.getUsn());
                            if (model.getOrderstatus()) {
                                holder.order_show_qr.setVisibility(View.INVISIBLE);
                            }
                            Picasso.get().load(model.getOrderimg()).resize(200, 200).into(holder.orderimg);
                            holder.order_show_qr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    qrpopup(model.getOrderid());
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                            return new AdminOrdersViewHolder(view);
                        }
                    };

            ordersList.setAdapter(adapter);
            adapter.startListening();
        }
    }

    private void qrpopup(String orderid) {

        AlertDialog dialog;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(true);
        final View popupview = getLayoutInflater().inflate(R.layout.activity_qrcode_display , null);
        alertDialog.setView(popupview);
        dialog = alertDialog.create();
        dialog.show();

        qrimg = popupview.findViewById(R.id.idIVQrcode);

        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(orderid, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrimg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userusn, userTotalPrice, userDate , userTime, orderstatus, userproductid, userSize;
        public ImageView orderimg;
        public Button order_show_qr;

        public AdminOrdersViewHolder(View itemView)
        {
            super(itemView);
            userusn = itemView.findViewById(R.id.order_usn);
            userName = itemView.findViewById(R.id.order_user_name);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDate = itemView.findViewById(R.id.order_date);
            userTime = itemView.findViewById(R.id.order_time);
            userproductid= itemView.findViewById(R.id.order_product_id);
            orderstatus = itemView.findViewById(R.id.order_order_status);
            userSize= itemView.findViewById(R.id.order_size);
            orderimg = itemView.findViewById(R.id.order_item_img);
            order_show_qr = itemView.findViewById(R.id.order_show_qr);
        }
    }

    private void RemoverOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }
}

