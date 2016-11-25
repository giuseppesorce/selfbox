package com.docgenerici.selfbox.comm;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class LoginModule {

  @Provides
  @Singleton
  LoginInteractor provideLoginInteractor(LoginApi api) {
    return new LoginInteractorImpl(api);
  }

}
