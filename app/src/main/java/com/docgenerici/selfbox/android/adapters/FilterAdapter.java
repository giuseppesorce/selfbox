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
import com.docgenerici.selfbox.models.FilterProduct;

import java.util.ArrayList;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyHolder> {


    private OnItemClickListener mItemClickListener;
    private Context mContext;
    private ArrayList<FilterProduct> filterProducts;
//    @BindDrawable(R.drawable.ic_check_check)
//    Drawable dwCheck;
//    @BindDrawable(R.drawable.ic_check_uncheck)
//    Drawable dwUnCheck;
    public FilterAdapter(Context context, ArrayList<FilterProduct> productDocArrayList, OnItemClickListener mItemClickListener) {
        mContext = context;
        this.filterProducts = productDocArrayList;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_filter, parent, false));

    }

    @Override
    public void onBindViewHolder(MyHolder viewHolder, int position) {
        final FilterProduct item = filterProducts.get(position);

        ((MyHolder)viewHolder).tvName.setText(item.name);
        ((MyHolder)viewHolder).vBar.setBackgroundColor(item.color);

        if(item.select){
            ((MyHolder)viewHolder).tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_check_check), null);
        }else{
            ((MyHolder)viewHolder).tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.ic_check_uncheck), null);
        }

    }



    @Override
    public int getItemCount() {
        return filterProducts.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private ImageView vBar;

        public MyHolder(View itemView) {
            super(itemView);
            vBar = (ImageView) itemView.findViewById(R.id.vBar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final FilterProduct item = filterProducts.get(getPosition());
            item.select= !item.select;
            mItemClickListener.onItemClick(view, 0);
            notifyDataSetChanged();
        }
    }



}