package com.docgenerici.selfbox.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SyncContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Sorce
 */
public class GridSyncAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    List<SyncContent> syncContents = new ArrayList<>();

    public GridSyncAdapter(Context context, List<SyncContent> contentDocList) {
        this.context = context;
        this.syncContents = contentDocList;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_sync, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    public SyncContent getSyncContentByType(int type) {
        for (int i = 0; i < syncContents.size(); i++) {
            if (syncContents.get(i).getType() == type) {
                return syncContents.get(i);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SyncContent syncContent = syncContents.get(position);

        ((MyItemHolder) holder).tvLabel.setText(syncContent.getTitle());
        ((MyItemHolder) holder).tvPercentage.setText(syncContent.getPercentage() + "%");
        float percentage = (float) ((float) syncContent.getPercentage() / 100.0f);
        ((MyItemHolder) holder).vRectPercentage.setScaleX(percentage);
        ((MyItemHolder) holder).vRectPercentage.setPivotX(0);

    }

    @Override
    public int getItemCount() {
        return syncContents.size();
    }

    public void changeItems(ArrayList<SyncContent> contents) {

        this.syncContents= contents;
        notifyDataSetChanged();

    }

    public class MyItemHolder extends RecyclerView.ViewHolder {

        private View vRectPercentage;
        TextView tvLabel;
        TextView tvPercentage;

        public MyItemHolder(View itemView) {
            super(itemView);


            tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
            vRectPercentage = itemView.findViewById(R.id.vRectPercentage);
            tvPercentage = (TextView) itemView.findViewById(R.id.tvPercentage);
        }


    }


}
