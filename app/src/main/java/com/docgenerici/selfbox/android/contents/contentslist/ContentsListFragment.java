package com.docgenerici.selfbox.android.contents.contentslist;

import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce
 */

public class ContentsListFragment extends Fragment implements ContentListPresenter.ContentView {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llAZ)
    LinearLayout llAZ;
    @BindColor(R.color.grey_filter)
    int grey_filter;
    private ContentListPresenter presenter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.frag_contents_list,container,false);
        ButterKnife.bind(this, view);
        presenter= SelfBoxApplicationImpl.appComponent.contentListPresenter();
        presenter.setView(this);
        if(getArguments() !=null){

        }
        return view;

    }

    @OnClick(R.id.llAZ)
    void onSelectAZ(){
     presenter.selectAZ();
    }

    @OnClick(R.id.llDate)
    void onSelectDate(){
        presenter.selectDate();

    }

    @OnClick(R.id.btFilter)
    void onSelectFilter(){

    }


    @Override
    public void showSelectAz() {
        llAZ.getBackground().setColorFilter(grey_filter, PorterDuff.Mode.MULTIPLY);
        llDate.getBackground().clearColorFilter();
    }

    @Override
    public void showSelectDate() {
        llAZ.getBackground().clearColorFilter();
        llDate.getBackground().setColorFilter(grey_filter, PorterDuff.Mode.MULTIPLY);
    }
}
