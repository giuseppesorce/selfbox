package com.docgenerici.selfbox.android.start;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class StartModule {

  @Provides
  @Singleton
  StartPresenter provideStartPresenter() {
    return new StartPresenterImpl();
  }
}
