package com.docgenerici.selfbox.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce
 */
public class ListPharmauserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener onClickListener;
    ArrayList<FarmaciaDto> pharmaUserArrayList = new ArrayList<>();

    public ListPharmauserAdapter(ArrayList<FarmaciaDto> contentDocList, OnItemClickListener listener) {
        this.pharmaUserArrayList = contentDocList;
        this.onClickListener= listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_pharma_user, parent, false);
            viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        FarmaciaDto contentDoc= pharmaUserArrayList.get(position);
        ((MyItemHolder)holder).tvPharmaName.setText(contentDoc.fullname);
        if(contentDoc.selected){
            ((MyItemHolder)holder).tvPharmaName.setBackgroundResource(R.color.green_transparent);
        }else{
            ((MyItemHolder)holder).tvPharmaName.setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return pharmaUserArrayList.size();
    }

    public ArrayList<FarmaciaDto> getPharmaUsers() {
        return pharmaUserArrayList;
    }
    public FarmaciaDto getPharmaUser(int position) {
        return pharmaUserArrayList.get(position);
    }

    public void setPharmaList(ArrayList<FarmaciaDto> newlist) {
        pharmaUserArrayList.clear();
        pharmaUserArrayList.addAll(newlist);
        notifyDataSetChanged();

    }

    public  class MyItemHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView tvPharmaName;
        public MyItemHolder(View itemView) {
            super(itemView);
            tvPharmaName= (TextView) itemView.findViewById(R.id.tvPharmaName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v,getPosition());
        }
    }
}
