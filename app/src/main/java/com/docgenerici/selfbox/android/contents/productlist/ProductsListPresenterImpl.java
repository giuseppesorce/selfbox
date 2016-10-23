package com.docgenerici.selfbox.android.contents.productlist;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce
 */

public class ProductsListPresenterImpl implements ProductsListPresenter {
    private PListView view;

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
}
