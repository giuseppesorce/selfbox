package com.docgenerici.selfbox.android.contents;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.models.ContentDoc;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class MainContentPresenterImpl implements MainContentPresenter {
    private MainContentView view;
    private ArrayList<ContentDoc> contentsShared;
    private String category_content;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof MainContentView)) {
            throw new IllegalArgumentException("View must extend MainContentPresenter.View");
        }
        this.view = (MainContentView) view;
    }

    @Override
    public void setup() {
        view.setupView();
    }

    @Override
    public float getScale(int width, int width1) {
        return (float)width1 /(float)(width);
    }

    @Override
    public void onSelectContents() {
        view.showContents();
    }

    @Override
    public void onSelectProducts() {
        view.showProducts();
    }

    @Override
    public void onSelectShare() {
        view.showShareContents(contentsShared);
    }

    @Override
    public void setShareContents(ArrayList<ContentDoc> contentsShared) {
        this.contentsShared= contentsShared;
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        return contentsShared;
    }

    @Override
    public void setCategory(String category) {
       category_content= category;
    }

    @Override
    public int getContentColor() {

        int color= 0;
        Resources res= SelfBoxApplicationImpl.appComponent.context().getResources();
        switch (category_content){

            case "isf":
                return  res.getColor(R.color.orange);
            case "medico":
                return  res.getColor(R.color.blu);
            case "pharma":
                return  res.getColor(R.color.green);
        }
        return color;


    }



    @Override
    public int getContentDarkColor() {
        int color= 0;
        Resources res= SelfBoxApplicationImpl.appComponent.context().getResources();
        switch ((category_content)){

            case "isf":
                return  res.getColor(R.color.orange_dark);
            case "medico":
                return  res.getColor(R.color.blu_dark);
            case "pharma":
                return  res.getColor(R.color.green_dark);
        }
        return color;
    }

    @Override
    public String getCategory() {
        return category_content;
    }

    @Override
    public Drawable getBackGroundhelp() {

        Resources res= SelfBoxApplicationImpl.appComponent.context().getResources();
        switch ((category_content)){

            case "isf":
                return  res.getDrawable(R.drawable.ic_help_contents_orange);
            case "medico":
                return  res.getDrawable(R.drawable.ic_help_blu);
            case "pharma":
                return  res.getDrawable(R.drawable.ic_help_contents);
        }
        return null;
    }


}
