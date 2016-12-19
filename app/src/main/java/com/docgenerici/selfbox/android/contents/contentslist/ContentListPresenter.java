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

    void setup(int folderdraw);


    List<ContentDoc> getContents(String categoryContent);

    void setShare(ContentDoc position);

    ArrayList<ContentDoc> getContentsShared();

    List<ContentDoc> getContentsByFolder(int id);

    void setLevelView(int i);

    int getLevelView();

    interface ContentView extends BaseView {

        void showSelectAz();

        void showSelectDate();

        void openFilterDialog();

        void refreshContents();

        void setup();
    }
}
