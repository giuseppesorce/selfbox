package com.docgenerici.selfbox.android.contents.filters;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.docgenerici.selfbox.models.FilterProduct;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.docgenerici.selfbox.R.id.rvProduct;

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



    public static FilterProductDialog createInstance() {
        FilterProductDialog frag = new FilterProductDialog();
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
        View root = inflater.inflate(R.layout.dialog_filters_product, container,
                false);
        ButterKnife.bind(this, root);
        getDialog().getWindow().addFlags(STYLE_NO_TITLE);
        createLegendFilter();
        return root;
    }

    private void createLegendFilter() {

        ArrayList<FilterProduct> filterProduct = new ArrayList<>();
        filterProduct.add(new FilterProduct(Color.parseColor("#fff05c"), "Antiinfettivi"));
        filterProduct.add(new FilterProduct(Color.parseColor("#a77c40"), "Antineoplastici"));
        filterProduct.add(new FilterProduct(Color.parseColor("#be0f0f"), "Cardiovascolari"));
        filterProduct.add(new FilterProduct(Color.parseColor("#ada5d0"), "Dermatologici"));
        filterProduct.add(new FilterProduct(Color.parseColor("#f8ab01"), "Ematologici"));
        filterProduct.add(new FilterProduct(Color.parseColor("#67ba8d"), "Gastoenterici e metabolici"));
        filterProduct.add(new FilterProduct(Color.parseColor("#f37111"), "Genito-urinari"));
        filterProduct.add(new FilterProduct(Color.parseColor("#152073"), "Neurologici"));
        filterProduct.add(new FilterProduct(Color.parseColor("#008f76"), "Organi di senso"));
        filterProduct.add(new FilterProduct(Color.parseColor("#ac308b"), "Preparatori ormonali sistemici.esclusi ormoni sessuali ed insuline"));
        filterProduct.add(new FilterProduct(Color.parseColor("#ada5d0"), "Respiratori"));
        filterProduct.add(new FilterProduct(Color.parseColor("#03a5b0"), "Sistema Muscolo-scheletrico"));


        FilterAdapter adapter = new FilterAdapter(getActivity().getApplicationContext(), filterProduct, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
       // rvProduct.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), getResources().getDrawable(R.drawable.line_divider), getResources().getDimensionPixelSize(R.dimen.margin_divider_decotator)));
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