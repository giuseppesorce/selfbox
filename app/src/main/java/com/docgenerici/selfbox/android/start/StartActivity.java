package com.docgenerici.selfbox.android.start;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.home.HomeActivity;

public class StartActivity extends AppCompatActivity implements StartPresenter.StartView {

    private StartPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        presenter= SelfBoxApplicationImpl.appComponent.startPresenter();
        presenter.setView(this);
        presenter.chekActivation();


    }

    @Override
    public void gotoHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
