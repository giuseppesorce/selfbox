package com.docgenerici.selfbox.android.home.info;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.docgenerici.selfbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce
 * */

public class InfoDialogFragment extends DialogFragment {


    public static InfoDialogFragment createInstance() {
        InfoDialogFragment frag = new InfoDialogFragment();
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
        View root = inflater.inflate(R.layout.dialog_info, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);

       return root;
    }

    @OnClick(R.id.btClose)
    void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.rlRoot)
    void closeHelpDialog(){
        dismiss();
    }
}
