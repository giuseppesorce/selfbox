package com.docgenerici.selfbox.android.contents.productlist;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.docgenerici.selfbox.R;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.android.adapters.OnSelectProdcutDetail;
import com.docgenerici.selfbox.android.adapters.ProductsAdapter;
import com.docgenerici.selfbox.android.contents.filters.FilterProductDialog;
import com.docgenerici.selfbox.android.contents.productlist.legenda.LegendaDialogFragment;
import com.docgenerici.selfbox.android.pdf.PdfActivity;
import com.docgenerici.selfbox.android.utils.SelfBoxUtils;
import com.docgenerici.selfbox.debug.Dbg;
import com.docgenerici.selfbox.models.ContentDoc;
import com.docgenerici.selfbox.models.ContentShared;
import com.docgenerici.selfbox.models.FilterProduct;
import com.docgenerici.selfbox.models.ProductDoc;
import com.docgenerici.selfbox.config.SelfBoxConstants;
import com.docgenerici.selfbox.models.products.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.category;

/**
 * @author Giuseppe Sorce
 */

public class ProductListFragment extends Fragment implements ProductsListPresenter.PListView, OnSelectProdcutDetail, OnSelectFilter, TextWatcher {


    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    @BindView(R.id.rlPrice)
    RelativeLayout rlPrice;
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
    private ArrayList<ProductDoc> productDocArrayList = new ArrayList<>();
    private String[] colors;
    private HashMap<String, String> categorieColors = new HashMap<>();
    private boolean training;
    private String categorySelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_products_list, container, false);
        ButterKnife.bind(this, view);

        colors = getResources().getStringArray(R.array.categorie_color);
        presenter = SelfBoxApplicationImpl.appComponent.productsListPresenter();
        presenter.setView(this);
        presenter.setup();
        if (getArguments() != null) {
            training = getArguments().getBoolean("training", false);
            categorySelect = getArguments().getString("category");
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
        presenter.selectTerapeutica();

        etSearch.addTextChangedListener(this);
        return view;

    }

    @OnClick(R.id.rlPrice)
    void onSelectMenuPrice() {
        File file = new File(getActivity().getExternalFilesDir("contents"), "listinoprezzi.pdf");
        if (file.exists()) {
            String path = file.getAbsolutePath();
            Dbg.p(" file.getAbsoluteFile(): " + file.getAbsoluteFile());
//            if (path.contains("file://")) {
//                path = path.replace("file://", "");
//            }
            Intent intent = new Intent(getActivity(), PdfActivity.class);
            intent.putExtra("path", path);
            intent.putExtra("canShare", false);
            getActivity().startActivity(intent);
        }
    }


    @OnClick(R.id.llAZ)
    void onSelectAZ() {
        presenter.selectAZ();
    }

    @OnClick(R.id.llDate)
    void onSelectDate() {
        presenter.selectTerapeutica();
    }

    @OnClick(R.id.btFilter)
    void onSelectFilter() {
        presenter.onSelectFilter();
    }

    @Override
    public void setup() {
        productDocArrayList = new ArrayList<>();

        adapter = new ProductsAdapter(getActivity(), productDocArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProduct.setLayoutManager(linearLayoutManager);
        rvProduct.setAdapter(adapter);
    }

    @Override
    public void openFilterDialog() {
        FragmentTransaction ft = getFragmentManager()
                .beginTransaction();
        filtersDialog = FilterProductDialog.createInstance(presenter.getCategoriesFilter());
        filtersDialog.setListener(this);
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

        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        ArrayList<String> categories = presenter.getCategoriesFilter();
        productDocArrayList.clear();


        RealmResults<Product> products = realm.where(Product.class).findAllSorted("nome", Sort.ASCENDING);
        for (int j = 0; j < products.size(); j++) {
            Product product = products.get(j);

            productDocArrayList.add(new ProductDoc(product.getAic(), SelfBoxConstants.TypeProductRow.PRODUCT, product.getNome().toUpperCase(), product.denominazione_it, product.classeSnn, product.noFCDL, Color.parseColor(categorieColors.get(product.categoria_farmacologica)), product.getScheda().getUri(), product.rcp, product.getUriPdf(), product.getScheda().uriPdf));
        }

        adapter.notifyDataSetChanged();
        etSearch.getText().clear();
    }

    @Override
    public void showSelectTerapeutica() {
        llAZ.getBackground().clearColorFilter();
        llDate.getBackground().setColorFilter(grey_filter, PorterDuff.Mode.MULTIPLY);

        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        ArrayList<String> categories = presenter.getCategoriesFilter();
        productDocArrayList.clear();
        categorieColors.clear();
        for (int i = 0; i < categories.size(); i++) {
            String categoria = categories.get(i);

            categorieColors.put(categoria, colors[i]);

            productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.HEADER, categoria.toUpperCase()));
            RealmResults<Product> products = realm.where(Product.class).equalTo("categoria_farmacologica", categoria).findAll();
            for (int j = 0; j < products.size(); j++) {

                Product product = products.get(j);
                ProductDoc productDoc=  new ProductDoc(product.getAic(), SelfBoxConstants.TypeProductRow.PRODUCT, product.getNome().toUpperCase(), product.denominazione_it, product.classeSnn, product.noFCDL, Color.parseColor(SelfBoxUtils.getCategoryColor(product.categoria_farmacologica)), product.getScheda().getUri(), product.rcp, product.getUriPdf(), product.getScheda().uriPdf);
                productDocArrayList.add(productDoc);

            }
        }
        adapter.notifyDataSetChanged();
        etSearch.getText().clear();
    }


    @OnClick(R.id.btLegenda)
    void onTapLegenda() {
        presenter.onSelectLegenda();
    }

    public static ProductListFragment createInstance(boolean training, String category) {
        ProductListFragment frag = new ProductListFragment();
        Bundle init = new Bundle();
        init.putBoolean("training", training);
        init.putString("category", category);
        frag.setArguments(init);
        return frag;
    }

    @Override
    public void onSelectScheda(ProductDoc product) {
        String url = product.getUriSchedaPdf();
        Dbg.p("onSelectScheda: " + url);
        Intent intent = new Intent(getActivity(), PdfActivity.class);
        intent.putExtra("type", "product");
        intent.putExtra("contentSelect", new ContentDoc());
        intent.putExtra("canShare", getCanShare());
        intent.putExtra("training",training);
        intent.putExtra("contentSelect", new ContentShared(String.valueOf("scheda_" + product.getAic()), product.getTitle(), "product", SelfBoxConstants.pathProduct + product.getScheda()));
        intent.putExtra("path", url);
        getActivity().startActivity(intent);
    }

    private boolean getCanShare() {
        return !training && !categorySelect.equalsIgnoreCase("isf");
    }

    @Override
    public void onSelectRpc(ProductDoc product) {
        String url = product.getUriPdf();
        Dbg.p("onSelectRpc: " + url);
        Intent intent = new Intent(getActivity(), PdfActivity.class);
        intent.putExtra("type", "product");
        intent.putExtra("training",training);
        intent.putExtra("canShare", getCanShare());
        intent.putExtra("contentSelect", new ContentShared(String.valueOf("rpc_" + product.getAic()), product.getTitle(), "product", SelfBoxConstants.pathProduct + product.getRpc()));
        intent.putExtra("path", url);
        getActivity().startActivity(intent);

    }

    @Override
    public void onChangeFilter(ArrayList<FilterProduct> filtersProducts) {
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        productDocArrayList.clear();
        for (int i = 0; i < filtersProducts.size(); i++) {
            String categoria = filtersProducts.get(i).name;
            if (filtersProducts.get(i).select) {
                productDocArrayList.add(new ProductDoc(SelfBoxConstants.TypeProductRow.HEADER, categoria.toUpperCase()));
                RealmResults<Product> products = realm.where(Product.class).equalTo("categoria_farmacologica", categoria).findAll();
                for (int j = 0; j < products.size(); j++) {
                    Product product = products.get(j);
                    Dbg.p("PRODOTTO: " + product.getUriPdf());

                    productDocArrayList.add(new ProductDoc(product.getAic(), SelfBoxConstants.TypeProductRow.PRODUCT, product.getNome().toUpperCase(), product.denominazione_it, product.classeSnn, product.noFCDL, Color.parseColor(SelfBoxUtils.getCategoryColor(product.categoria_farmacologica)), product.getScheda().getUri(), product.rcp, product.getUriPdf(), product.getScheda().uriPdf));
                }
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String filterText = etSearch.getText().toString().toLowerCase();
        searchText(filterText);

    }

    private void searchText(String filterText) {
        if (filterText.length() > 2) {
            final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
            productDocArrayList.clear();


            RealmResults<Product> products = realm.where(Product.class).findAll();
            for (int j = 0; j < products.size(); j++) {
                Product product = products.get(j);
                if (product.getNome().toLowerCase().contains(filterText.toLowerCase())) {
                    productDocArrayList.add(new ProductDoc(product.getAic(), SelfBoxConstants.TypeProductRow.PRODUCT, product.getNome().toUpperCase(), product.denominazione_it, product.classeSnn, product.noFCDL, Color.parseColor(SelfBoxUtils.getCategoryColor(product.categoria_farmacologica)), product.getScheda().getUri(), product.rcp, product.getUriPdf(), product.getScheda().uriPdf));

                }
            }

            adapter.notifyDataSetChanged();
        } else {
            presenter.selectLastFilter();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
