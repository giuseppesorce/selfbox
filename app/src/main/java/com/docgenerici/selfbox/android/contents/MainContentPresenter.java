package com.docgenerici.selfbox.android.contents;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce
 */

public interface MainContentPresenter extends Presenter {

    void setup();

    float getScale(int width, int width1);

    void onSelectContents();

    void onSelectProducts();

    interface MainContentView extends BaseView {

        void setupView();

        void showContents();

        void showProducts();
    }
}
