package com.docgenerici.selfbox.android.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Sorce
 */
public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String category;
    private boolean canShare = true;
    private OnItemContentClickListener onClickListener;
    Context context;
    List<ContentDoc> contentDocs = new ArrayList<>();

    public GalleryAdapter(Context context, List<ContentDoc> contentDocList, boolean canshare, String category, OnItemContentClickListener listener) {
        this.context = context;
        this.contentDocs = contentDocList;
        this.onClickListener = listener;
        this.canShare = canshare;
        this.category = category;
    }


    @Override
    public int getItemViewType(int position) {
        return contentDocs.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SelfBoxConstants.TypeContent.VISUAL) {
            return new MyItemHolderVisual(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_content_visual, parent, false));
        } else {
            return new MyItemHolderVisual(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_content, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ContentDoc contentDoc = contentDocs.get(position);

        switch (contentDoc.type) {
            case SelfBoxConstants.TypeContent.PDF:
                ((MyHolder) holder).ivType.setImageResource(R.drawable.ic_type_pdf);
                break;
            case SelfBoxConstants.TypeContent.VISUAL:
                ((MyHolder) holder).ivType.setImageResource(R.drawable.ic_type_sprite);
                break;
            case SelfBoxConstants.TypeContent.FOLDER:
                ((MyHolder) holder).ivType.setImageResource(R.drawable.ic_type_folder);
                break;
            case SelfBoxConstants.TypeContent.VIDEO:
                ((MyHolder) holder).ivType.setImageResource(R.drawable.ic_type_play);
                break;
        }

        if (contentDoc.shared) {
            ((MyHolder) holder).ivShare.setImageResource(R.drawable.ic_share_red);
        } else {
            ((MyHolder) holder).ivShare.setImageResource(R.drawable.ic_share_grey);
        }
        ((MyHolder) holder).ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onSelectShare(position);
                }
            }
        });

        ((MyHolder) holder).tvTitle.setText(contentDoc.name);
        Dbg.p("COVER["+contentDoc.id+"] : "+contentDoc.cover);

        if (contentDoc.cover != null && !contentDoc.cover.isEmpty()) {
            Picasso.with(context).load(new File(contentDoc.cover)).resize(700, 450).centerCrop().into(((MyHolder) holder).ivCover);
        } else {
            ((MyHolder) holder).ivCover.setImageResource(contentDoc.image);
        }
        if (contentDoc.isFolder) {
            ((MyHolder) holder).ivShare.setVisibility(View.INVISIBLE);
        } else {

            if (canShare) {
                ((MyHolder) holder).ivShare.setVisibility(View.VISIBLE);
            } else {
                ((MyHolder) holder).ivShare.setVisibility(View.INVISIBLE);
            }

        }

        if (contentDoc.typeview == SelfBoxConstants.TypeViewContent.NEW) {
            ((MyHolder) holder).vNew.setVisibility(View.VISIBLE);
            ((MyHolder) holder).tvNew.setVisibility(View.VISIBLE);
            ((MyHolder) holder).rlItem.setAlpha(1.0f);
            ((MyHolder) holder).ivBorder.setVisibility(View.GONE);
            ((MyHolder) holder).ivImp.setVisibility(View.GONE);
            ((MyHolder) holder).ivImageBackground.setBackgroundResource(R.drawable.bk_item_grid);
        } else if (contentDoc.typeview == SelfBoxConstants.TypeViewContent.IMPORTANT) {
            ((MyHolder) holder).vNew.setVisibility(View.GONE);
            ((MyHolder) holder).tvNew.setVisibility(View.GONE);
            ((MyHolder) holder).rlItem.setAlpha(1.0f);


            ((MyHolder) holder).ivBorder.setVisibility(View.VISIBLE);
            ((MyHolder) holder).ivImp.setVisibility(View.VISIBLE);
            ((MyHolder) holder).ivImageBackground.setBackgroundResource(R.drawable.bk_item_grid);
        } else if (contentDoc.typeview == SelfBoxConstants.TypeViewContent.IMPORTANT_NEW) {
            ((MyHolder) holder).vNew.setVisibility(View.VISIBLE);
            ((MyHolder) holder).tvNew.setVisibility(View.VISIBLE);
            ((MyHolder) holder).rlItem.setAlpha(1.0f);
            ((MyHolder) holder).ivBorder.setVisibility(View.VISIBLE);
            ((MyHolder) holder).ivImp.setVisibility(View.VISIBLE);
            ((MyHolder) holder).ivImageBackground.setBackgroundResource(R.drawable.bk_item_grid);


        } else if (contentDoc.typeview == SelfBoxConstants.TypeViewContent.READ) {
            ((MyHolder) holder).vNew.setVisibility(View.GONE);
            ((MyHolder) holder).tvNew.setVisibility(View.GONE);
            ((MyHolder) holder).rlItem.setAlpha(0.5f);
            ((MyHolder) holder).ivBorder.setVisibility(View.GONE);
            ((MyHolder) holder).ivImp.setVisibility(View.GONE);
            ((MyHolder) holder).ivImageBackground.setBackgroundResource(R.drawable.bk_item_gridshadw);
        } else {
            ((MyHolder) holder).rlItem.setAlpha(1.0f);
            ((MyHolder) holder).vNew.setVisibility(View.GONE);
            ((MyHolder) holder).ivBorder.setVisibility(View.GONE);
            ((MyHolder) holder).tvNew.setVisibility(View.GONE);
            ((MyHolder) holder).ivImp.setVisibility(View.GONE);
            ((MyHolder) holder).ivImageBackground.setBackgroundResource(R.drawable.bk_item_grid);
        }
        switch ((category)) {

            case "isf":
                ((MyHolder) holder).vNew.setBackgroundResource(R.drawable.back_rounded_orange_new);
                ((MyHolder) holder).ivBorder.setBackgroundResource(R.drawable.borderline);
                ((MyHolder) holder).ivImp.setBackgroundResource(R.drawable.ic_imp_orange);
                break;
            case "medico":
                ((MyHolder) holder).vNew.setBackgroundResource(R.drawable.back_rounded_blue_new);
                ((MyHolder) holder).ivBorder.setBackgroundResource(R.drawable.borderlineblu);
                ((MyHolder) holder).ivImp.setBackgroundResource(R.drawable.ic_imp_blu);

                break;
            case "pharma":
                ((MyHolder) holder).vNew.setBackgroundResource(R.drawable.back_rounded_green_new);
                ((MyHolder) holder).ivBorder.setBackgroundResource(R.drawable.borderlinegreen);
                ((MyHolder) holder).ivImp.setBackgroundResource(R.drawable.ic_imp_green);

                break;
        }

    }

    @Override
    public int getItemCount() {
        return contentDocs.size();
    }

    public ContentDoc getItemPosition(int position) {
        return contentDocs.get(position);
    }

    public ArrayList<ContentDoc> getItems() {
        return (ArrayList<ContentDoc>) contentDocs;
    }

    public void changeItems(List<ContentDoc> filtered) {
        contentDocs = filtered;
        notifyDataSetChanged();

    }

    public void setContents(List<ContentDoc> contents) {

    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View vNew;
        ImageView ivImageBackground;
        ImageView ivCover;
        ImageView ivShare;
        ImageView ivBorder;
        ImageView ivType;
        ImageView ivImp;


        TextView tvTitle;
        TextView tvNew;
        RelativeLayout rlItem;

        public MyHolder(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            vNew = (View) itemView.findViewById(R.id.vNew);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            ivImp = (ImageView) itemView.findViewById(R.id.ivImp);
            ivImageBackground = (ImageView) itemView.findViewById(R.id.ivImageBackground);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            ivBorder = (ImageView) itemView.findViewById(R.id.ivBorder);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvNew = (TextView) itemView.findViewById(R.id.tvNew);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public class MyItemHolderVisual extends MyHolder implements View.OnClickListener {
        View vNew;
        ImageView ivCover;
        ImageView ivShare;
        ImageView ivBorder;
        ImageView ivType;
        TextView tvTitle;
        ImageView ivImp;
        TextView tvNew;
        RelativeLayout rlItem;

        public MyItemHolderVisual(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            vNew = (View) itemView.findViewById(R.id.vNew);
            ivBorder = (ImageView) itemView.findViewById(R.id.ivBorder);
            ivImp = (ImageView) itemView.findViewById(R.id.ivImp);
            ivImageBackground = (ImageView) itemView.findViewById(R.id.ivImageBackground);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvNew = (TextView) itemView.findViewById(R.id.tvNew);
            ivBorder = (ImageView) itemView.findViewById(R.id.ivBorder);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onItemClick(view, getAdapterPosition());
            }
        }

    }

    public class MyItemHolder extends MyHolder implements View.OnClickListener {
        View vNew;
        ImageView ivCover;
        RelativeLayout rlItem;
        ImageView ivBorder;
        ImageView ivShare;
        ImageView ivImp;
        ImageView ivType;
        TextView tvTitle;
        TextView tvNew;

        public MyItemHolder(View itemView) {
            super(itemView);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            vNew = (View) itemView.findViewById(R.id.vNew);
            ivImp = (ImageView) itemView.findViewById(R.id.ivImp);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            ivBorder = (ImageView) itemView.findViewById(R.id.ivBorder);
            ivImageBackground = (ImageView) itemView.findViewById(R.id.ivImageBackground);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvNew = (TextView) itemView.findViewById(R.id.tvNew);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onClickListener != null) {
                onClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


}
