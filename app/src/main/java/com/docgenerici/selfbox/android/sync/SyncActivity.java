package com.docgenerici.selfbox.android.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.GridSpacingItemDecoration;
import com.docgenerici.selfbox.android.adapters.GridSyncAdapter;
import com.docgenerici.selfbox.android.downloader.DownloaderDoc;
import com.docgenerici.selfbox.android.downloader.ListenerDowloadDoc;
import com.docgenerici.selfbox.android.home.HomeActivity;
import com.docgenerici.selfbox.android.synservices.ContentsService;
import com.docgenerici.selfbox.android.synservices.ProductSyncService;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.SyncContent;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    private static final String[] PERMISSIONS = new String[]{
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,
            INTERNET
    };
    private Intent intentContent;
    private Intent intentProduct;
    private DownloaderDoc downloaderPrice;

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
        if(Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }else{
            createGridSync();
        }
    }

    private void checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                return;
            }else{
                createGridSync();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!areAllPermissionsGranted(requestCode, grantResults)) {
            Toast.makeText(this, "Devi accettare tutti i permessi per poter usare l' applicazione", Toast.LENGTH_SHORT).show();
        }else{
            createGridSync();
        }
    }

private void createGridSync(){
    GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
    syncContents= presenter.getContents();
    gridSyncAdapter = new GridSyncAdapter(this, syncContents);
    int spanCount = 2; // 3 columns
    int spacing = 50; // 50px
    boolean includeEdge = false;
    rvGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    rvGrid.setLayoutManager(gridLayout);
    rvGrid.setAdapter(gridSyncAdapter);
}

    private boolean areAllPermissionsGranted(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

        if(intentProduct !=null){
            stopService(intentProduct);
            Dbg.p("stopService product");
        }  if(intentContent !=null){
            stopService(intentContent);
            Dbg.p("stopService intentContent");
        }
    }

    @Override
    public void startProductService() {
       intentProduct= new Intent(this, ProductSyncService.class);
        startService(intentProduct);
    }

    @Override
    public void updatePercentage() {
        gridSyncAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCodeError(String error) {

    }

    @Override
    public void startContentsService() {
        intentContent= new Intent(this, ContentsService.class);
        startService(intentContent);
    }

    @Override
    public void gotoHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void loadPricelist() {
        downloaderPrice = DownloaderDoc.newInstance(new ListenerDowloadDoc() {
            @Override
            public void fileDownloaded(Uri uri, String mimeType, int id) {
                Dbg.p("LISTINO CARICATO: "+uri.toString());
            }

            @Override
            public void downloadError(String error, int errortype, int id) {
                Dbg.p("LISTINO ERRORE");
            }

            @Override
            public Context getContext() {
                return getApplicationContext();
            }
        });
        File file= new File(getExternalFilesDir("contents"),  "listinoprezzi.pdf");
        File backup= new File(getExternalFilesDir("contents"),  "delete_listinoprezzi.pdf");
        if(file.exists()){
            file.renameTo(backup);
        }
        Uri uri= Uri.parse("http://www.docgenerici.it/xpdf/examples/listino_prezzi.php");
        downloaderPrice.download(uri, "contents", "listinoprezzi.pdf", 12234);

       // downloaderCover.download(uriContentCover, "contents", filenameContentCover, contentEasy.id);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(MyReceiver, new IntentFilter("sync"));
        presenter.checkSyncData();


    }

    //Defining broadcast receiver
    private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            int type = intent.getIntExtra("type", 0);
            int percentage = intent.getIntExtra("percentage", 0);
            presenter.onSyncMessage(type, percentage, message);

        }
    };

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(MyReceiver);
        super.onStop();

    }



}
