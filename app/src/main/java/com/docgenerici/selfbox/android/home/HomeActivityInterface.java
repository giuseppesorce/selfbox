package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;
import com.docgenerici.selfbox.models.medico.MedicoDto;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public interface HomeActivityInterface {


    void onSelectPharmaUser(FarmaciaDto lastPharmaUser);

    void onSelectMedicoUser(MedicoDto lastPharmaUser);

    void onSelectTrainingFamarcia();
    void onSelectTrainingMedico();
}
