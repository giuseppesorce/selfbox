package com.docgenerici.selfbox.models;


import android.widget.Filter;

import java.util.ArrayList;

/**
 * @author Giuseppe Sorce #@copyright xx 06/11/16.
 */

public class AlphabeticModelFilter extends Filter {
    private ArrayList<PharmaUser> allModelItemsArray;
    private ArrayList<PharmaUser> filteredModelItemsArray;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        constraint = constraint.toString().toLowerCase();
        System.out.println("In performFiltering()");
        FilterResults result = new FilterResults();
        if(constraint != null && constraint.toString().length() > 0)
        {
            ArrayList<PharmaUser> filteredItems = new ArrayList<PharmaUser>();

            for(int i = 0, l = allModelItemsArray.size(); i < l; i++)
            {
                PharmaUser m = allModelItemsArray.get(i);
                if(m.name.toLowerCase().contains(constraint))
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
        filteredModelItemsArray = (ArrayList<PharmaUser>)results.values;
    }
}
