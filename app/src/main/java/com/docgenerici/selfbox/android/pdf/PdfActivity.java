package com.docgenerici.selfbox.android.pdf;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.docgenerici.selfbox.R;

import java.io.File;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.path;

public class PdfActivity extends AppCompatActivity {


    @BindView(R.id.btBack)
    Button btBack;
    private static final String SAMPLE_FILE = "sample.pdf";
    private String pathPdf;
    private RelativeLayout mainLayout;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private String TAG = "selfbox";
    private SearchTask mSearchTask;
    @BindColor(R.color.green)
    int green;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);
        pathPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE;
        mainLayout = (RelativeLayout) findViewById(R.id.pdflayout);
        changeStatusBar(green);

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

    @OnClick(R.id.btBack)
    void onTapBack(){
        finish();
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
