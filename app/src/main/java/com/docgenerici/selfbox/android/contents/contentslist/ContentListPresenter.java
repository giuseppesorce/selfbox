package com.docgenerici.selfbox.android.contents.contentslist;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce
 */

public interface ContentListPresenter extends Presenter {

    void selectAZ();

    void selectDate();

    interface ContentView extends BaseView {

        void showSelectAz();

        void showSelectDate();
    }
}
