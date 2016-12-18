package com.docgenerici.selfbox.android.contents.productlist;

import com.docgenerici.selfbox.models.FilterProduct;

import java.util.ArrayList;

/**
 * @uthor giuseppesorce
 */

public interface OnSelectFilter {
    void onChangeFilter(ArrayList<FilterProduct> filtersProducts);
}
