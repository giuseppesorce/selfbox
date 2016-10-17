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

    private OnItemClickListener onClickListener;
    Context context;
    List<ContentDoc> contentDocs = new ArrayList<>();

    public GalleryAdapter(Context context, List<ContentDoc> contentDocList, OnItemClickListener listener) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       ContentDoc contentDoc= contentDocs.get(position);

        switch (contentDoc.getType()){
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
        ((MyItemHolder)holder).tvTitle.setText(contentDoc.getTitle());
        ((MyItemHolder)holder).ivCover.setImageDrawable(contentDoc.getImage());
//        Picasso.with(context).load(photo.path).resize(200,200).centerCrop().into(((MyItemHolder)holder).mImg);
//        if(photo.selectedgalley){
//            ((MyItemHolder)holder).ivSelectPhoto.setVisibility(View.VISIBLE);
//        }else{
//            ((MyItemHolder)holder).ivSelectPhoto.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return contentDocs.size();
    }

    public  class MyItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivCover;
        ImageView ivType;
        TextView tvTitle;

        public MyItemHolder(View itemView) {
            super(itemView);

            ivCover= (ImageView) itemView.findViewById(R.id.ivCover);
            ivType= (ImageView) itemView.findViewById(R.id.ivType);
            tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
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
