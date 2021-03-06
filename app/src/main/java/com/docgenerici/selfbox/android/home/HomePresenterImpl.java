package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.models.farmacia.Farmacia;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.Medico;
import com.docgenerici.selfbox.models.medico.MedicoDto;
import com.docgenerici.selfbox.models.persistence.MedicalView;
import com.docgenerici.selfbox.models.persistence.PharmaView;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Giuseppe Sorce
 */

public class HomePresenterImpl implements HomePresenter {
    private HomeView view;

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
        ArrayList<FarmaciaDto> pharmaList= new ArrayList<>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Farmacia> farmacie = realm.where(Farmacia.class).findAll();
        for (int i = 0; i < farmacie.size(); i++) {
            FarmaciaDto farmaciaDto= new FarmaciaDto();
            farmaciaDto.id= farmacie.get(i).id;
            farmaciaDto.fullname= farmacie.get(i).fullname;
            farmaciaDto.type= farmacie.get(i).type;
            farmaciaDto.ente= farmacie.get(i).ente;
            pharmaList.add(farmaciaDto);
        }
        return pharmaList;
    }

    @Override
    public ArrayList<MedicoDto> getMedicalList() {
        ArrayList<MedicoDto> medicoDtoArrayList= new ArrayList<>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Medico> medici = realm.where(Medico.class).findAll();
        for (int i = 0; i < medici.size(); i++) {
            MedicoDto farmaciaDto= new MedicoDto();
            farmaciaDto.id= medici.get(i).id;
            farmaciaDto.fullname= medici.get(i).fullname;
            farmaciaDto.email= medici.get(i).email;
            farmaciaDto.code= medici.get(i).code;

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
        try{
            realm.beginTransaction();
            MedicalView medicalView= new MedicalView();
            medicalView.setIdadd((int)new Date().getTime());
            medicalView.setD(new Date());
            medicalView.setId(lastMedicoUser.code);
            realm.copyToRealmOrUpdate(medicalView);
        }catch (Exception ex){

        }finally {
            realm.commitTransaction();
        }

    }

    @Override
    public void addPharmaView(FarmaciaDto lastPharmaUser) {
        Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        try{
            realm.beginTransaction();
            PharmaView pharmaView= new PharmaView();
            pharmaView.setIdadd((int)new Date().getTime());
            pharmaView.setD(new Date());
            pharmaView.setId(lastPharmaUser.ente);
            realm.copyToRealmOrUpdate(pharmaView);
        }catch (Exception ex){

        }finally {
            realm.commitTransaction();
        }
    }
}
