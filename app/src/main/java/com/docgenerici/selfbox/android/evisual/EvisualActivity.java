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
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.custom.buttons.CButton;
import com.docgenerici.selfbox.android.utils.MathUtils;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.persistence.ItemShared;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class EvisualActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.llContainerButton)
    LinearLayout llContainerButton;
    @BindView(R.id.btShare)
    Button btShare;
    @BindView(R.id.btHelp)
    Button btHelp;
    private String pathVisual;
    private ArrayList<File> filesContent= new ArrayList<>();
    private Button buttonCliked;
    private boolean canShare;
    private String typeContent;
    private ContentDoc contentDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evisual);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            pathVisual = getIntent().getStringExtra("path");
            canShare = getIntent().getBooleanExtra("canShare", false);
            typeContent = getIntent().getStringExtra("type");
            contentDoc = (ContentDoc) getIntent().getParcelableExtra("contentSelect");
            setupShare();
            Dbg.p("pathVisual: "+pathVisual);
            if(pathVisual.startsWith("file://")){
                pathVisual= pathVisual.replace("file://", "");
            }
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
            if(i==0){
                cButton.setAlpha(0.5f);
                buttonCliked= cButton;
            }

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
            if(buttonCliked !=null){
                buttonCliked.setAlpha(1.0f);
            }
            String n = ((Button) v).getText().toString();
                    buttonCliked= (Button)v;
            buttonCliked.setAlpha(0.5f);
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


    private void setupShare() {
        if(contentDoc !=null){
            String id= String.valueOf(contentDoc.id);
            Realm realm= SelfBoxApplicationImpl.appComponent.realm();
            ItemShared itemShared = realm.where(ItemShared.class).equalTo("id", id).findFirst();
            if(itemShared !=null){
                btShare.setBackgroundResource(R.drawable.ic_share_red);
            }else{
                btShare.setBackgroundResource(R.drawable.ic_share_white);

            }
        }
    }

    @OnClick(R.id.btShare)
    void addOrDeleteShare(){
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        if(contentDoc !=null) {
            String id = String.valueOf(contentDoc.id);

            final ItemShared sharedItem = realm.where(ItemShared.class).equalTo("id", id).findFirst();
            if (sharedItem != null) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        sharedItem.deleteFromRealm();
                    }
                });
            } else {
                try {

                    realm.beginTransaction();

                    ItemShared newSharedItem = new ItemShared();
                    newSharedItem.setId(id);
                    newSharedItem.setName(contentDoc.name);
                    newSharedItem.setType("content");
                    realm.copyToRealmOrUpdate(newSharedItem);
                } catch (Exception ex) {

                } finally {
                    realm.commitTransaction();
                }
            }
        }
        setupShare();

    }
}
