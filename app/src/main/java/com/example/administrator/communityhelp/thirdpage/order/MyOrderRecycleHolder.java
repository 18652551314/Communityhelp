package com.example.administrator.communityhelp.thirdpage.order;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

public class MyOrderRecycleHolder extends RecyclerView.ViewHolder {
        ImageView imageViewGoodsPic;
        TextView textViewGoodsName;
        TextView textViewOrderTime;
        TextView textViewTotalCost;
        TextView textViewTotalCount;
        Button btnDelete, btnPay;
        LinearLayout linearLayout_myoder_item;

        public MyOrderRecycleHolder(View itemView) {
            super(itemView);
            imageViewGoodsPic = (ImageView) itemView.findViewById(R.id.imageView_myOrder_item);
            textViewGoodsName = (TextView) itemView.findViewById(R.id.textView_myOrder_item_goodsName);
            textViewOrderTime = (TextView) itemView.findViewById(R.id.textView_myOrder_item_time);
            textViewTotalCost = (TextView) itemView.findViewById(R.id.textView_myOrder_item_totolCost);
            textViewTotalCount = (TextView) itemView.findViewById(R.id.textView_myOrder_item_totolCount);
            btnDelete = (Button) itemView.findViewById(R.id.btn_myOrder_item_delate);
            btnPay = (Button) itemView.findViewById(R.id.btn_myOrder_item_pay);
            linearLayout_myoder_item = (LinearLayout) itemView.findViewById(R.id.linearLayout_myoder_item);
        }
    }