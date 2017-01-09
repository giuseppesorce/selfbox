package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.comm.ApiInteractor;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.contents.ContentBox;
import com.docgenerici.selfbox.models.contents.Target;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.Medico;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.persistence.ItemShared;
import com.docgenerici.selfbox.models.persistence.MedicalView;
import com.docgenerici.selfbox.models.persistence.PharmaView;
import com.docgenerici.selfbox.models.persistence.ShareContentReminder;
import com.docgenerici.selfbox.models.shares.ShareDataSend;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Giuseppe Sorce
 */

public class HomePresenterImpl implements HomePresenter {
    private final ApiInteractor apiInteractor;
    private HomeView view;


    public HomePresenterImpl(ApiInteractor apiInteractor) {
        this.apiInteractor = apiInteractor;
    }

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof HomeView)) {
            throw new IllegalArgumentException("View must extend HomePresenter.View");
        }
        this.view = (HomeView) view;
    }

    @Override
    public void onSelectISF() {
        view.showISF();
    }

    @Override
    public void onSelectMedico() {
        view.showDialogMedicalSearch();
    }

    @Override
    public void onSelectPharma() {
        view.showDialogPharmaSearch();
    }

    @Override
    public void onSelectSync() {
        view.gotoSync();
    }

    @Override
    public void onSelectHelp() {
        view.showHelp();
    }

    @Override
    public void onSelectInfo() {
        view.showInfo();
    }

    @Override
    public ArrayList<FarmaciaDto> getPharmaList() {
        ArrayList<FarmaciaDto> pharmaList = new ArrayList<>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Farmacia> farmacie = realm.where(Farmacia.class).findAll();
        for (int i = 0; i < farmacie.size(); i++) {
            FarmaciaDto farmaciaDto = new FarmaciaDto();
            farmaciaDto.id = farmacie.get(i).id;
            farmaciaDto.fullname = farmacie.get(i).fullname;
            farmaciaDto.type = farmacie.get(i).type;
            farmaciaDto.ente = farmacie.get(i).ente;
            pharmaList.add(farmaciaDto);
        }
        return pharmaList;
    }

    @Override
    public ArrayList<MedicoDto> getMedicalList() {
        ArrayList<MedicoDto> medicoDtoArrayList = new ArrayList<>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Medico> medici = realm.where(Medico.class).findAll();
        for (int i = 0; i < medici.size(); i++) {
            MedicoDto farmaciaDto = new MedicoDto();
            farmaciaDto.id = medici.get(i).id;
            farmaciaDto.fullname = medici.get(i).fullname;
            farmaciaDto.email = medici.get(i).email;
            farmaciaDto.code = medici.get(i).code;

            medicoDtoArrayList.add(farmaciaDto);
        }
        return medicoDtoArrayList;
    }

    @Override
    public void onSelectMedicoUser(MedicoDto lastMedicoUser) {

        view.showMedico(lastMedicoUser);
    }

    @Override
    public void addMedicalView(MedicoDto lastMedicoUser) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        try {
            realm.beginTransaction();
            MedicalView medicalView = new MedicalView();
            medicalView.setIdadd((int) new Date().getTime());
            medicalView.setSelectionDate(new Date());
            medicalView.setCode(lastMedicoUser.code);
            realm.copyToRealmOrUpdate(medicalView);
        } catch (Exception ex) {

        } finally {
            realm.commitTransaction();
        }

    }

    @Override
    public void addPharmaView(FarmaciaDto lastPharmaUser) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        try {
            realm.beginTransaction();
            PharmaView pharmaView = new PharmaView();
            pharmaView.setIdadd((int) new Date().getTime());
            pharmaView.setSelectionDate(new Date());
            pharmaView.setCode(lastPharmaUser.ente);
            realm.copyToRealmOrUpdate(pharmaView);
        } catch (Exception ex) {

        } finally {
            realm.commitTransaction();
        }
    }

    @Override
    public void checkNotification() {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<ContentBox> newContents = realm.where(ContentBox.class).equalTo("newcontent", true).findAll();
        InfoApp infoApp = realm.where(InfoApp.class).findFirst();

        InfoApp info = realm.where(InfoApp.class).findFirst();
        String userTarget = info.lineShortCode;
        Dbg.p("CONTENUTI NUOVI:" + newContents.size());
        int isf = 0;
        int medico = 0;
        int pharma = 0;
        if (newContents != null && newContents.size() > 0) {
            for (ContentBox contentBox : newContents) {

                ArrayList<Target> targetsContents = new ArrayList<Target>(contentBox.targets);
                for (int i = 0; i < targetsContents.size(); i++) {
                    String target = targetsContents.get(i).code.toLowerCase();
                    switch (target) {
                        case "isf_specia":
                            if (userTarget.equalsIgnoreCase("s")) {
                                isf++;
                            }
                            break;

                        case "isf":
                            if (userTarget.equalsIgnoreCase("g")) {
                                isf++;
                            } else {

                            }
                            break;
                        case "farmacia":
                            pharma++;
                            break;
                        case "medico":
                            medico++;
                    }
                }
            }
            if (isf > 0) {
                view.showIsfNotification(isf);
            } else {
                view.hideIsfNotification();
            }
            if (medico > 0) {
                view.showMedicalNotification(medico);
            } else {
                view.hideMedicalNotification();
            }
            if (pharma > 0) {
                view.showPharmaNotification(pharma);
            } else {
                view.hidePharmaNotification();
            }

        } else {
            view.hideIsfNotification();
            view.hidePharmaNotification();
            view.hideMedicalNotification();

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

    @Override
    public void checkReminder() {
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        ShareContentReminder reminder = realm.where(ShareContentReminder.class).findFirst();
        Dbg.p("checkReminder");
        if (reminder != null) {
            final long idReminder = reminder.getId();
            if (!reminder.getReminderShare().isEmpty()) {
                ShareDataSend shareDataSend = null;
                try {
                    shareDataSend = new Gson().fromJson(reminder.getReminderShare(), ShareDataSend.class);
                } catch (Exception ex) {
                }
                if (shareDataSend != null) {
                    Dbg.p("checkReminder invio");
                    apiInteractor.shareData(shareDataSend)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<ServerResponse>() {
                                @Override
                                public void call(ServerResponse resp) {
                                    if(resp.success) {
                                        try {


                                            final ShareContentReminder reminderDelelte = realm.where(ShareContentReminder.class).equalTo("id", idReminder).findFirst();
                                            if (reminderDelelte != null) {
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        reminderDelelte.deleteFromRealm();
                                                        Dbg.p("checkReminder cancello");
                                                        checkReminder();
                                                    }
                                                });
                                            }
                                        } catch (Exception ex) {

                                        }
                                    }


                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    Dbg.p("checkReminder INVIO ERRORE");

                                }
                            });
                }

            }
        }
        /*
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
         */
    }
}