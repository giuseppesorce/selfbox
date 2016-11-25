package com.docgenerici.selfbox.android.start;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce #@copyright xx 22/09/16.
 */

public class StartPresenterImpl implements StartPresenter {
    private final ApiInteractor apiInteractor;
    private StartView view;

    public StartPresenterImpl(ApiInteractor loginInteractor) {
        this.apiInteractor = loginInteractor;

    }

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



        }else{
//            String appLicence="77750";
//            String isfCode="77750";
//            String appVer="1.0.0-beta";
//            String devId="gfgfgfg";
//            apiInteractor.login(appVer, devId, isfCode,  appLicence)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<LoginResponse>() {
//                        @Override
//                        public void call(LoginResponse loginResponse) {
//                            //getAllMedicalData();
//                        }
//                    }, new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//
//                            Dbg.p("CALL ERRORE");
//
//                        }
//                    });

            getAllContents();

        }
    }

    private void getAllMedicalData() {

        String isf="77750";
        apiInteractor.getallMedical(isf)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MedicalList>() {
                    @Override
                    public void call(MedicalList loginResponse) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getAllMedicalData");

                    }
                });


    }

    private void getAllContents() {

        String isf="77750";
        apiInteractor.getAllContents(isf)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Folder>>() {
                    @Override
                    public void call(List<Folder> folders) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getAllcontents");

                    }
                });


    }

    private boolean hereActivation() {

        return false;
    }
}
