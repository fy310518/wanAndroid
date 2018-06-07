package com.fy.baselibrary.retrofit;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestModule_GetClientFactory implements Factory<OkHttpClient> {
  private final RequestModule module;

  private final Provider<HttpLoggingInterceptor> interceptorProvider;

  private final Provider<Interceptor> headerProvider;

  public RequestModule_GetClientFactory(
      RequestModule module,
      Provider<HttpLoggingInterceptor> interceptorProvider,
      Provider<Interceptor> headerProvider) {
    assert module != null;
    this.module = module;
    assert interceptorProvider != null;
    this.interceptorProvider = interceptorProvider;
    assert headerProvider != null;
    this.headerProvider = headerProvider;
  }

  @Override
  public OkHttpClient get() {
    return Preconditions.checkNotNull(
        module.getClient(interceptorProvider.get(), headerProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<OkHttpClient> create(
      RequestModule module,
      Provider<HttpLoggingInterceptor> interceptorProvider,
      Provider<Interceptor> headerProvider) {
    return new RequestModule_GetClientFactory(module, interceptorProvider, headerProvider);
  }

  /** Proxies {@link RequestModule#getClient(HttpLoggingInterceptor, Interceptor)}. */
  public static OkHttpClient proxyGetClient(
      RequestModule instance, HttpLoggingInterceptor interceptor, Interceptor header) {
    return instance.getClient(interceptor, header);
  }
}
