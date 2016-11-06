package com.docgenerici.selfbox.android.contents.contentslist;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class ContentListPresenterImpl implements ContentListPresenter {
    private ContentView view;
    private final int FILTER_BY_DATE= 1;
    private final int FILTER_BY_AZ= 2;
    private  int FILTER_NOW= FILTER_BY_AZ;
    private ArrayList<ContentDoc> contents;

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

    @Override
    public void setup(int sample1, int sample2, int sample3) {
        contents = new ArrayList<>();
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Listino prezzi 05/10/2016", sample1, "10886"));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Listino medico settembre 2015", sample2, "10866"));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.FOLDER, "Programmi Eventi 2016", sample1, ""));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.FOLDER, "Congressi 2016", sample2, ""));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.VISUAL, "Presentazione nuovi prodotti", sample3, ""));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Brochure OMEGA 3", sample2, "10886"));
        contents.add(new ContentDoc(SelfBoxConstants.TypeContent.PDF, "Brochure 2016", sample1, "10886"));
    }

    @Override
    public List<ContentDoc> getContents() {
        return contents;
    }

    @Override
    public void setShare(int position) {
        contents.get(position).shared=!contents.get(position).shared;
        view.refreshContents();
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        ArrayList<ContentDoc> contentsShared= new ArrayList<>();
        for (int i = 0; i < contents.size(); i++) {
            if(contents.get(i).shared){
                contentsShared.add(contents.get(i));
            }
        }
        return contentsShared;
    }


}
