package com.example.smartlunches.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlunches.Interface.ItemClickListener;
import com.example.smartlunches.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice , itemvegornonveg , item_rating;
    public ImageView imageView;
    public ItemClickListener listener;
    public CheckBox available;
    public RelativeLayout layoutitem;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        layoutitem = itemView.findViewById(R.id.layoutitem);
        imageView = (ImageView) itemView.findViewById(R.id.item_img);
        txtProductName = (TextView) itemView.findViewById(R.id.item_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.item_id);
        txtProductPrice = (TextView) itemView.findViewById(R.id.item_cost);
        itemvegornonveg = itemView.findViewById(R.id.item_vegornonveg);
        item_rating = itemView.findViewById(R.id.item_rating);
        available = itemView.findViewById(R.id.checkforadding);
    }

    public void setItemClickListener(ItemClickListener listener)

    {
        this.listener = listener;
    }
    @Override
    public void onClick(View view)
    {
        listener.OnClick(view,getAdapterPosition(),false);
    }
}
