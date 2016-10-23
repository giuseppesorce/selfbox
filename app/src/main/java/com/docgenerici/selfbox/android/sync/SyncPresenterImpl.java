package com.docgenerici.selfbox.android.sync;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class SyncPresenterImpl implements SyncPresenter {
    private SyncView view;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof SyncView)) {
            throw new IllegalArgumentException("View must extend SyncPresenter.View");
        }
        this.view = (SyncView) view;
    }

    @Override
    public void setup() {
        view.setup();
    }
}
