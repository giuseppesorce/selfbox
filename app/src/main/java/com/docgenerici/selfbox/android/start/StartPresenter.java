package com.docgenerici.selfbox.android.start;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce
 */

public interface StartPresenter extends Presenter {


    void chekActivation();

    interface StartView extends BaseView {

        void gotoHome();
    }
}
