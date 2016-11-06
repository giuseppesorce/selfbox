package com.docgenerici.selfbox.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Sorce
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemContentClickListener onClickListener;
    Context context;
    List<ContentDoc> contentDocs = new ArrayList<>();

    public GalleryAdapter(Context context, List<ContentDoc> contentDocList, OnItemContentClickListener listener) {
        this.context = context;
        this.contentDocs = contentDocList;
        this.onClickListener= listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_content, parent, false);
            viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       ContentDoc contentDoc= contentDocs.get(position);

        switch (contentDoc.type){
            case SelfBoxConstants.TypeContent.PDF:
                ((MyItemHolder)holder).ivType.setImageResource(R.drawable.ic_type_pdf);
                break;
            case SelfBoxConstants.TypeContent.VISUAL:
                ((MyItemHolder)holder).ivType.setImageResource(R.drawable.ic_type_sprite);
                break;
            case SelfBoxConstants.TypeContent.FOLDER:
                ((MyItemHolder)holder).ivType.setImageResource(R.drawable.ic_type_folder);
                break;
        }
        if(contentDoc.shared){
            ((MyItemHolder)holder).ivShare.setImageResource(R.drawable.ic_share_red);
        }else{
            ((MyItemHolder)holder).ivShare.setImageResource(R.drawable.ic_share_grey);
        }
        ((MyItemHolder)holder).ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onSelectShare(position);
                }
            }
        });
        ((MyItemHolder)holder).tvTitle.setText(contentDoc.title);
        ((MyItemHolder)holder).ivCover.setImageResource(contentDoc.image);
        if(position % 2 ==0){
            ((MyItemHolder)holder).vNew.setVisibility(View.VISIBLE);
            ((MyItemHolder)holder).tvNew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contentDocs.size();
    }

    public  class MyItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View vNew;
        ImageView ivCover;
        ImageView ivShare;
        ImageView ivType;
        TextView tvTitle;
        TextView tvNew;

        public MyItemHolder(View itemView) {
            super(itemView);

            vNew= (View) itemView.findViewById(R.id.vNew);
            ivShare= (ImageView) itemView.findViewById(R.id.ivShare);
            ivCover= (ImageView) itemView.findViewById(R.id.ivCover);
            ivType= (ImageView) itemView.findViewById(R.id.ivType);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
            tvNew= (TextView) itemView.findViewById(R.id.tvNew);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onClickListener !=null){
                onClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


}
