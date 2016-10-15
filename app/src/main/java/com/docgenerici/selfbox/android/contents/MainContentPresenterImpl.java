package com.docgenerici.selfbox.android.contents;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class MainContentPresenterImpl implements MainContentPresenter {
    private MainContentView view;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof MainContentView)) {
            throw new IllegalArgumentException("View must extend MainContentPresenter.View");
        }
        this.view = (MainContentView) view;
    }

    @Override
    public void setup() {
        view.setupView();
    }

    @Override
    public float getScale(int width, int width1) {
        return (float)(float)width1/(float)(width);
    }

    @Override
    public void onSelectContents() {
        view.showContents();
    }

    @Override
    public void onSelectProducts() {
        view.showProducts();
    }
}
