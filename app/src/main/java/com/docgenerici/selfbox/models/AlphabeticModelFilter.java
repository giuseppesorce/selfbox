package com.docgenerici.selfbox.models;


import android.widget.Filter;

import com.docgenerici.selfbox.models.farmacia.FarmaciaDto;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public class AlphabeticModelFilter extends Filter {
    private ArrayList<FarmaciaDto> allModelItemsArray;
    private ArrayList<FarmaciaDto> filteredModelItemsArray;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        constraint = constraint.toString().toLowerCase();
        System.out.println("In performFiltering()");
        FilterResults result = new FilterResults();
        if(constraint != null && constraint.toString().length() > 0)
        {
            ArrayList<FarmaciaDto> filteredItems = new ArrayList<FarmaciaDto>();

            for(int i = 0, l = allModelItemsArray.size(); i < l; i++)
            {
                FarmaciaDto m = allModelItemsArray.get(i);
                if(m.fullname.toLowerCase().contains(constraint))
                    filteredItems.add(m);
            }
            result.count = filteredItems.size();
            result.values = filteredItems;
        }
        else
        {
            synchronized(this)
            {
                result.values = allModelItemsArray;
                result.count = allModelItemsArray.size();
            }
        }
        return result;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredModelItemsArray = (ArrayList<FarmaciaDto>)results.values;
    }
}
