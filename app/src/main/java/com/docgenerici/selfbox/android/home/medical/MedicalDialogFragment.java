package com.docgenerici.selfbox.android.home.medical;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.adapters.ListMedicalUserAdapter;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.SimpleDividerItemDecoration;
import com.docgenerici.selfbox.android.home.HomeActivityInterface;
import com.docgenerici.selfbox.models.medico.MedicoDto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce #@copyright xx 18/10/16.
 */

public class MedicalDialogFragment extends DialogFragment implements OnItemClickListener, TextWatcher {

    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvHelp)
    TextView tvHelp;
    private ArrayList<MedicoDto> pharmaList;
    private ListMedicalUserAdapter adapter;
    private MedicoDto lastMedicoUser;
    private HomeActivityInterface homeInterface;

    public static MedicalDialogFragment createInstance(ArrayList<MedicoDto> medicoList) {
        MedicalDialogFragment frag = new MedicalDialogFragment();
        Bundle init = new Bundle();
        init.putParcelableArrayList("medicoList", medicoList);
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
        homeInterface = (HomeActivityInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_pharma_search, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);
        if (getArguments() != null) {
            pharmaList = getArguments().getParcelableArrayList("medicoList");
        }
        if (pharmaList != null && pharmaList.size() > 0) {
            createMedicalList();
        }
        tvHelp.setText("SELEZIONA MEDICO");
        etSearch.addTextChangedListener(this);
        return root;
    }

    @OnClick(R.id.btClose)
    void closeDialog() {
        dismiss();
    }

    private void createMedicalList() {
        adapter = new ListMedicalUserAdapter(new ArrayList<MedicoDto>(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.line_divider), getResources().getDimensionPixelSize(R.dimen.nomargin_divider_decotator)));
        rvList.setAdapter(adapter);
        filterList("");

    }

    @OnClick(R.id.btNext)
    void nextMedical() {
        if(lastMedicoUser == null || !checkMedialSelected()){
            Toast.makeText(getActivity(), "Seleziona un medico", Toast.LENGTH_SHORT).show();
        }else {
            homeInterface.onSelectMedicoUser(lastMedicoUser);
            dismiss();
        }
    }

    private boolean checkMedialSelected() {
        boolean selected= false;
        ArrayList<MedicoDto> medicoUsers = adapter.getMedicoUsers();
        for(MedicoDto medico : medicoUsers){
            if(medico.selected){
                selected= true;
                break;
            }
        }
        return selected;
    }

    @OnClick(R.id.btTraining)
    void selectTraining() {
        homeInterface.onSelectTrainingMedico();
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        MedicoDto pharmaSelect = adapter.getMedicoUser(position);
        if (lastMedicoUser != null && pharmaSelect.fullname.equalsIgnoreCase(lastMedicoUser.fullname)) {
            pharmaSelect.selected = false;
        } else {
            pharmaSelect.selected = true;
        }
        if (lastMedicoUser != null) {
            lastMedicoUser.selected = false;
        }
        lastMedicoUser = pharmaSelect;
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

        ArrayList<MedicoDto> worldpopulationlist = new ArrayList<>();
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(pharmaList);
        } else {
            for (MedicoDto wp : pharmaList) {
                if (wp.fullname.toLowerCase().startsWith(charText.toLowerCase())) {
                    worldpopulationlist.add(wp);

                }
            }
        }
        adapter.setPharmaList(worldpopulationlist);

    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
