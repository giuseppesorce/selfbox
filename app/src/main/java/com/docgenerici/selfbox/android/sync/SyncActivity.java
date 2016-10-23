package com.docgenerici.selfbox.android.sync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GalleryAdapter;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.GridSyncAdapter;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;
import com.docgenerici.selfbox.models.SyncContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncActivity extends AppCompatActivity implements SyncPresenter.SyncView{

    @BindView(R.id.rvGrid)
    RecyclerView rvGrid;
    private SyncPresenter presenter;
    private GridSyncAdapter gridSyncAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);
        presenter= SelfBoxApplicationImpl.appComponent.syncPresenter();
        presenter.setView(this);
        presenter.setup();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//

//        rvGallery.setLayoutManager(gridLayout);
//        ArrayList<ContentDoc> contents= new ArrayList<>();
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Listino prezzi 05/10/2016", sample1));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Listino medico settembre 2015",sample2));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.FOLDER, "Programmi Eventi 2016", sample1));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.FOLDER, "Congressi 2016",sample2));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.VISUAL, "Presentazione nuovi prodotti", sample3));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Brochure OMEGA 3", sample2));
//        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Brochure 2016", sample1));
//        galleryAdapter = new GalleryAdapter(getActivity(), contents, this);
//        int spanCount = 3; // 3 columns
//        int spacing = 50; // 50px
//        boolean includeEdge = false;
//        rvGallery.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//        rvGallery.setAdapter(galleryAdapter);
    }

    @Override
    public void setup() {
        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        ArrayList<SyncContent> syncContents= new ArrayList<>();
        syncContents.add(new SyncContent("Contenuti", 0.10f));
        syncContents.add(new SyncContent("Briefcase", 0.20f));
        syncContents.add(new SyncContent("Anagrafiche", 0.05f));
        syncContents.add(new SyncContent("Logs", 0.0f));
        syncContents.add(new SyncContent("Informazioni personali", 0.50f));
        syncContents.add(new SyncContent("Catalogo prodotti", 0.56f));
        gridSyncAdapter = new GridSyncAdapter(this, syncContents);

        int spanCount = 2; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        rvGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvGrid.setLayoutManager(gridLayout);
        rvGrid.setAdapter(gridSyncAdapter);
    }
}
