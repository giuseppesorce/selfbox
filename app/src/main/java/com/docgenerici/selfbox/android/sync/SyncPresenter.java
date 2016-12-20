package com.docgenerici.selfbox.android.sync;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;
import com.docgenerici.selfbox.models.SyncContent;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public interface SyncPresenter extends Presenter {

    void setup();

    void startSync();

    void stopSync();

    void onSyncMessage(int type, int percentage, String message);

    ArrayList<SyncContent> getContents();

    interface SyncView extends BaseView {

        void setup();

        void onStartSync();

        void onStopSync();

        void startProductService();

        void updatePercentage();

        void showCodeError(String error);

        void startContentsService();

        void gotoHome();

        void loadPricelist();
    }
}
