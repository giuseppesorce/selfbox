package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GalleryAdapter;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.OnItemContentClickListener;
import com.docgenerici.selfbox.android.contents.ContentActivityInterface;
import com.docgenerici.selfbox.android.contents.filters.FilterDialog;
import com.docgenerici.selfbox.android.pdf.PdfActivity;
import com.docgenerici.selfbox.android.video.VideoActivity;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
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
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindString(R.string.contenuti)
    String strContenuti;
    private ContentListPresenter presenter;
    private GalleryAdapter galleryAdapter;
    private FilterDialog filtersDialog;
    private ContentActivityInterface activityInterface;
    private String categoryContent;
    private ContentDoc contentSelect;
    private GridLayoutManager gridLayout;
    private int spanCount = 3; // 3 columns
    private int spacing = 40; // 50px
    private boolean includeEdge = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_contents_list, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            categoryContent = getArguments().getString("category");
        }
        presenter = SelfBoxApplicationImpl.appComponent.contentListPresenter();
        presenter.setView(this);
        presenter.setup(R.drawable.foldersample);

        presenter.selectAZ();
        etSearch.clearFocus();
        String category = SelfBoxApplicationImpl.appComponent.mainContentPresenter().getCategory();
        Resources res = getResources();
        switch ((category)) {

            case "isf":
                btFilter.setBackgroundResource(R.drawable.ic_filter_orange);
                Drawable ic_search_orange = res.getDrawable(R.drawable.ic_search_orange);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_orange, null);
                break;
            case "medico":
                Drawable ic_search_blue = res.getDrawable(R.drawable.ic_search_blu);
                btFilter.setBackgroundResource(R.drawable.ic_filter_blu);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_blue, null);

                break;
            case "pharma":
                Drawable ic_search_green = res.getDrawable(R.drawable.ic_search);
                btFilter.setBackgroundResource(R.drawable.ic_filter_green);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_green, null);
                break;
        }

        return view;
    }

    private void createGalleryContentsItems() {
        tvTitle.setText(strContenuti);
        rvGallery.setLayoutManager(gridLayout);
        presenter.setLevelView(0);
        galleryAdapter = new GalleryAdapter(getActivity(), presenter.getContents(categoryContent), this);
        rvGallery.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvGallery.setAdapter(galleryAdapter);
    }

    private void createGalleryContentsItemsFromFolder(int id) {
        presenter.setLevelView(1);
        rvGallery.setLayoutManager(gridLayout);
        galleryAdapter = new GalleryAdapter(getActivity(), presenter.getContentsByFolder(id), this);
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
        if (activityInterface != null) {
            activityInterface.setShared(presenter.getContentsShared());
        }
    }

    @Override
    public void setup() {
        gridLayout = new GridLayoutManager(getActivity(), 3);
        createGalleryContentsItems();
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

        contentSelect = galleryAdapter.getItemPosition(position);
        if (contentSelect.type == SelfBoxConstants.TypeContent.FOLDER) {
            tvTitle.setText(strContenuti + " / " + contentSelect.name);
            createGalleryContentsItemsFromFolder(contentSelect.id);
        } else {
            if (contentSelect.type == SelfBoxConstants.TypeContent.VIDEO) {
               Intent intentVideo= new Intent(getActivity(), VideoActivity.class);
                intentVideo.putExtra("path", contentSelect.content);
                startActivity(intentVideo);
            } else {
                Intent intent = new Intent(getActivity(), PdfActivity.class);
                intent.putExtra("path", contentSelect.content);
                if (contentSelect.content != null) {
                    startActivity(intent);
                }
            }
        }

    }

    public static ContentsListFragment createInstance(String category) {
        ContentsListFragment frag = new ContentsListFragment();
        Bundle init = new Bundle();
        init.putString("category", category);
        frag.setArguments(init);
        return frag;
    }

    @Override
    public void onSelectShare(int position) {
        ContentDoc contentSelectShare = galleryAdapter.getItemPosition(position);
        presenter.setShare(contentSelectShare);
    }

    public boolean canBack() {
        if (presenter.getLevelView() == 0) {
            return true;
        } else if (presenter.getLevelView() == 1) {
            createGalleryContentsItems();
            return false;
        }
        return true;

    }
}
