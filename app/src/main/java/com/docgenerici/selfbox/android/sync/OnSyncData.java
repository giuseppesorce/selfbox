package com.docgenerici.selfbox.android.sync;

import com.docgenerici.selfbox.models.products.Prodotto;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xxxx 2016.
 *         <p>......</p>
 */

public interface OnSyncData {
    void onXmlParsed(ArrayList<Prodotto> products);
}
