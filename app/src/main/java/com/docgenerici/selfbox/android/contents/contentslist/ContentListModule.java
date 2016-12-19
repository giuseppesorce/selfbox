package com.docgenerici.selfbox.android.contents.contentslist;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Giuseppe Sorce
 */
@Module
public class ContentListModule {

  @Provides
  @Singleton
  ContentListPresenter provideContentListPresenter() {
    return new ContentListPresenterImpl();
  }
}
