package com.docgenerici.selfbox.android.contents.contentslist;


import android.graphics.drawable.Drawable;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Sorce
 */

public interface ContentListPresenter extends Presenter {

    void selectAZ();

    void selectDate();

    void onSelecteFilter();

    void setup(Drawable sample1, Drawable sample2, Drawable sample3);

    List<ContentDoc> getContents();

    void setShare(int position);

    ArrayList<ContentDoc> getContentsShared();

    interface ContentView extends BaseView {

        void showSelectAz();

        void showSelectDate();

        void openFilterDialog();

        void refreshContents();
    }
}
