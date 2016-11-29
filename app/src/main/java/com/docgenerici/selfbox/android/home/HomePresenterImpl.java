package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce
 */

public class HomePresenterImpl implements HomePresenter {
    private HomeView view;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof HomeView)) {
            throw new IllegalArgumentException("View must extend HomePresenter.View");
        }
        this.view = (HomeView) view;
    }

    @Override
    public void onSelectISF() {
        view.showISF();
    }

    @Override
    public void onSelectMedico() {
        view.showMedico();
    }

    @Override
    public void onSelectPharma() {
        view.showDialogPharmaSearch();
    }

    @Override
    public void onSelectSync() {
        view.gotoSync();
    }

    @Override
    public void onSelectHelp() {
        view.showHelp();
    }

    @Override
    public void onSelectInfo() {
        view.showInfo();
    }

    @Override
    public ArrayList<FarmaciaDto> getPharmaList() {
        ArrayList<FarmaciaDto> pharmaList= new ArrayList<>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Farmacia> farmacie = realm.where(Farmacia.class).findAll();
        for (int i = 0; i < farmacie.size(); i++) {
            FarmaciaDto farmaciaDto= new FarmaciaDto();
            farmaciaDto.id= farmacie.get(i).id;
            farmaciaDto.fullname= farmacie.get(i).fullname;
            farmaciaDto.type= farmacie.get(i).type;
            pharmaList.add(farmaciaDto);
        }
        return pharmaList;
    }
}
