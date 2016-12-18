package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.adapters.GalleryAdapter;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.OnItemContentClickListener;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @uthor giuseppesorce
 */

public class ListContentByFolderFragment extends Fragment implements ContentListPresenter.ContentView, OnItemContentClickListener {


    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llAZ)
    LinearLayout llAZ;
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;
//    @BindColor(R.color.grey_filter)
//    int grey_filter;
//    @BindDrawable(R.drawable.sample1)
//    Drawable sample1;
//    @BindDrawable(R.drawable.sample2)
//    Drawable sample2;
//    @BindDrawable(R.drawable.sample3)
//    Drawable sample3;
//    @BindView(R.id.btFilter)

    private String categoryContent;
    private ArrayList<ContentDoc> contentsByFolder;
    private GalleryAdapter galleryAdapter;

    public static ListContentByFolderFragment createInstance(ArrayList<ContentDoc> contents) {
        ListContentByFolderFragment frag = new ListContentByFolderFragment();
        Bundle init = new Bundle();
        Dbg.p("contents: "+contents.size());
        init.putParcelableArrayList("contents", contents);
        frag.setArguments(init);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contents_list, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            contentsByFolder = getArguments().getParcelableArrayList("contents");


        }

       createGalleryContentsItems();
        return  view;
    }


    private void createGalleryContentsItems() {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(gridLayout);
        galleryAdapter = new GalleryAdapter(getActivity(), getContents(), this);
        int spanCount = 3; // 3 columns
        int spacing = 40; // 50px
        boolean includeEdge = false;
        rvGallery.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvGallery.setAdapter(galleryAdapter);
    }

    private ArrayList<ContentDoc> getContents() {
        return  contentsByFolder;
    }

    @Override
    public void onSelectShare(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void showSelectAz() {

    }

    @Override
    public void showSelectDate() {

    }

    @Override
    public void openFilterDialog() {

    }

    @Override
    public void refreshContents() {

    }
}
