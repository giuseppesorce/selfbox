package com.docgenerici.selfbox.android.adapters;

/**
 * Created by Giuseppe Sorce
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.models.ProductDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;

import java.util.ArrayList;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyHolder> {


    private OnSelectProdcutDetail mItemClickListener;
    private Context mContext;
    private ArrayList<ProductDoc> productDocArrayList;

    public ProductsAdapter(Context context, ArrayList<ProductDoc> productDocArrayList, OnSelectProdcutDetail mItemClickListener) {
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
    public void onBindViewHolder(MyHolder viewHolder, final int position) {
        final ProductDoc item = productDocArrayList.get(position);

        if (item.getTyperow() == SelfBoxConstants.TypeProductRow.HEADER) {
            ((MyViewHolderHeader) viewHolder).tvTitle.setText(item.getTitle());
        } else if (item.getTyperow() == SelfBoxConstants.TypeProductRow.PRODUCT) {
            ((MyViewHolder) viewHolder).tvProductname.setText(item.getTitle());
            ((MyViewHolder) viewHolder).tvProductSubname.setText(item.getSubtitle());
            ((MyViewHolder) viewHolder).tvClasse.setText(item.getClasse());
            ((MyViewHolder) viewHolder).tvNoInside.setText(item.getNoinside());
            ((MyViewHolder) viewHolder).vLine.setBackgroundColor(item.getColor());

            ((MyViewHolder) viewHolder).btCp.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mItemClickListener.onSelectRpc(item);
                }
            });
            ((MyViewHolder) viewHolder).btPdf.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mItemClickListener.onSelectScheda(item);

                }
            });

            if(item.getUriPdf() ==null || item.getUriPdf().length() < 2 || item.getUriPdf().contains("error_")){
                ((MyViewHolder) viewHolder).btCp.setEnabled(false);
                ((MyViewHolder) viewHolder).btCp.setAlpha(0.6f);
            }else{
                ((MyViewHolder) viewHolder).btCp.setEnabled(true);
                ((MyViewHolder) viewHolder).btCp.setAlpha(1.0f);
            }
            if(item.getUriSchedaPdf() ==null || item.getUriSchedaPdf().length() < 2 || item.getUriSchedaPdf().contains("error_")){
                ((MyViewHolder) viewHolder).btPdf.setEnabled(false);
                ((MyViewHolder) viewHolder).btPdf.setAlpha(0.6f);
            }else{
                ((MyViewHolder) viewHolder).btPdf.setEnabled(true);
                ((MyViewHolder) viewHolder).btPdf.setAlpha(1.0f);
            }

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

    public class MyViewHolder extends MyHolder  {


        public View vLine;
        public TextView tvProductname;
        public TextView tvProductSubname;
        public TextView tvNoInside;
        private TextView tvClasse;
        private ImageView ivBarCode;
         Button btPdf;
         Button btCp;
        public MyViewHolder(final View itemView) {
            super(itemView);
            vLine =  itemView.findViewById(R.id.vLine);
            ivBarCode = (ImageView) itemView.findViewById(R.id.ivBarCode);
            tvProductname = (TextView) itemView.findViewById(R.id.tvProductname);
            tvProductSubname = (TextView) itemView.findViewById(R.id.tvProductSubname);
            tvClasse = (TextView) itemView.findViewById(R.id.tvClasse);
            tvNoInside = (TextView) itemView.findViewById(R.id.tvNoInside);
            btPdf = (Button) itemView.findViewById(R.id.btPdf);
            btCp = (Button) itemView.findViewById(R.id.btCp);
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