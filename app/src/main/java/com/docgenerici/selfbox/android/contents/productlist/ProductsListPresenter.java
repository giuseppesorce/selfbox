package com.docgenerici.selfbox.android.contents.productlist;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public interface ProductsListPresenter extends Presenter {

    void setup();

    void onSelectFilter();

    void onSelectLegenda();

    void selectAZ();

    void selectTerapeutica();

    ArrayList<String> getCategoriesFilter();

    void selectLastFilter();

    interface PListView extends BaseView {

        void setup();

        void openFilterDialog();

        void showLegenda();

        void showSelectAz();

        void showSelectTerapeutica();
    }
}
