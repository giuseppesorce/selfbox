package com.docgenerici.selfbox.android.start;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        presenter= SelfBoxApplicationImpl.appComponent.startPresenter();
        presenter.setView(this);
        presenter.chekActivation();
    }

    @OnClick(R.id.tvSend)
    void onTapSend(){
        String code= etCode.getText().toString();
        presenter.setActivation(code);
    }

    @Override
    public void gotoHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void showActivationInput() {

    }

    @Override
    public void showCodeError(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressToSend() {
        etCode.setVisibility(View.GONE);
        tvSend.setVisibility(View.GONE);
        tvLabel.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }
}
