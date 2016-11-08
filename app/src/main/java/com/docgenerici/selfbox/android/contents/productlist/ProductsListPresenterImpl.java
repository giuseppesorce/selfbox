package com.docgenerici.selfbox.android.contents.productlist;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce
 */

public class ProductsListPresenterImpl implements ProductsListPresenter {
    private PListView view;
    private final int FILTER_BY_DATE= 1;
    private final int FILTER_BY_AZ= 2;
    private  int FILTER_NOW= FILTER_BY_AZ;
    @Override
    public void setView(BaseView view) {
        if (!(view instanceof PListView)) {
            throw new IllegalArgumentException("View must extend ProductsListPresenter.View");
        }
        this.view = (PListView) view;
    }

    @Override
    public void setup() {
        view.setup();
    }

    @Override
    public void onSelectFilter() {
        view.openFilterDialog();
    }

    @Override
    public void onSelectLegenda() {
        view.showLegenda();
    }

    @Override
    public void selectAZ() {
        FILTER_NOW= FILTER_BY_AZ;
        view.showSelectAz();
    }

    @Override
    public void selectDate() {
        FILTER_NOW= FILTER_BY_DATE;
        view.showSelectDate();
    }
}
