package com.docgenerici.selfbox.comm;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class ApiModule {

  @Provides
  @Singleton
  ApiInteractor provideLoginInteractor(CommApi api) {
    return new ApiInteractorImpl(api);
  }

}
