package com.docgenerici.selfbox.android.start;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce
 */

public interface StartPresenter extends Presenter {


    void chekActivation();

    void setActivation(String code);

    interface StartView extends BaseView {

        void gotoHome();

        void showActivationInput();

        void showCodeError(String s);

        void showProgressToSend();

        void gotoSyncActivity();
    }
}
