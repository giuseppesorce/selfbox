package com.docgenerici.selfbox.android.pdf;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.SearchTask;
import com.artifex.mupdfdemo.SearchTaskResult;
import com.docgenerici.selfbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class PdfFragment extends DialogFragment {


    @BindView(R.id.fmPdf)
    RelativeLayout fmPdf;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private String mFilePath;
    private SearchTask mSearchTask;

    public static PdfFragment createInstance(String mFilePath) {
        PdfFragment frag = new PdfFragment();
        Bundle init = new Bundle();
        init.putString("mFilePath", mFilePath);
        frag.setArguments(init);
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_pdf, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);

        if (getArguments() != null) {
            mFilePath = getArguments().getString("mFilePath");
        }
        Log.i("selfbox", "mFilePath: "+mFilePath);

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e("selfbox", "Document Not Opening");
        }
        if (core != null) {
            mDocView = new MuPDFReaderView(getActivity()) {
                @Override
                protected void onMoveToChild(int i) {
                    if (core == null)
                        return;
                    super.onMoveToChild(i);
                }

            };

            mDocView.setAdapter(new MuPDFPageAdapter(getActivity().getApplicationContext(), core));
            fmPdf.addView(mDocView);
        }
        mSearchTask = new SearchTask(getActivity().getApplicationContext(), core) {

            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                mDocView.setDisplayedViewIndex(result.pageNumber);
                mDocView.resetupChildren();
            }
        };

        return root;
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(getActivity().getApplicationContext(), path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("selfbox", e.getMessage());
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

//    @OnClick(R.id.btClose)
//    void closeDialog(){
//        dismiss();
//    }
//
//    @OnClick(R.id.rlRoot)
//    void closeHelpDialog(){
//        dismiss();
//    }
}
