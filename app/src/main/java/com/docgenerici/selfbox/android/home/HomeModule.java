package com.docgenerici.selfbox.android.home;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce @copyright Amasingapps srl  2016.
 */
@Module
public class HomeModule {

  @Provides
  @Singleton
  HomePresenter provideHomePresenter() {
    return new HomePresenterImpl();
  }
}
