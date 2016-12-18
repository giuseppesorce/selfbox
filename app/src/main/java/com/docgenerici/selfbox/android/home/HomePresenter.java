package com.docgenerici.selfbox.android.home;


import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.Presenter;
import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce
 */

public interface HomePresenter extends Presenter {

    void onSelectISF();

    void onSelectMedico();

    void onSelectPharma();

    void onSelectSync();

    void onSelectHelp();

    void onSelectInfo();

    ArrayList<FarmaciaDto> getPharmaList();
    ArrayList<MedicoDto> getMedicalList();

    void onSelectMedicoUser(MedicoDto lastMedicoUser);

    interface HomeView extends BaseView {

        void showISF();

        void showMedico(MedicoDto lastMedicoUser);

        void showDialogPharmaSearch();

        void showHelp();

        void gotoSync();

        void showInfo();

        void showDialogMedicalSearch();
    }
}
