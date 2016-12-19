package com.docgenerici.selfbox.android.contents.share;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.ListContentShareAdapter;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.SimpleDividerItemDecoration;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.persistence.ConfigurationApp;
import com.docgenerici.selfbox.models.persistence.InfoApp;
import com.docgenerici.selfbox.models.shares.ShareData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class ShareContentsDialogFragment extends DialogFragment implements OnItemClickListener {

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
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tvFrom)
    TextView tvFrom;
    @BindView(R.id.etText)
    EditText etText;
    String doctorCode="";
    String doctorName="";

    private ArrayList<ContentDoc> contentsShared;
    private ListContentShareAdapter adapter;
    private InfoApp infoApp;
    private ShareInterface shareInterface;

    public static ShareContentsDialogFragment createInstance(ArrayList<ContentDoc> contentsShared) {
        ShareContentsDialogFragment frag = new ShareContentsDialogFragment();
        Bundle init = new Bundle();
        init.putParcelableArrayList("contentsShared", contentsShared);
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
        if(getArguments() !=null){
            contentsShared= getArguments().getParcelableArrayList("contentsShared");
        }
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
        if(contentsShared !=null && contentsShared.size() > 0){

            adapter= new ListContentShareAdapter(contentsShared, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            rvList.setLayoutManager(linearLayoutManager);
            rvList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.line_divider), getResources().getDimensionPixelSize(R.dimen.margin_divider_decotator)));
            rvList.setAdapter(adapter);
        }

        Realm realm= SelfBoxApplicationImpl.appComponent.realm();
        ConfigurationApp appConfig = realm.where(ConfigurationApp.class).findFirst();
         infoApp = realm.where(InfoApp.class).findFirst();
        if(appConfig !=null){
            if(appConfig.getMailText() !=null && !appConfig.getMailText().isEmpty()){
                String email= appConfig.getMailText();
                if(infoApp !=null){
                    String name= infoApp.name;
                    String surname= infoApp.surname;
                    if(name !=null) {
                        email = email.replace("[isf-name]", name);
                    }else{
                        name="";
                    }
                    if(surname !=null) {
                        email = email.replace("[isf-surname]", surname);
                    }else{
                        surname="";
                    }
                    email= email.replace("[fullname]", doctorName);
                   tvFrom.setText(name +" "+surname);
                }
                etText.setText(Html.fromHtml(email));

            }
        }
        return root;
    }

    @OnClick(R.id.btSend)
    void sendShare(){
        String email= etEmail.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Inserisci un'email corretta", Toast.LENGTH_SHORT).show();
            return;
        }
        Realm realm= SelfBoxApplicationImpl.appComponent.realm();
        realm.beginTransaction();
        ShareData shareData= new ShareData();
        shareData.id= new Date().getTime();
        shareData.doctorCode= doctorCode;
        shareData.isfCode= infoApp.repCode;
        shareData.doctorEmail= email;
        shareData.emailCustomText= etText.getText().toString();
        String idShare= "";
        for (int i = 0; i < contentsShared.size(); i++) {
            idShare+= contentsShared.get(i).id+",";
        }
        shareData.contentIds= idShare;
        shareData.requestDate= ""+new Date().getTime();
        realm.commitTransaction();
        if(shareInterface !=null){
            shareInterface.onShareData(shareData);
        }
        dismiss();
    }

    @OnClick(R.id.btCancel)
    void cancelShare(){
        dismiss();
    }


    @OnClick(R.id.btClose)
    void closeDialog(){
        dismiss();
    }


    @OnClick(R.id.rlRoot)
    void closeHelpDialog(){
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        contentsShared.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void setShare(ShareInterface shareInterface) {
        this.shareInterface= shareInterface;

    }
}
