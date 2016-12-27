package com.docgenerici.selfbox.android.evisual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.custom.buttons.CButton;
import com.docgenerici.selfbox.android.utils.MathUtils;
import com.docgenerici.selfbox.debug.Dbg;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvisualActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.llContainerButton)
    LinearLayout llContainerButton;

    private String pathVisual;
    private ArrayList<File> filesContent= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evisual);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            pathVisual = getIntent().getStringExtra("path");
            Dbg.p("pathVisual: "+pathVisual);
            File file= new File(pathVisual);
            if(file.exists()){
                String fileName= file.getName();
                File folder= new File(file.getAbsolutePath().replace(fileName, "")+ fileName.replace(".zip", ""));
                if(folder.exists() && folder.isDirectory()){
                    Dbg.p("E una directory: folder: "+folder.getAbsolutePath());
                    File[] fileList = folder.listFiles();
                    for (int i = 0; i < fileList.length; i++) {
                       if(fileList[i].getName().equalsIgnoreCase("asset")){
                           checkHtmlInAsset(fileList[i]);
                           break;
                       }

                    }
                }
            }
            Dbg.p("FOLDER: "+filesContent.size());
            if(filesContent.size() > 0){
                createButtons();
                loadContentHtml(filesContent.get(0));
            }

        }
    }

    private void createButtons() {
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(MathUtils.dp2px(getApplicationContext(), 45), MathUtils.dp2px(getApplicationContext(), 40));
        params.leftMargin= MathUtils.dp2px(getApplicationContext(), 15);
        for (int i = 0; i < filesContent.size(); i++) {
            CButton cButton= new CButton(getApplicationContext());
            cButton.setText(""+(i+1));
            cButton.setGravity(Gravity.CENTER);
            cButton.setBackgroundResource(R.color.white);
            cButton.setTextColor(getResources().getColor(R.color.black));
            llContainerButton.addView(cButton, params);
            cButton.setId(i);
            cButton.setOnClickListener(this);

        }
    }

    private void checkHtmlInAsset(File file) {
        if(file.exists()){
            if(file.isDirectory()){
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    Dbg.p("Dentro asset: "+fileList[i].getName());
                    if(fileList[i].getName().equalsIgnoreCase("html")){
                        File html= fileList[i];
                        checkContents(html);
                        break;
                    }
                }
            }
        }
    }

    private void checkContents(File html) {

        if(html.exists() && html.isDirectory()){
            File[] lista = html.listFiles();
            for (int i = 0; i < lista.length; i++) {

                if(getIsVisual(lista[i])){
                    filesContent.add((lista[i]));
                }
            }
        }
    }

    private boolean getIsVisual(File file){
        boolean visual= false;
        for (int i = 1; i <15 ; i++) {
            if(file.getName().equalsIgnoreCase(""+i)){
                visual= true;
                break;
            }

        }
        return  visual;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button){
            String n = ((Button) v).getText().toString();
            Dbg.p("N: "+n);
            loadVistual(n);

        }
    }

    private void loadVistual(String n) {

        for (int i = 0; i < filesContent.size(); i++) {
            if(filesContent.get(i).getName().equalsIgnoreCase(n)){
                loadContentHtml(filesContent.get(i));
                break;
            }
        }
    }

    private void loadContentHtml(File file) {

        if(file.exists() && file.isDirectory()){
            File htmlIndex= new File(file, "index.html");
            if(htmlIndex.exists()){
                loadWebView(htmlIndex.getAbsolutePath());
            }
        }
    }

    private void loadWebView(String absolutePath) {
        Dbg.p("absolutePath: "+absolutePath);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("file://"+absolutePath);

    }
}
