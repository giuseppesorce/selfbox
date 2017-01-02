package com.docgenerici.selfbox.android;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Giuseppe Sorce @copyright AmasingApps srl  2016.
 */

@Module
public class AppModule {

    private final Context context;
    //private MyRealmMigration myMigration;

    public AppModule(Context context) {
        this.context = context;
      //  this.myMigration= new MyRealmMigration();
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return context.getResources();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager() {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    @Provides
    @Singleton
    Gson proviceGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    AlarmManager provideAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    @Singleton
    DownloadManager provideDownloadManager() {
        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Provides
    @Singleton
    ContentResolver provideContentResolver() {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    InputMethodManager provideInputMethodManager() {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    @Singleton
    SMApplication provideSMApplication() {
        return (SMApplication) context;
    }

    @Provides
    Realm provideRealm() {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
//                .schemaVersion(1)
//                .migration(myMigration)
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        return Realm.getDefaultInstance();
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        return Realm.getDefaultInstance();
    }




}
