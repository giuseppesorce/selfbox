package com.docgenerici.selfbox.android.contents.filters;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.docgenerici.selfbox.R;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce #@copyright xx 17/10/16.
 */

public class FilterDialog extends DialogFragment {


    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;
    @BindView(R.id.tvFiltri)
    TextView tvFiltri;
    @BindView(R.id.tvAvvisi)
    TextView tvAvvisi;
    @BindView(R.id.tvDocumenti)
    TextView tvDocumenti;
    @BindView(R.id.tvVisual)
    TextView tvVisual;
    @BindView(R.id.tvVideo)
    TextView tvVideo;
    @BindView(R.id.tvVideoIntervesta)
    TextView tvVideoIntervesta;
    private boolean filterVideo = false;
    private boolean filterAvvisi = false;
    private boolean filterVideoIntervista = false;
    private boolean filterDocumenti = false;
    private boolean filterVisual = false;
    @BindDrawable(R.drawable.ic_check_check)
    Drawable dwCheck;
    @BindDrawable(R.drawable.ic_check_uncheck)
    Drawable dwUnCheck;

    public static FilterDialog createInstance() {
        FilterDialog frag = new FilterDialog();
        Bundle init = new Bundle();
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
        View root = inflater.inflate(R.layout.dialog_filters, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);
        return root;
    }

    @OnClick(R.id.tvFiltri)
    void onSelectFiltri() {
        dismiss();
    }

    @OnClick(R.id.rlRoot)
    void onSelectRoot() {
        dismiss();
    }

    @OnClick(R.id.tvAvvisi)
    void onSelectAvvisi() {
        filterAvvisi = !filterAvvisi;
        if (filterAvvisi) {
            tvAvvisi.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
        } else {
            tvAvvisi.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
        }
    }

    @OnClick(R.id.tvDocumenti)
    void onSelectDocumenti() {
        filterDocumenti = !filterDocumenti;
        if (filterDocumenti) {
            tvDocumenti.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
        } else {
            tvDocumenti.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
        }

    }

    @OnClick(R.id.tvVideo)
    void onSelectVideo() {
        filterVideo = !filterVideo;
        if (filterVideo) {
            tvVideo.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
        } else {
            tvVideo.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
        }

    }

    @OnClick(R.id.tvVisual)
    void onSelectVisual() {
        filterVisual = !filterVisual;
        if (filterVisual) {
            tvVisual.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
        } else {
            tvVisual.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
        }

    }

    @OnClick(R.id.tvVideoIntervesta)
    void onSelectVideoIntervista() {
        filterVideoIntervista = !filterVideoIntervista;
        if (filterVideoIntervista) {
            tvVideoIntervesta.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
        } else {
            tvVideoIntervesta.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
        }

    }
}