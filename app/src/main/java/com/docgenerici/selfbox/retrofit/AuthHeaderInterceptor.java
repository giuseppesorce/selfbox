package com.docgenerici.selfbox.retrofit;




import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author GiuseppeSorce
 */
@Singleton
final class AuthHeaderInterceptor implements Interceptor {




  @Inject
  public AuthHeaderInterceptor() {

  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder builder = request.newBuilder();

    return chain.proceed(builder.build());
  }
}
