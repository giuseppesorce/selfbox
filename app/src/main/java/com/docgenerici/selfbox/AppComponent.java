package com.docgenerici.selfbox;

import android.content.Context;
import android.content.res.Resources;

import com.docgenerici.selfbox.android.AppModule;
import com.docgenerici.selfbox.android.contents.MainContentPresenter;
import com.docgenerici.selfbox.android.contents.MaintContentModule;
import com.docgenerici.selfbox.android.contents.contentslist.ContentListModule;
import com.docgenerici.selfbox.android.contents.contentslist.ContentListPresenter;
import com.docgenerici.selfbox.android.home.HomeModule;
import com.docgenerici.selfbox.android.home.HomePresenter;
import com.docgenerici.selfbox.android.start.StartModule;
import com.docgenerici.selfbox.android.start.StartPresenter;
import com.docgenerici.selfbox.retrofit.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

/**
 * @author Giuseppe Sorce
 */
@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class, StartModule.class, HomeModule.class, MaintContentModule.class, ContentListModule.class
})
public interface AppComponent {

    Context context();

    Resources resources();

    Realm realm();

    StartPresenter startPresenter();

    HomePresenter homePresenter();

    MainContentPresenter mainContentPresenter();

    ContentListPresenter contentListPresenter();
}
