package com.docgenerici.selfbox.android.contents.productlist;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public interface ProductsListPresenter extends Presenter {

    void setup();

    void onSelectFilter();

    void onSelectLegenda();

    void selectAZ();

    void selectDate();

    interface PListView extends BaseView {

        void setup();

        void openFilterDialog();

        void showLegenda();

        void showSelectAz();

        void showSelectDate();
    }
}
