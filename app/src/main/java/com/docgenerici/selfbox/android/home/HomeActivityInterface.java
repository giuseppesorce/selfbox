package com.docgenerici.selfbox.android.home;

import com.docgenerici.selfbox.models.PharmaUser;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public interface HomeActivityInterface {


    void onSelectPharmaUser(PharmaUser lastPharmaUser);

    void onSelectTraining(PharmaUser lastPharmaUser);
}
