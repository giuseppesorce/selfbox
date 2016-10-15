package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.docgenerici.selfbox.R;

import butterknife.ButterKnife;

/**
 * @author Giuseppe Sorce #@copyright xx 15/10/16.
 */

public class ContentsListFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.frag_contents_list,container,false);
        ButterKnife.bind(this, view);

        if(getArguments() !=null){

        }
        return view;

    }


}
