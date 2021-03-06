package com.docgenerici.selfbox.android.contents;


import android.graphics.drawable.Drawable;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.shares.ShareData;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce
 */

public interface MainContentPresenter extends Presenter {

    void setup(String category);

    float getScale(int width, int width1);

    void onSelectContents();

    void onSelectProducts();

    void onSelectShare();

    void setShareContents(ArrayList<ContentDoc> contentsShared);

    ArrayList<ContentDoc> getContentsShared();

    void setCategories(String category, MedicoDto medicoSelected, FarmaciaDto lastPharmaUser);

    int getContentColor();

    int getContentDarkColor();

    String getCategory();

    Drawable getBackGroundhelp();

    void shareData(ShareData shareData);


    interface MainContentView extends BaseView {

        void setupView();

        void showContents();

        void showProducts();

        void showShareContents(ArrayList<ContentDoc> contentsShared);
    }
}
