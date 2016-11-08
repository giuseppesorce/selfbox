package com.docgenerici.selfbox.android.home.pharma;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.adapters.ListPharmauserAdapter;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.SimpleDividerItemDecoration;
import com.docgenerici.selfbox.android.home.HomeActivityInterface;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.PharmaUser;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.docgenerici.selfbox.R.id.rvList;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class PharmaDialogFragment extends DialogFragment implements OnItemClickListener, TextWatcher {

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.etSearch)
    EditText etSearch;

    private ArrayList<PharmaUser> pharmaList;
    private ListPharmauserAdapter adapter;
    private PharmaUser lastPharmaUser;
    private HomeActivityInterface homeInterface;

    public static PharmaDialogFragment createInstance(ArrayList<PharmaUser> pharmaList) {
        PharmaDialogFragment frag = new PharmaDialogFragment();
        Bundle init = new Bundle();
        init.putParcelableArrayList("pharmaList", pharmaList);
        frag.setArguments(init);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.detail);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeInterface= (HomeActivityInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_pharma_search, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);
            if(getArguments() !=null){
            pharmaList= getArguments().getParcelableArrayList("pharmaList");
        }
        if(pharmaList !=null && pharmaList.size() > 0){
            createPharmaList();
        }

        etSearch.addTextChangedListener(this);

        return root;
    }

    @OnClick(R.id.btClose)
    void closeDialog(){
        dismiss();
    }

    private void createPharmaList() {
        adapter= new ListPharmauserAdapter(new ArrayList<PharmaUser>(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.line_divider), getResources().getDimensionPixelSize(R.dimen.nomargin_divider_decotator)));
        rvList.setAdapter(adapter);
        filterList("");

    }

    @OnClick(R.id.btNext)
    void nextPharma(){
        homeInterface.onSelectPharmaUser(lastPharmaUser);
        dismiss();
    }

    @OnClick(R.id.btTraining)
    void selectTraining(){
        homeInterface.onSelectTraining(lastPharmaUser);
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        PharmaUser pharmaSelect = adapter.getPharmaUser(position);
        if (lastPharmaUser !=null && pharmaSelect.name.equalsIgnoreCase(lastPharmaUser.name)) {
                pharmaSelect.selected= false;
        }else{
            pharmaSelect.selected= true;
        }
        if (lastPharmaUser !=null) {
            lastPharmaUser.selected = false;
        }
        lastPharmaUser= pharmaSelect;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String filterText = etSearch.getText().toString().toLowerCase();
        filterList(filterText);
    }

    private void filterList(String charText) {

        ArrayList<PharmaUser> worldpopulationlist = new ArrayList<>();
            worldpopulationlist.clear();
            if (charText.length() == 0) {
                worldpopulationlist.addAll(pharmaList);
            }
            else
            {
                for (PharmaUser wp : pharmaList)
                {
                    if (wp.name.toLowerCase().startsWith(charText.toLowerCase()))
                    {
                        worldpopulationlist.add(wp);
                        Dbg.p("aggiungo "+wp.name);
                    }
                }
            }
            adapter.setPharmaList(worldpopulationlist);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
