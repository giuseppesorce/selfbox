package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GalleryAdapter;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.OnItemContentClickListener;
import com.docgenerici.selfbox.android.contents.ContentActivityInterface;
import com.docgenerici.selfbox.android.contents.MainContentPresenterImpl;
import com.docgenerici.selfbox.android.contents.filters.FilterDialog;
import com.docgenerici.selfbox.android.pdf.PdfActivity;
import com.docgenerici.selfbox.android.pdf.PdfFragment;
import com.docgenerici.selfbox.android.video.VideoActivity;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.io.File;
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
    @BindView(R.id.btFilter)
    Button btFilter;
    private ContentListPresenter presenter;
    private GalleryAdapter galleryAdapter;
    private FilterDialog filtersDialog;
    private ContentActivityInterface activityInterface;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contents_list, container, false);
        ButterKnife.bind(this, view);
        presenter = SelfBoxApplicationImpl.appComponent.contentListPresenter();
        presenter.setup(R.drawable.sample1, R.drawable.sample2, R.drawable.sample3);
        presenter.setView(this);
        createGalleryContentsItems();
        presenter.selectAZ();
        etSearch.clearFocus();
        String category= SelfBoxApplicationImpl.appComponent.mainContentPresenter().getCategory();
        Resources res= getResources();
        switch ((category)){

            case "isf":
                btFilter.setBackgroundResource(R.drawable.ic_filter_orange);
                Drawable ic_search_orange= res.getDrawable(R.drawable.ic_search_orange);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_orange, null);
                break;
            case "medico":
                Drawable ic_search_blue= res.getDrawable(R.drawable.ic_search_blu);
                btFilter.setBackgroundResource(R.drawable.ic_filter_blu);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_blue, null);

                break;
            case "pharma":
                Drawable ic_search_green= res.getDrawable(R.drawable.ic_search);
                btFilter.setBackgroundResource(R.drawable.ic_filter_green);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_green, null);
                break;
        }

        return view;
    }

    private void createGalleryContentsItems() {
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(gridLayout);
        galleryAdapter = new GalleryAdapter(getActivity(), presenter.getContents(), this);
        int spanCount = 3; // 3 columns
        int spacing = 40; // 50px
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
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        filtersDialog = FilterDialog.createInstance();
        filtersDialog.show(ft, "filtersDialog");
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

        if(galleryAdapter.getItemPosition(position).type ==  SelfBoxConstants.TypeContent.VIDEO){
            startActivity(new Intent(getActivity(), VideoActivity.class));
        }else{
            startActivity(new Intent(getActivity(), PdfActivity.class));
        }

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
