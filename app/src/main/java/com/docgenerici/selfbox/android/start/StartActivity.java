package com.docgenerici.selfbox.android.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.home.HomeActivity;
import com.docgenerici.selfbox.android.sync.SyncActivity;
import com.docgenerici.selfbox.models.SyncDataCheck;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class StartActivity extends AppCompatActivity implements StartPresenter.StartView {

    private StartPresenter presenter;


    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvLabel)
    TextView tvLabel;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvError)
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Hawk.init(this).build();
        presenter = SelfBoxApplicationImpl.appComponent.startPresenter();
        presenter.setView(this);
        presenter.chekActivation();

    }

    @OnClick(R.id.tvSend)
    void onTapSend() {
        String code = etCode.getText().toString();
        tvError.setText("");
        presenter.setActivation(code);
    }

    @Override
    public void gotoHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showActivationInput() {
        etCode.setVisibility(View.VISIBLE);
        tvSend.setVisibility(View.VISIBLE);
        tvLabel.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showCodeError(String error) {
        tvError.setText(error);
    }

    @Override
    public void showProgressToSend() {
        etCode.setVisibility(View.GONE);
        tvSend.setVisibility(View.GONE);
        tvLabel.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void gotoSyncActivity() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        SyncDataCheck syncData = realm.where(SyncDataCheck.class).findFirst();

        realm.beginTransaction();
        if(syncData ==null){
            syncData= new SyncDataCheck();
            syncData.fromhome= false;
        }
        realm.copyToRealmOrUpdate(syncData);
        realm.commitTransaction();

        if(Hawk.isBuilt()) {


            startActivity(new Intent(this, SyncActivity.class));
            finish();
        }else{
            Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(StartActivity.this, SyncActivity.class));
                    finish();
                }
            }, 700);
        }
    }
}
