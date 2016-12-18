package com.docgenerici.selfbox.android.contents.productlist;

import com.docgenerici.selfbox.BaseView;
import com.docgenerici.selfbox.android.SelfBoxApplicationImpl;
import com.docgenerici.selfbox.models.products.Product;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Giuseppe Sorce
 */

public class ProductsListPresenterImpl implements ProductsListPresenter {
    private PListView view;
    private final int FILTER_BY_TERAP = 1;
    private final int FILTER_BY_AZ= 2;
    private  int FILTER_NOW= FILTER_BY_AZ;
    private ArrayList<String> categoriesList;

    @Override
    public void setView(BaseView view) {
        if (!(view instanceof PListView)) {
            throw new IllegalArgumentException("View must extend ProductsListPresenter.View");
        }
        this.view = (PListView) view;
    }

    @Override
    public void setup() {
        categoriesList= new ArrayList<String>();
        final Realm realm = SelfBoxApplicationImpl.appComponent.realm();
        RealmResults<Product> products2 = realm.where(Product.class).findAllSorted("categoria_farmacologica", Sort.ASCENDING).distinct("categoria_farmacologica");
        for (int i = 0; i < products2.size(); i++) {
           categoriesList.add(products2.get(i).categoria_farmacologica);
        }
        view.setup();
    }

    @Override
    public void onSelectFilter() {
        view.openFilterDialog();
    }

    @Override
    public void onSelectLegenda() {
        view.showLegenda();
    }

    @Override
    public void selectAZ() {
        FILTER_NOW= FILTER_BY_AZ;
        view.showSelectAz();
    }

    @Override
    public void selectTerapeutica() {
        FILTER_NOW= FILTER_BY_TERAP;
        view.showSelectTerapeutica();
    }

    @Override
    public ArrayList<String> getCategoriesFilter() {
        return categoriesList;
    }

    @Override
    public void selectLastFilter() {

        if(FILTER_NOW == FILTER_BY_TERAP){
            view.showSelectTerapeutica();
        }else{
            view.showSelectAz();
        }

    }
}
