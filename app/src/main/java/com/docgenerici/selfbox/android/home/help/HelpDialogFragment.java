package com.docgenerici.selfbox.android.home.help;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.docgenerici.selfbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class HelpDialogFragment extends DialogFragment {



    @BindView(R.id.webView)
    WebView webView;

    public static HelpDialogFragment createInstance() {
        HelpDialogFragment frag = new HelpDialogFragment();
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
        View root = inflater.inflate(R.layout.dialog_help, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);

        webView.loadUrl("file:///android_asset/help.html");


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
