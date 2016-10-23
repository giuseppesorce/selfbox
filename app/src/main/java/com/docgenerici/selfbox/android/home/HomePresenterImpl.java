package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.BaseView;

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
        view.showPharma();
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

    }
}
