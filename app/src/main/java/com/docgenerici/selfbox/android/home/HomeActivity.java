package com.docgenerici.selfbox.android.home;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.contents.ContentsActivity;
import com.docgenerici.selfbox.android.home.help.HelpDialogFragment;
import com.docgenerici.selfbox.android.home.info.InfoDialogFragment;
import com.docgenerici.selfbox.android.home.medical.MedicalDialogFragment;
import com.docgenerici.selfbox.android.home.pharma.PharmaDialogFragment;
import com.docgenerici.selfbox.android.sync.SyncActivity;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.products.Product;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements HomePresenter.HomeView, HomeActivityInterface {


    private static final int WRITE_PERMISSION = 124;
    private HomePresenter presenter;
    @BindColor(R.color.grey_filter)
    int grey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        presenter = SelfBoxApplicationImpl.appComponent.homePresenter();
        presenter.setView(this);
        changeStatusBar(grey);
        checkWritePermission();

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
        Intent intent = new Intent(this, ContentsActivity.class);
        intent.putExtra("category", "isf");
        startActivity(intent);
    }

    @Override
    public void showMedico(MedicoDto lastMedicoUser) {
        Intent intent = new Intent(this, ContentsActivity.class);
        intent.putExtra("category", "medico");
        intent.putExtra("medico", "lastMedicoUser");
        startActivity(intent);
    }

    @Override
    public void showDialogPharmaSearch() {
        ArrayList<FarmaciaDto> pharmaList = presenter.getPharmaList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        PharmaDialogFragment pharmaDialog = PharmaDialogFragment.createInstance(pharmaList);
        pharmaDialog.show(ft, "pharmaDialog");
    }

    @Override
    public void showHelp() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        HelpDialogFragment helpDialog = HelpDialogFragment.createInstance();
        helpDialog.show(ft, "helpDialog");
    }

    @Override
    public void gotoSync() {
        startActivity(new Intent(this, SyncActivity.class));
    }

    @Override
    public void showInfo() {

        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        InfoDialogFragment infoDialogFragment = InfoDialogFragment.createInstance();
        infoDialogFragment.show(ft, "infoDialogFragment");
    }

    @Override
    public void showDialogMedicalSearch() {
        ArrayList<MedicoDto> presenterMedicalList = presenter.getMedicalList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MedicalDialogFragment pharmaDialog = MedicalDialogFragment.createInstance(presenterMedicalList);
        pharmaDialog.show(ft, "medical");

    }

    private void changeStatusBar(int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void checkWritePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION);
            }
        }
    }

    @Override
    public void onSelectPharmaUser(FarmaciaDto lastPharmaUser) {
        Intent intent = new Intent(this, ContentsActivity.class);
        intent.putExtra("category", "pharma");
        startActivity(intent);
    }

    @Override
    public void onSelectMedicoUser(MedicoDto lastMedicoUser) {
        presenter.onSelectMedicoUser(lastMedicoUser);
    }

    @Override
    public void onSelectTrainingFamarcia(FarmaciaDto lastPharmaUser) {
        Intent intent = new Intent(this, ContentsActivity.class);
        intent.putExtra("category", "pharma");
        startActivity(intent);
    }

    @Override
    public void onSelectTrainingMedico(MedicoDto lastMedicoUser) {

    }
}
