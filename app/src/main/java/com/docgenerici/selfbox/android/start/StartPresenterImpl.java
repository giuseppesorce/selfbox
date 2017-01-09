package com.docgenerici.selfbox.android.start;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.async.XmlTaskParse;
import com.docgenerici.selfbox.android.utils.SelfBoxUtils;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.EmailText;
import com.docgenerici.selfbox.models.LoginResponse;
import com.docgenerici.selfbox.models.MedicalList;
import com.docgenerici.selfbox.models.contents.Folder;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.medico.Medico;
import com.docgenerici.selfbox.models.persistence.ConfigurationApp;
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.persistence.ShareContentReminder;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
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



        Hawk.init(SelfBoxApplicationImpl.appComponent.context())
                .build();


        if (hereActivation()) {
//
            if (syncronized()) {
                //  deleteLastUpdate();
                view.gotoHome();

            } else {
                view.gotoSyncActivity();
            }
//
        } else {
            view.showActivationInput();
            getEmailText();

        }
    }

    private void deleteLastUpdate() {

        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp infoApp = realm.where(InfoApp.class).findFirst();
        try {
            realm.beginTransaction();
            infoApp.lastUpdate = 0;
            realm.copyToRealmOrUpdate(infoApp);

        } catch (Exception ex) {

        } finally {
            realm.commitTransaction();
        }

    }

    private boolean syncronized() {
        boolean syncro = false;
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        ConfigurationApp configurationApp = realm.where(ConfigurationApp.class).findFirst();
        if (configurationApp != null) {
            if (configurationApp.isSyncronized()) {
                syncro = true;
            }
        }
        return syncro;
    }

    @Override
    public void setActivation(String isfCode) {
        if (isfCode == null || isfCode.length() < 5) {
            view.showCodeError("Inserisci almeno 5 caratteri");
        } else {
            view.showProgressToSend();
            String appVer = SelfBoxUtils.getApplicationVersionName(SelfBoxApplicationImpl.appComponent.context());
            String devId = SelfBoxUtils.getDeviceId(SelfBoxApplicationImpl.appComponent.context());
            apiInteractor.login(appVer, devId, isfCode, isfCode)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<LoginResponse>() {
                        @Override
                        public void call(LoginResponse loginResponse) {

                            if (loginResponse.result) {
                                final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
                                realm.beginTransaction();
                                InfoApp infoApp = new InfoApp();
                                infoApp.line = loginResponse.line;
                                infoApp.name = loginResponse.name;
                                infoApp.surname = loginResponse.surname;
                                infoApp.repCode = loginResponse.repCode;
                                infoApp.lineCode = loginResponse.lineCode;
                                infoApp.lineShortCode = loginResponse.lineShortCode;
                                infoApp.result = loginResponse.result;
                                infoApp.selfBoxContentDownloadUrl = loginResponse.selfBoxContentDownloadUrl;
                                infoApp.selfBoxIsfDrugstoreDownloadUrl = loginResponse.selfBoxIsfDrugstoreDownloadUrl;
                                infoApp.selfBoxProductDownloadUrl = loginResponse.selfBoxProductDownloadUrl;
                                realm.copyToRealmOrUpdate(infoApp);
                                realm.commitTransaction();
                                if (syncronized()) {
                                    view.gotoHome();

                                } else {
                                    view.gotoSyncActivity();
                                }

                            } else {
                                view.showCodeError("Il codice inserito non è corretto");
                                view.showActivationInput();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                            Dbg.p("CALL ERRORE");

                        }
                    });
        }
    }


    private boolean hereActivation() {
        boolean activated = false;
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        InfoApp appInfo = realm.where(InfoApp.class).findFirst();
        if (appInfo != null) {
            if (!appInfo.repCode.isEmpty()) {

                activated = true;
            }
        }
        return activated;
    }

    private void getEmailText() {
        apiInteractor.getEmailText()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EmailText>() {
                    @Override
                    public void call(EmailText emailText) {
                        if (emailText != null) {
                            storeEmailText(emailText.mailTemplateDefaultText);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Dbg.p("CALL ERRORE getEmailText : " + throwable.getLocalizedMessage());

                    }
                });
    }

    private void storeEmailText(String text) {
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        ConfigurationApp configurationApp = realm.where(ConfigurationApp.class).findFirst();
        realm.beginTransaction();
        if (configurationApp == null) {
            configurationApp = new ConfigurationApp();
            configurationApp.setMailText(text);
            realm.copyToRealmOrUpdate(configurationApp);
        }

        realm.commitTransaction();


    }

}
