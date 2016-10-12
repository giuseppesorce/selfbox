package com.docgenerici.selfbox.android;

import android.app.Application;


import com.docgenerici.selfbox.AppComponent;
import com.docgenerici.selfbox.DaggerAppComponent;
import com.docgenerici.selfbox.retrofit.RetrofitModule;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Giuseppe Sorce @copyright
 */
public class BoxApplicationImpl extends Application implements SMApplication {


  public static AppComponent appComponent;
  public static BoxApplicationImpl instannce;


  @Override
  public void onCreate() {
    super.onCreate();



    instannce= this;
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).
        retrofitModule(new RetrofitModule(new File(getCacheDir() + "/cache"),
            10 * 1024 * 1024))
        .build();
  }

  private void initRealmConfiguration() {
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
            .deleteRealmIfMigrationNeeded()
            .build();
    Realm.setDefaultConfiguration(realmConfiguration);
  }




}
