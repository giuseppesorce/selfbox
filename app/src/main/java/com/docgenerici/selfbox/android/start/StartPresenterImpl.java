package com.docgenerici.selfbox.android.start;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class StartPresenterImpl implements StartPresenter {
    private StartView view;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof StartView)) {
            throw new IllegalArgumentException("View must extend StartPresenter.View");
        }
        this.view = (StartView) view;
    }

    @Override
    public void chekActivation() {
        //TODO this method checks if there is a previous activation
        if(hereActivation()){
            view.gotoHome();
        }else{

        }
    }

    private boolean hereActivation() {

        return true;
    }
}
