package com.docgenerici.selfbox.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce
 */
public class ListContentShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener onClickListener;

    ArrayList<ContentDoc> contentDocs = new ArrayList<>();

    public ListContentShareAdapter(ArrayList<ContentDoc> contentDocList, OnItemClickListener listener) {
        this.contentDocs = contentDocList;
        this.onClickListener= listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_content_share, parent, false);
            viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       ContentDoc contentDoc= contentDocs.get(position);
        ((MyItemHolder)holder).tvShareTitle.setText(contentDoc.name);
        ((MyItemHolder)holder).ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(null, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contentDocs.size();
    }

    public  class MyItemHolder extends RecyclerView.ViewHolder{

        TextView tvShareTitle;
        ImageView ivClose;

        public MyItemHolder(View itemView) {
            super(itemView);

            tvShareTitle= (TextView) itemView.findViewById(R.id.tvShareTitle);
            ivClose= (ImageView) itemView.findViewById(R.id.ivClose);

        }
    }
}
