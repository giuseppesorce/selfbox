package com.docgenerici.selfbox.retrofit;

import android.annotation.SuppressLint;


import com.docgenerici.selfbox.comm.storage.Environment;
import com.docgenerici.selfbox.debug.Dbg;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This module provides a modern REST client library init based on retrofit,
 * okhttp and gson. It will log the http calls in debug builds and use
 * support rxjava observables and singles.
 */
@Module
public class RetrofitModule {

  private final File cacheDir;
  private final long cacheSize;


  public RetrofitModule() {
    this.cacheDir = null;
    this.cacheSize = 0;
  }

  /**
   * Init the retrofit module with HTTP caching.
   *
   * @param cacheDir  the directory to use for the HTTP cache
   * @param cacheSize the size of the HTTP cache
   */
  public RetrofitModule(File cacheDir, long cacheSize) {
    this.cacheDir = cacheDir;
    this.cacheSize = cacheSize;
  }



  /**
   * Provides an OkHttpClient object.
   *
   * @return a Call.Factory OkHttpClient object
   */
  @Provides
  @Singleton
  Call.Factory provideOkHttpClient(AuthHeaderInterceptor authHeaderInterceptor) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.sslSocketFactory(createSSLSocketFactory());
    builder.hostnameVerifier(new TrustAllHostnameVerifier());
    if (cacheDir != null && cacheSize > 0) {
      Cache cache = new Cache(cacheDir, cacheSize);
      builder.cache(cache);
    }

      HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(     new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
        Dbg.p("SERVER_RESPONSE: "+ message, "server");
        }
      });

      httpLoggingInterceptor
          .setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.interceptors().add(httpLoggingInterceptor);


    builder.interceptors().add(authHeaderInterceptor);
    return builder.build();
  }

  @SuppressLint("TrulyRandom")
  private static SSLSocketFactory createSSLSocketFactory() {

    SSLSocketFactory sSLSocketFactory = null;

    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[]{new TrustAllManager()},
          new SecureRandom());
      sSLSocketFactory = sc.getSocketFactory();
    } catch (Exception e) {
    }

    return sSLSocketFactory;
  }
  private static class TrustAllHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  private static class TrustAllManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)

        throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }
  }

  /**
   * Provides a Retrofit object configured with:
   *
   * @param factory the OkHttp Client factory to use.
   * @return the Retrofit client.
   */
  @Provides
  @Singleton
  Retrofit provideRetrofit(Call.Factory factory , Environment environment) {


    return new Retrofit.Builder()
            .baseUrl(environment.getBaseUrl())
            .callFactory(factory)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
  }
}
