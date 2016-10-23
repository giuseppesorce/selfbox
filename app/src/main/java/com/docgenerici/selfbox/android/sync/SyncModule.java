package com.docgenerici.selfbox.android.sync;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce @copyright
 */
@Module
public class SyncModule {

  @Provides
  @Singleton
  SyncPresenter provideSyncPresenter() {
    return new SyncPresenterImpl();
  }
}
