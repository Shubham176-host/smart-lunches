package com.example.smartlunches.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlunches.Interface.ItemClickListener;
import com.example.smartlunches.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
        public TextView txtProductName, txtProductQuantity, txtProductPrice , itemvegornonveg , item_rating;
        public ImageView imageView , delete_img;
        public ItemClickListener listener;

        public CartViewHolder(View itemView)
        {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.item_img);
                txtProductName = (TextView) itemView.findViewById(R.id.item_name);
                txtProductQuantity = (TextView) itemView.findViewById(R.id.item_q);
                txtProductPrice = (TextView) itemView.findViewById(R.id.item_cost);
                itemvegornonveg = itemView.findViewById(R.id.item_vegornonveg);
                item_rating = itemView.findViewById(R.id.all_menu_rating);
                delete_img = itemView.findViewById(R.id.delete_img);
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
