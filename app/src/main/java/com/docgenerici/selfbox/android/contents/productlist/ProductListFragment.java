package com.docgenerici.selfbox.android.contents.productlist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.OnItemClickListener;
import com.docgenerici.selfbox.android.adapters.ProductsAdapter;
import com.docgenerici.selfbox.android.contents.filters.FilterDialog;
import com.docgenerici.selfbox.android.contents.productlist.legenda.LegendaDialogFragment;
import com.docgenerici.selfbox.models.ProductDoc;
import com.docgenerici.selfbox.models.SelfBoxConstants;

import java.util.ArrayList;

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
    private FilterDialog filtersDialog;
    private LegendaDialogFragment legendaFragment;

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

        return view;

    }

    @OnClick(R.id.btFilter)
    void onSelectFilter(){
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
        adapter = new ProductsAdapter(getActivity().getApplicationContext(), productDocArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);
       // rvProduct.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.line_divider), getResources().getDimensionPixelSize(R.dimen.margin_divider_decotator)));
        rvProduct.setAdapter(adapter);

    }

   @Override
    public void openFilterDialog() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        filtersDialog = FilterDialog.createInstance();
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
    public void onItemClick(View view, int position) {

    }

    @OnClick(R.id.btLegenda)
    void onTapLegenda(){
        presenter.onSelectLegenda();
    }
}
