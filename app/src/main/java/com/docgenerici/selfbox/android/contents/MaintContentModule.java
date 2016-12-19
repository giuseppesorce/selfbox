package com.docgenerici.selfbox.android.contents;


import com.docgenerici.selfbox.comm.ApiInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce @copyright Amasingapps srl  2016.
 */
@Module
public class MaintContentModule {

  @Provides
  @Singleton
  MainContentPresenter provideMainContentPresenter(ApiInteractor apiInteractor) {
    return new MainContentPresenterImpl(apiInteractor);
  }
}
