package com.docgenerici.selfbox;

import android.content.Context;
import android.content.res.Resources;

import com.docgenerici.selfbox.android.AppModule;
import com.docgenerici.selfbox.android.contents.MainContentPresenter;
import com.docgenerici.selfbox.android.contents.MaintContentModule;
import com.docgenerici.selfbox.android.contents.contentslist.ContentListModule;
import com.docgenerici.selfbox.android.contents.contentslist.ContentListPresenter;
import com.docgenerici.selfbox.android.contents.productlist.ProductListModule;
import com.docgenerici.selfbox.android.contents.productlist.ProductsListPresenter;
import com.docgenerici.selfbox.android.home.HomeModule;
import com.docgenerici.selfbox.android.home.HomePresenter;
import com.docgenerici.selfbox.android.start.StartModule;
import com.docgenerici.selfbox.android.start.StartPresenter;
import com.docgenerici.selfbox.android.sync.SyncModule;
import com.docgenerici.selfbox.android.sync.SyncPresenter;
import com.docgenerici.selfbox.comm.LoginModule;
import com.docgenerici.selfbox.comm.SelfApiModule;
import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.comm.storage.EnvironmentModule;
import com.docgenerici.selfbox.retrofit.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

/**
 * @author Giuseppe Sorce
 */
@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class,SyncModule.class, SelfApiModule.class,EnvironmentModule.class,
        StartModule.class, HomeModule.class, MaintContentModule.class, ContentListModule.class, ProductListModule.class, LoginModule.class
})
public interface AppComponent {

    Context context();

    Resources resources();

    Realm realm();

    StartPresenter startPresenter();

    HomePresenter homePresenter();

    MainContentPresenter mainContentPresenter();

    ContentListPresenter contentListPresenter();
    ProductsListPresenter productsListPresenter();
    SyncPresenter syncPresenter();
    Environment environment();
}
