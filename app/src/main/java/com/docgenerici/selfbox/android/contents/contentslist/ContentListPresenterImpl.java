package com.docgenerici.selfbox.android.contents.contentslist;

import com.docgenerici.selfbox.BaseView;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class ContentListPresenterImpl implements ContentListPresenter {
    private ContentView view;
    private final int FILTER_BY_DATE= 1;
    private final int FILTER_BY_AZ= 2;
    private  int FILTER_NOW= FILTER_BY_AZ;
    @Override
    public void setView(BaseView view) {
        if (!(view instanceof ContentView)) {
            throw new IllegalArgumentException("View must extend ContentListPresenter.View");
        }
        this.view = (ContentView) view;
    }

    @Override
    public void selectAZ() {
        FILTER_NOW= FILTER_BY_AZ;
        view.showSelectAz();
    }

    @Override
    public void selectDate() {
        FILTER_NOW= FILTER_BY_DATE;
        view.showSelectDate();
    }

    @Override
    public void onSelecteFilter() {
        view.openFilterDialog();
    }
}
