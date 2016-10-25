package com.docgenerici.selfbox.android.sync;

import android.os.CountDownTimer;
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
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;
import com.docgenerici.selfbox.models.SyncContent;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SyncActivity extends AppCompatActivity implements SyncPresenter.SyncView{

    @BindView(R.id.rvGrid)
    RecyclerView rvGrid;
    private SyncPresenter presenter;
    private GridSyncAdapter gridSyncAdapter;
    private CountDownTimer timer;
    private ArrayList<SyncContent> syncContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.syncPresenter();
        presenter.setView(this);
        presenter.setup();
        timer = new CountDownTimer(30000, 500) {

            public void onTick(long millisUntilFinished) {

                for (int i = 0; i < syncContents.size(); i++) {
                    SyncContent syncContent = syncContents.get(i);
                    syncContent.setPercentage(syncContent.getPercentage() + getRandom(1, 7));
                    Dbg.p("syncContent: " + syncContent.getPercentage());
                    if (syncContent.getPercentage() > 100) {
                        syncContent.setPercentage(100);
                    }


                }
                gridSyncAdapter.notifyDataSetChanged();
                boolean allThousand = true;
                for (int i = 0; i < syncContents.size(); i++) {
                    SyncContent syncContent = syncContents.get(i);
                    if (syncContent.getPercentage() < 100) {
                        allThousand = false;
                    }


                }
                if (allThousand) {
                    timer.cancel();
                }

            }

            public void onFinish() {

            }
        };


    }

    @OnClick(R.id.btCancel)
    void stopFakeSync(){
        timer.cancel();

        for (int i = 0; i < syncContents.size(); i++) {
            SyncContent syncContent = syncContents.get(i);
            syncContent.setPercentage(0);
        }
        gridSyncAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btSend)
    void startFakeSync() {

        timer.start();

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

    private int getRandom(int min, int max) {

        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }


}
