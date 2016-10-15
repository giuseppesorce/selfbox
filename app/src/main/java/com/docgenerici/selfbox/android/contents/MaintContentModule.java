package com.docgenerici.selfbox.android.contents;


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
  MainContentPresenter provideMainContentPresenter() {
    return new MainContentPresenterImpl();
  }
}
