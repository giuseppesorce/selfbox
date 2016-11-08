package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.models.PharmaUser;

import java.util.ArrayList;

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
    public ArrayList<PharmaUser> getPharmaList() {
        ArrayList<PharmaUser> pharmaList= new ArrayList<>();
        PharmaUser pharmaUser= new PharmaUser();
        PharmaUser pharmaUser2= new PharmaUser();
        PharmaUser pharmaUser3= new PharmaUser();
        PharmaUser pharmaUser4= new PharmaUser();
        PharmaUser pharmaUser5= new PharmaUser();
        pharmaUser.name="FARMATEST1 FARMAtest1";
        pharmaUser2.name="D FARMATEST2 ";
        pharmaUser3.name="A FARMATEST2 ";
        pharmaUser4.name="C FARMATEST2 ";
        pharmaUser5.name="Demo FARMATEST2 ";

        pharmaList.add(pharmaUser);
        pharmaList.add(pharmaUser2);
        pharmaList.add(pharmaUser3);
        pharmaList.add(pharmaUser4);
        pharmaList.add(pharmaUser5);
        return pharmaList;
    }
}
