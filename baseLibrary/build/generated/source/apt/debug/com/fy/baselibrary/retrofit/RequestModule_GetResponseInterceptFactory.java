package com.fy.baselibrary.retrofit;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import okhttp3.logging.HttpLoggingInterceptor;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestModule_GetResponseInterceptFactory
    implements Factory<HttpLoggingInterceptor> {
  private final RequestModule module;

  public RequestModule_GetResponseInterceptFactory(RequestModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public HttpLoggingInterceptor get() {
    return Preconditions.checkNotNull(
        module.getResponseIntercept(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<HttpLoggingInterceptor> create(RequestModule module) {
    return new RequestModule_GetResponseInterceptFactory(module);
  }

  /** Proxies {@link RequestModule#getResponseIntercept()}. */
  public static HttpLoggingInterceptor proxyGetResponseIntercept(RequestModule instance) {
    return instance.getResponseIntercept();
  }
}
