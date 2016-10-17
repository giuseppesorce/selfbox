package com.docgenerici.selfbox.android.adapters;

/**
 * Created by Giuseppe Sorce
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.models.ProductDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyHolder> {


    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private ArrayList<ProductDoc> productDocArrayList;

    public ProductsAdapter(Context context, ArrayList<ProductDoc> productDocArrayList, OnItemClickListener mItemClickListener) {
        mContext = context;
        this.productDocArrayList = productDocArrayList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SelfBoxConstants.TypeProductRow.HEADER) {
            return new MyViewHolderHeader(LayoutInflater.from(mContext).inflate(R.layout.item_header, parent, false));
        } else if (viewType == SelfBoxConstants.TypeProductRow.PRODUCT) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false));
        }else{
            throw new RuntimeException("Could not inflate layout");
        }
    }

    @Override
    public void onBindViewHolder(MyHolder viewHolder, int position) {
        final ProductDoc item = productDocArrayList.get(position);

        if (item.getTyperow() == SelfBoxConstants.TypeProductRow.HEADER) {
            ((MyViewHolderHeader) viewHolder).tvTitle.setText(item.getTitle());
        } else if (item.getTyperow() == SelfBoxConstants.TypeProductRow.PRODUCT) {
            ((MyViewHolder) viewHolder).tvProductname.setText(item.getTitle());
            ((MyViewHolder) viewHolder).tvProductSubname.setText(item.getSubtitle());
            ((MyViewHolder) viewHolder).tvClasse.setText(item.getClasse());
            ((MyViewHolder) viewHolder).tvNoInside.setText(item.getNoinside());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productDocArrayList.get(position).getTyperow();
    }


    @Override
    public int getItemCount() {
        return productDocArrayList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class MyViewHolder extends MyHolder implements View.OnClickListener {


        public TextView tvProductname;
        public TextView tvProductSubname;
        public TextView tvNoInside;
        private TextView tvClasse;
        private ImageView ivBarCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivBarCode = (ImageView) itemView.findViewById(R.id.ivBarCode);
            tvProductname = (TextView) itemView.findViewById(R.id.tvProductname);
            tvProductSubname = (TextView) itemView.findViewById(R.id.tvProductSubname);
            tvClasse = (TextView) itemView.findViewById(R.id.tvClasse);
            tvNoInside = (TextView) itemView.findViewById(R.id.tvNoInside);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public class MyViewHolderHeader extends MyHolder {
        public TextView tvTitle;
        public MyViewHolderHeader(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}