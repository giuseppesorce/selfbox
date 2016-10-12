package com.docgenerici.selfbox;

import android.content.Context;
import android.content.res.Resources;

import com.docgenerici.selfbox.android.AppModule;
import com.docgenerici.selfbox.retrofit.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Giuseppe Sorce @copyright AmasingApps 2016.
 */
@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class,
   })
public interface AppComponent {

   Context context();
   Resources resources();
}
