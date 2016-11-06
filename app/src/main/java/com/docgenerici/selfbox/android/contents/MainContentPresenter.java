package com.docgenerici.selfbox.android.contents;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce
 */

public interface MainContentPresenter extends Presenter {

    void setup();

    float getScale(int width, int width1);

    void onSelectContents();

    void onSelectProducts();

    void onSelectShare();

    void setShareContents(ArrayList<ContentDoc> contentsShared);

    ArrayList<ContentDoc> getContentsShared();

    interface MainContentView extends BaseView {

        void setupView();

        void showContents();

        void showProducts();

        void showShareContents(ArrayList<ContentDoc> contentsShared);
    }
}
