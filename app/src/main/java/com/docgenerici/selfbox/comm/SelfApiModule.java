package com.docgenerici.selfbox.comm;

import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.retrofit.SelfBoxUserApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @uthor giuseppesorce
 */
@Module
public class SelfApiModule {


    @Provides
    @Singleton
    SelfBoxUserApi provideRetrofitUserApi(Retrofit retrofit) {
        return retrofit.create(SelfBoxUserApi.class);
    }

    @Provides
    @Singleton
    SelfApi provideUserApi(SelfBoxUserApi retrofitUserApi, Environment environment) {
        return new SelfApiImpl(retrofitUserApi, environment);
    }


    @Provides
    @Singleton
    CommApi provideLoginApi(SelfApi api) {
        return api;
    }
}
