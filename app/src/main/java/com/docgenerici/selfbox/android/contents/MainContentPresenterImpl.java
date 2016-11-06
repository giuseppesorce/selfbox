package com.docgenerici.selfbox.android.contents;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class MainContentPresenterImpl implements MainContentPresenter {
    private MainContentView view;
    private ArrayList<ContentDoc> contentsShared;

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
        return (float)width1 /(float)(width);
    }

    @Override
    public void onSelectContents() {
        view.showContents();
    }

    @Override
    public void onSelectProducts() {
        view.showProducts();
    }

    @Override
    public void onSelectShare() {
        view.showShareContents(contentsShared);
    }

    @Override
    public void setShareContents(ArrayList<ContentDoc> contentsShared) {
        this.contentsShared= contentsShared;
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        return contentsShared;
    }
}
