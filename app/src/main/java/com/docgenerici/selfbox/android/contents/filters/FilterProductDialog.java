package com.docgenerici.selfbox.android.contents.filters;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.adapters.FilterAdapter;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.contents.productlist.OnSelectFilter;
import com.docgenerici.selfbox.android.utils.SelfBoxUtils;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.FilterProduct;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce #@copyright xx 17/10/16.
 */

public class FilterProductDialog extends DialogFragment implements OnItemClickListener {


    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;
    @BindView(R.id.tvFiltri)
    TextView tvFiltri;
    @BindView(R.id.rvList)
    RecyclerView rvList;

    private boolean filterVideo = false;
    private boolean filterAvvisi = false;
    private boolean filterVideoIntervista = false;
    private boolean filterDocumenti = false;
    private boolean filterVisual = false;
    private ArrayList<String> categories;
    private OnSelectFilter listener;
    private ArrayList<FilterProduct> filtersProducts;


    public static FilterProductDialog createInstance(ArrayList<String> categories) {
        FilterProductDialog frag = new FilterProductDialog();
        Bundle init = new Bundle();
        init.putStringArrayList("categoriesList", categories);
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
        View root = inflater.inflate(R.layout.dialog_filters_product, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);
        if (getArguments() != null) {
            categories = getArguments().getStringArrayList("categoriesList");
        }
        createLegendFilter();
        return root;
    }

    private void createLegendFilter() {

        filtersProducts = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            filtersProducts.add(new FilterProduct(Color.parseColor(SelfBoxUtils.getCategoryColor(categories.get(i))), categories.get(i), true));
        }
        FilterAdapter adapter = new FilterAdapter(getActivity().getApplicationContext(), filtersProducts, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(adapter);
    }


    @OnClick(R.id.tvFiltri)
    void onSelectFiltri() {
        dismiss();
    }

    @OnClick(R.id.rlRoot)
    void onSelectRoot() {
        dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        for (int i = 0; i < filtersProducts.size(); i++) {
            Dbg.p("filtersProducts: " + filtersProducts.get(i).select);
        }
        listener.onChangeFilter(filtersProducts);
    }

    public void setListener(OnSelectFilter listener) {
        this.listener = listener;
    }
//
//    @OnClick(R.id.tvAvvisi)
//    void onSelectAvvisi() {
//        filterAvvisi = !filterAvvisi;
//        if (filterAvvisi) {
//            tvAvvisi.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
//        } else {
//            tvAvvisi.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
//        }
//    }
//
//    @OnClick(R.id.tvDocumenti)
//    void onSelectDocumenti() {
//        filterDocumenti = !filterDocumenti;
//        if (filterDocumenti) {
//            tvDocumenti.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
//        } else {
//            tvDocumenti.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
//        }
//
//    }
//
//    @OnClick(R.id.tvVideo)
//    void onSelectVideo() {
//        filterVideo = !filterVideo;
//        if (filterVideo) {
//            tvVideo.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
//        } else {
//            tvVideo.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
//        }
//
//    }
//
//    @OnClick(R.id.tvVisual)
//    void onSelectVisual() {
//        filterVisual = !filterVisual;
//        if (filterVisual) {
//            tvVisual.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
//        } else {
//            tvVisual.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
//        }
//
//    }
//
//    @OnClick(R.id.tvVideoIntervesta)
//    void onSelectVideoIntervista() {
//        filterVideoIntervista = !filterVideoIntervista;
//        if (filterVideoIntervista) {
//            tvVideoIntervesta.setCompoundDrawablesWithIntrinsicBounds(null, null, dwCheck, null);
//        } else {
//            tvVideoIntervesta.setCompoundDrawablesWithIntrinsicBounds(null, null, dwUnCheck, null);
//        }
//
//    }
}