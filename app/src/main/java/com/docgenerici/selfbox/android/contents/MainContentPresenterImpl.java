package com.docgenerici.selfbox.android.contents;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.shares.ShareData;
import com.docgenerici.selfbox.models.shares.ShareDataSend;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class MainContentPresenterImpl implements MainContentPresenter {
    private final ApiInteractor apiInteractor;
    private MainContentView view;
    private ArrayList<ContentDoc> contentsShared;
    private String category_content;

    public MainContentPresenterImpl(ApiInteractor apiInteractor) {
        this.apiInteractor = apiInteractor;
    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof MainContentView)) {
            throw new IllegalArgumentException("View must extend MainContentPresenter.View");
        }
        this.view = (MainContentView) view;
    }

    @Override
    public void setup(String category) {
        category_content= category;
        view.setupView();
    }

    @Override
    public float getScale(int width, int width1) {
        return (float) width1 / (float) (width);
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
        this.contentsShared = contentsShared;
    }

    @Override
    public ArrayList<ContentDoc> getContentsShared() {
        return contentsShared;
    }

    @Override
    public void setCategories(String category, MedicoDto medicoSelected, FarmaciaDto lastPharmaUser) {
        category_content = category;
    }

    @Override
    public int getContentColor() {

        int color = 0;
        Resources res = SelfBoxApplicationImpl.appComponent.context().getResources();
        switch (category_content) {

            case "isf":
                return res.getColor(R.color.orange);
            case "medico":
                return res.getColor(R.color.blu);
            case "pharma":
                return res.getColor(R.color.green);
        }
        return color;


    }


    @Override
    public int getContentDarkColor() {
        int color = 0;
        Resources res = SelfBoxApplicationImpl.appComponent.context().getResources();
        switch ((category_content)) {

            case "isf":
                return res.getColor(R.color.orange_dark);
            case "medico":
                return res.getColor(R.color.blu_dark);
            case "pharma":
                return res.getColor(R.color.green_dark);
        }
        return color;
    }

    @Override
    public String getCategory() {
        return category_content;
    }

    @Override
    public Drawable getBackGroundhelp() {

        Resources res = SelfBoxApplicationImpl.appComponent.context().getResources();
        switch ((category_content)) {

            case "isf":
                return res.getDrawable(R.drawable.ic_help_contents_orange);
            case "medico":
                return res.getDrawable(R.drawable.ic_help_blu);
            case "pharma":
                return res.getDrawable(R.drawable.ic_help_contents);
        }
        return null;
    }

    @Override
    public void shareData(ShareData shareData) {
        ShareDataSend shareDataSend = new ShareDataSend();
        String[] ids = shareData.contentIds.split(",");
        shareDataSend.contentIds = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            shareDataSend.contentIds.add(Integer.parseInt(ids[i]));
        }
        shareDataSend.isfCode = shareData.isfCode;
        shareDataSend.doctorCode = shareData.doctorCode;
        shareDataSend.doctorEmail = shareData.doctorEmail;
        shareDataSend.emailCustomText = shareData.emailCustomText;
        shareDataSend.requestDate = shareData.requestDate;
        apiInteractor.shareData(shareDataSend)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody resp) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE share: " + throwable.getLocalizedMessage());

                    }
                });
    }


}
