package com.docgenerici.selfbox.android.start;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.docgenerici.selfbox.comm.ApiInteractor;

/**
 * @author Giuseppe Sorce
 */
@Module
public class StartModule {

  @Provides
  @Singleton
  StartPresenter provideStartPresenter(ApiInteractor loginInteractor) {
    return new StartPresenterImpl(loginInteractor);
  }
}
