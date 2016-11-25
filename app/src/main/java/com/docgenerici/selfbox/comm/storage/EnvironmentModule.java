package com.docgenerici.selfbox.comm.storage;



import android.text.TextUtils;

import com.docgenerici.selfbox.BuildConfig;

import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class EnvironmentModule {


  private String baseUrl;


  public EnvironmentModule(String baseUrl) {
    this.baseUrl = baseUrl;

  }



  @Provides
  @Singleton
  public Environment provideEnvironment() {
    if (baseUrl == null || TextUtils.isEmpty(baseUrl)) {
      baseUrl = BuildConfig.BASE_URL;
    }


    return new Environment(baseUrl);
  }
}
