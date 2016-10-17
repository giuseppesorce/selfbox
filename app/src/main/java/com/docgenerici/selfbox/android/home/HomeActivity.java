package com.docgenerici.selfbox.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.ContentsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements HomePresenter.HomeView {


    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.homePresenter();
        presenter.setView(this);
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);


    }

    @OnClick(R.id.rlISF)
    void onTapISF() {
        presenter.onSelectISF();
    }

    @OnClick(R.id.rlMedico)
    void onTapMedico() {
        presenter.onSelectMedico();
    }

    @OnClick(R.id.rlPharma)
    void onTapPharma() {
        presenter.onSelectPharma();
    }

    @OnClick(R.id.btSync)
    void onTapSync() {
        presenter.onSelectSync();
    }

    @OnClick(R.id.btHelp)
    void onTapHelp() {
        presenter.onSelectHelp();
    }

    @OnClick(R.id.btInfo)
    void onTapInfo() {
        presenter.onSelectInfo();
    }

    @Override
    public void showISF() {
        startActivity(new Intent(this, ContentsActivity.class));
    }

    @Override
    public void showMedico() {
        startActivity(new Intent(this, ContentsActivity.class));
    }

    @Override
    public void showPharma() {
        startActivity(new Intent(this, ContentsActivity.class));
    }
}
