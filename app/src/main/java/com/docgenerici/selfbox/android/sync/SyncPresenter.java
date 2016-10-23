package com.docgenerici.selfbox.android.sync;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public interface SyncPresenter extends Presenter {

    void setup();

    interface SyncView extends BaseView {

        void setup();
    }
}
