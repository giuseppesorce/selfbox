package com.docgenerici.selfbox.android.contents;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.home.ServerResponse;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.persistence.ItemShared;
import com.docgenerici.selfbox.models.persistence.ShareContentReminder;
import com.docgenerici.selfbox.models.products.ProductShare;
import com.docgenerici.selfbox.models.shares.ShareData;
import com.docgenerici.selfbox.models.shares.ShareDataSend;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
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
        category_content = category;
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
      final  ShareDataSend shareDataSend = new ShareDataSend();
        String[] ids = shareData.contentIds.split(",");
        ArrayList<ItemShared> itemsToShare = getItemShared(ids);
        shareDataSend.contentIds = new ArrayList<>();
        shareDataSend.products = new ArrayList<>();

        for (int i = 0; i < itemsToShare.size(); i++) {

            if (itemsToShare.get(i).getType().equalsIgnoreCase("content")) {
                shareDataSend.contentIds.add(Integer.parseInt(itemsToShare.get(i).getId()));
            } else if (itemsToShare.get(i).getType().equalsIgnoreCase("product")) {
                shareDataSend.products.add(new ProductShare(itemsToShare.get(i).getName(), itemsToShare.get(i).getPath()));
            }
        }
        if (shareDataSend.products.size() > 0) {
            shareDataSend.products.add(new ProductShare("Listino prezzi", SelfBoxConstants.PATH_LISTINO));
        }
        shareDataSend.isfCode = shareData.isfCode;
        shareDataSend.doctorCode = shareData.doctorCode;
        shareDataSend.drugstoreCode = shareData.drugStore;
        shareDataSend.email = shareData.doctorEmail;
        shareDataSend.emailCustomText = shareData.emailCustomText;
        shareDataSend.requestDate = Long.parseLong(shareData.requestDate);
        apiInteractor.shareData(shareDataSend)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ServerResponse>() {
                    @Override
                    public void call(ServerResponse resp) {
                        if(resp.success) {
                            view.onSuccessContentShare();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                            saveSendShare(shareDataSend);
                            view.showReminder();




                    }
                });
    }

    private void saveSendShare(ShareDataSend shareDataSend) {
        String jsonReminder= new Gson().toJson(shareDataSend);
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        try{
            realm.beginTransaction();
            ShareContentReminder shareReminder= new ShareContentReminder();
            shareReminder.setId(new Date().getTime());
            shareReminder.setReminderShare(jsonReminder);
            realm.copyToRealmOrUpdate(shareReminder);
        }catch (Exception ex){

        }finally {
            realm.commitTransaction();
        }
    }

    private ArrayList<ItemShared> getItemShared(String[] idsSh) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();

        RealmQuery<ItemShared> query = realm.where(ItemShared.class);
        int i = 0;
        for (String id : idsSh) {
            // The or() operator requires left hand and right hand elements.
            // If articleIds had only one element then it would crash with
            // "Missing right-hand side of OR"
            if (i++ > 0) {
                query = query.or();
            }
            query = query.equalTo("id", id);
        }
        final RealmResults<ItemShared> itemsToShared = query.findAll();
        return new ArrayList<>(itemsToShared);
    }

    @Override
    public void refreshContents() {
        ArrayList<ItemShared> sharedItems = getContentShared();
        Dbg.p("sharedItem: " + sharedItems.size());
        if (sharedItems != null) {
            view.refreshContentShare(sharedItems.size());
        } else {
            view.refreshContentShare(0);
        }
    }

    @Override
    public void deleteShareContent() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        final RealmResults<ItemShared> sharedItems = realm.where(ItemShared.class).findAll();
        if (sharedItems != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sharedItems.deleteAllFromRealm();
                }
            });
        }
    }

    private ArrayList<ItemShared> getContentShared() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<ItemShared> sharedItem = realm.where(ItemShared.class).findAll();
        return new ArrayList<>(sharedItem);
    }


}
