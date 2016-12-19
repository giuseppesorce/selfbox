package com.docgenerici.selfbox.android.pdf;

import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.MainContentPresenter;
import com.docgenerici.selfbox.android.home.help.HelpDialogFragment;
import com.docgenerici.selfbox.debug.Dbg;

import java.io.File;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PdfActivity extends AppCompatActivity {


    @BindView(R.id.btShare)
    Button btShare;
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.btHelp)
    Button btHelp;
    @BindView(R.id.progress)
    ProgressBar progress;

    String SAMPLE_FILE = "sample.pdf";
    private String pathPdf;
    private RelativeLayout mainLayout;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private String TAG = "selfbox";
    private SearchTask mSearchTask;
    @BindColor(R.color.green)
    int green;
    private String pathIntent;
    private DownloadManager downloadManager;
    private long downloadReference;
    private String downloadCompleteIntentName = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
    private IntentFilter downloadCompleteIntentFilter = new IntentFilter(downloadCompleteIntentName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);
        //pathPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE;
        mainLayout = (RelativeLayout) findViewById(R.id.pdflayout);
       //  deleteFile();
        MainContentPresenter presenter = SelfBoxApplicationImpl.appComponent.mainContentPresenter();
        changeStatusBar(presenter.getContentDarkColor());
        rlToolbar.setBackgroundColor(presenter.getContentColor());
        btHelp.setBackground(presenter.getBackGroundhelp());
        registerReceiver(downloadCompleteReceiver, downloadCompleteIntentFilter);

        if (getIntent() != null) {
            pathPdf = getIntent().getStringExtra("path");
        }
//
//        if (pathIntent != null && pathIntent.length() > 4) {
//            pathPdf = pathIntent;
//            progress.setVisibility(View.VISIBLE);
//            loadPdf(pathIntent);
//        } else {
//
//            Dbg.p("OPEN DIRETTO");
//            openPdf(pathPdf);
//        }
        openPdf(pathPdf);


    }

    private void openPdf(String pathPdf) {

        Dbg.p("openPdf: " + pathPdf);
        progress.setVisibility(View.GONE);
        core = openFile(Uri.decode(pathPdf));

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e(TAG, "Document Not Opening");
        }
        if (core != null) {
            mDocView = new MuPDFReaderView(this) {
                @Override
                protected void onMoveToChild(int i) {
                    if (core == null)
                        return;
                    super.onMoveToChild(i);
                }

            };

            mDocView.setAdapter(new MuPDFPageAdapter(getBaseContext(), core));
            mainLayout.addView(mDocView);

        }


        mSearchTask = new SearchTask(getBaseContext(), core) {

            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                mDocView.setDisplayedViewIndex(result.pageNumber);
                mDocView.resetupChildren();
            }
        };
    }


    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
            //pathPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE;
            String pathPdfDownload = (getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath() + File.separator + "temp.pdf").replace("files/data/", "files/");

            if (id == downloadReference) {
                Dbg.p("COMPLETE: pathPdfDownload: " + pathPdfDownload);
                openPdf(pathPdfDownload);


            }
        }
    };

    private void loadPdf(String pathIntent) {


        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        Uri Download_Uri = Uri.parse(pathIntent.replace(" ", "%20"));
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(false);
        request.setTitle("My Data Download");
        request.setDescription("Android Data download using DownloadManager.");
        request.setDestinationInExternalFilesDir(this, null, "temp.pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        downloadReference = downloadManager.enqueue(request);

    }


    @OnClick(R.id.ivLogo)
    void onTapBack() {
        finish();
    }


    @OnClick(R.id.btHelp)
    public void showHelp() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        HelpDialogFragment helpDialog = HelpDialogFragment.createInstance();
        helpDialog.show(ft, "helpDialog");
    }


    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        pathPdf = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(getBaseContext(), path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }

    public void onDestroy() {
        if (core != null)
            core.onDestroy();
        core = null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSearchTask != null)
            mSearchTask.stop();
    }

    private void changeStatusBar(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
