package com.docgenerici.selfbox.android.contents.productlist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.ProductsAdapter;
import com.docgenerici.selfbox.android.contents.contentslist.ContentsListFragment;
import com.docgenerici.selfbox.android.contents.filters.FilterDialog;
import com.docgenerici.selfbox.android.contents.filters.FilterProductDialog;
import com.docgenerici.selfbox.android.contents.productlist.legenda.LegendaDialogFragment;
import com.docgenerici.selfbox.android.pdf.PdfActivity;
import com.docgenerici.selfbox.models.ProductDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Giuseppe Sorce
 */

public class ProductListFragment extends Fragment implements ProductsListPresenter.PListView, OnItemClickListener {


    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    private ProductsListPresenter presenter;
    private ProductsAdapter adapter;
    private FilterProductDialog filtersDialog;
    private LegendaDialogFragment legendaFragment;
    @BindView(R.id.btFilter)
    Button btFilter;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.llDate)
    LinearLayout llDate;
    @BindView(R.id.llAZ)
    LinearLayout llAZ;
    @BindColor(R.color.grey_filter)
    int grey_filter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_products_list, container, false);
        ButterKnife.bind(this, view);
        presenter = SelfBoxApplicationImpl.appComponent.productsListPresenter();
        presenter.setView(this);
        presenter.setup();
        if (getArguments() != null) {

        }
        String category = SelfBoxApplicationImpl.appComponent.mainContentPresenter().getCategory();
        Resources res = getResources();
        switch ((category)) {

            case "isf":
                btFilter.setBackgroundResource(R.drawable.ic_filter_orange);
                Drawable ic_search_orange = res.getDrawable(R.drawable.ic_search_orange);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_orange, null);
                break;
            case "medico":
                Drawable ic_search_blue = res.getDrawable(R.drawable.ic_search_blu);
                btFilter.setBackgroundResource(R.drawable.ic_filter_blu);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_blue, null);

                break;
            case "pharma":
                Drawable ic_search_green = res.getDrawable(R.drawable.ic_search);
                btFilter.setBackgroundResource(R.drawable.ic_filter_green);
                etSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, ic_search_green, null);
                break;
        }
        presenter.selectAZ();
        return view;

    }


    @OnClick(R.id.llAZ)
    void onSelectAZ() {
        presenter.selectAZ();
    }

    @OnClick(R.id.llDate)
    void onSelectDate() {
        presenter.selectDate();
    }

    @OnClick(R.id.btFilter)
    void onSelectFilter() {
        presenter.onSelectFilter();
    }

    @Override
    public void setup() {
        ArrayList<ProductDoc> productDocArrayList = new ArrayList<>();
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.HEADER, "ALTRI PRODOTTI TERAPEUTICI"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "SEVELAMER DOC", "180 cpr riv. con film 800 mg", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.HEADER, "ANTIINFETTIVI"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "SEVELAMER DOC", "180 cpr riv. con film 800 mg", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "PIROXICAM", "20 mg cpr rig. con 30 capsule, uso orale", "A85", "FCN"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "FLUCONAZOLO", "100 mg capsule rigide", "A94", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "NIMESULIDE", "100 mg compresse", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "SILDENAFIL", "50 mc compresse masticabili", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "SEVELAMER DOC", "180 cpr riv. con film 800 mg", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "PIROXICAM", "20 mg cpr rig. con 30 capsule, uso orale", "A85", "FCN"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "FLUCONAZOLO", "100 mg capsule rigide", "A94", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "NIMESULIDE", "100 mg compresse", "A84", "FCD"));
        productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.PRODUCT, "SILDENAFIL", "50 mc compresse masticabili", "A84", "FCD"));
        adapter = new ProductsAdapter(getActivity(), productDocArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);
        rvProduct.setAdapter(adapter);
    }

    @Override
    public void openFilterDialog() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        filtersDialog = FilterProductDialog.createInstance();
        filtersDialog.show(ft, "filtersDialog");
    }

    @Override
    public void showLegenda() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        legendaFragment = LegendaDialogFragment.createInstance();
        legendaFragment.show(ft, "legendaFragment");
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

    @Override
    public void onItemClick(View view, int position) {
        getActivity().startActivity(new Intent(getActivity(), PdfActivity.class));

    }

    @OnClick(R.id.btLegenda)
    void onTapLegenda() {
        presenter.onSelectLegenda();
    }

    public static ProductListFragment createInstance() {
        ProductListFragment frag = new ProductListFragment();
        Bundle init = new Bundle();
        frag.setArguments(init);
        return frag;
    }
}
