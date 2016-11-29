package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public interface HomeActivityInterface {


    void onSelectPharmaUser(FarmaciaDto lastPharmaUser);

    void onSelectTraining(FarmaciaDto lastPharmaUser);
}
