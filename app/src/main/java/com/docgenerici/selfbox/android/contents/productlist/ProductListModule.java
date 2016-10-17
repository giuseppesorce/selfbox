package com.docgenerici.selfbox.android.contents.productlist;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class ProductListModule {

  @Provides
  @Singleton
  ProductsListPresenter provideProductsListPresenter() {
    return new ProductsListPresenterImpl();
  }
}
