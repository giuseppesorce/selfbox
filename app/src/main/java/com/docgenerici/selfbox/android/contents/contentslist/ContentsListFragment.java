package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.PorterDuff;
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
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GalleryAdapter;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.OnItemContentClickListener;
import com.docgenerici.selfbox.android.contents.ContentActivityInterface;
import com.docgenerici.selfbox.android.contents.filters.FilterDialog;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce
 */

public class ContentsListFragment extends Fragment implements ContentListPresenter.ContentView, OnItemContentClickListener {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llAZ)
    LinearLayout llAZ;
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;
    @BindColor(R.color.grey_filter)
    int grey_filter;
    @BindDrawable(R.drawable.sample1)
    Drawable sample1;
    @BindDrawable(R.drawable.sample2)
    Drawable sample2;
    @BindDrawable(R.drawable.sample3)
    Drawable sample3;
    private ContentListPresenter presenter;
    private GalleryAdapter galleryAdapter;
    private FilterDialog filtersDialog;

    private ArrayList<ContentDoc> contents;private ContentActivityInterface activityInterface;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contents_list, container, false);
        ButterKnife.bind(this, view);
        presenter = SelfBoxApplicationImpl.appComponent.contentListPresenter();
        presenter.setup(sample1, sample2, sample3);
        presenter.setView(this);
        if (getArguments() != null) {

        }
        createGalleryContentsItems();
        presenter.selectAZ();
        etSearch.clearFocus();
        return view;
    }

    private void createGalleryContentsItems() {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(gridLayout);

        galleryAdapter = new GalleryAdapter(getActivity(), presenter.getContents(), this);
        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        rvGallery.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvGallery.setAdapter(galleryAdapter);
    }

    @OnClick(R.id.llAZ)
    void onSelectAZ() {
        presenter.selectAZ();
    }

    @OnClick(R.id.llDate)
    void onSelectDate() {
        presenter.selectDate();
    }

    @OnClick(R.id.btFilter)
    void onSelectFilter() {
        presenter.onSelecteFilter();
    }

    @Override
    public void showSelectAz() {
        llAZ.getBackground().setColorFilter(grey_filter, PorterDuff.Mode.MULTIPLY);
        llDate.getBackground().clearColorFilter();
    }

    @Override
    public void showSelectDate() {
        llAZ.getBackground().clearColorFilter();
        llDate.getBackground().setColorFilter(grey_filter, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void openFilterDialog() {

    }

    @Override
    public void refreshContents() {
        galleryAdapter.notifyDataSetChanged();
        if(activityInterface !=null){
            activityInterface.setShared(presenter.getContentsShared());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            activityInterface = (ContentActivityInterface) getActivity();
        }
    }



    @Override
    public void onItemClick(View view, int position) {

    }

    public static ContentsListFragment createInstance() {
        ContentsListFragment frag = new ContentsListFragment();
        Bundle init = new Bundle();
        frag.setArguments(init);
        return frag;
    }

    @Override
    public void onSelectShare(int position) {
        presenter.setShare(position);

    }
}
