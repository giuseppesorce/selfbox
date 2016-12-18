package com.docgenerici.selfbox.android.sync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.GridSyncAdapter;
import com.docgenerici.selfbox.models.SyncContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SyncActivity extends AppCompatActivity implements SyncPresenter.SyncView{

    @BindView(R.id.rvGrid)
    RecyclerView rvGrid;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvSend)
    TextView tvSend;
    private SyncPresenter presenter;
    private GridSyncAdapter gridSyncAdapter;
    private ArrayList<SyncContent> syncContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.syncPresenter();
        presenter.setView(this);
        presenter.setup();
    }

    @OnClick(R.id.btCancel)
    void stopSync(){
        presenter.stopSync();
    }

    @OnClick(R.id.tvSend)
    void startSync() {
        presenter.startSync();
    }

    @OnClick(R.id.ivLogo)
    void onTapLogo() {
        finish();
    }

    @Override
    public void setup() {


        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        syncContents = new ArrayList<>();
        syncContents.add(new SyncContent("Contenuti", 0));
        syncContents.add(new SyncContent("Briefcase", 0));
        syncContents.add(new SyncContent("Anagrafiche", 0));
        syncContents.add(new SyncContent("Logs", 0));
        syncContents.add(new SyncContent("Informazioni personali", 0));
        syncContents.add(new SyncContent("Catalogo prodotti", 0));
        gridSyncAdapter = new GridSyncAdapter(this, syncContents);
        int spanCount = 2; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        rvGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvGrid.setLayoutManager(gridLayout);
        rvGrid.setAdapter(gridSyncAdapter);

    }

    @Override
    public void onStartSync() {
        progress.setVisibility(View.VISIBLE);
        tvSend.setEnabled(false);
        tvSend.setAlpha(0.6f);
    }

    @Override
    public void onStopSync() {
        progress.setVisibility(View.GONE);
        tvSend.setEnabled(true);
        tvSend.setAlpha(1.0f);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
