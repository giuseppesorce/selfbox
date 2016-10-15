package com.docgenerici.selfbox.android.home;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce
 */

public interface HomePresenter extends Presenter {

    void onSelectISF();

    void onSelectMedico();

    void onSelectPharma();

    void onSelectSync();

    void onSelectHelp();

    void onSelectInfo();

    interface HomeView extends BaseView {

        void showISF();

        void showMedico();

        void showPharma();
    }
}
