package com.docgenerici.selfbox.android.sync;


import com.docgenerici.selfbox.comm.ApiInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce @copyright
 */
@Module
public class SyncModule {

  @Provides
  SyncPresenter provideSyncPresenter( ApiInteractor apiInteractor) {
    return new SyncPresenterImpl(apiInteractor);
  }
}
