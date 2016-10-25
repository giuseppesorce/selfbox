package com.docgenerici.selfbox.android.contents.share;

import android.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.docgenerici.selfbox.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class ShareContentsDialogFragment extends DialogFragment {

    @BindView(R.id.npDay)
    NumberPicker npDay;
    @BindView(R.id.npHour)
    NumberPicker npHour;
    @BindView(R.id.npMinutes)
    NumberPicker npMinutes;
    @BindView(R.id.npMonth)
    NumberPicker npMonth;
    @BindView(R.id.npYear)
    NumberPicker npYear;
    public static ShareContentsDialogFragment createInstance() {
        ShareContentsDialogFragment frag = new ShareContentsDialogFragment();
        Bundle init = new Bundle();
        frag.setArguments(init);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.detail);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_share_contents, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);

        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        String[] arrayPicker= new String[]{"GEN","FEB","MAR","APR","MAG", "GIU", "LUG", "AGO", "SET", "OTT", "NOV", "DEC"};
        String[] hours= new String[24];
        String[] minutes= new String[60];
        for (int i = 0; i < 60; i++) {
            minutes[i]=(String.valueOf(i <10 ? "0"+i : i));
        }
        for (int i = 0; i < 24; i++) {

            hours[i]=(String.valueOf(i <10 ? "0"+i : i));
        }
        npMonth.setDisplayedValues(arrayPicker);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npHour.setDisplayedValues(hours);

        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);
        npMinutes.setDisplayedValues(minutes);
        Calendar calendar = Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        npYear.setMinValue(2016);
        npYear.setMaxValue(year+1);

        calendar.setTime(new Date());
        npDay.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        npMonth.setValue(calendar.get(Calendar.MONTH)+1);
        npYear.setValue(calendar.get(Calendar.YEAR));
        npHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npMinutes.setValue(calendar.get(Calendar.MINUTE));

        return root;
    }

    @OnClick(R.id.btClose)
    void closeDialog(){
        dismiss();
    }

    @OnClick(R.id.rlRoot)
    void closeHelpDialog(){
        dismiss();
    }
}