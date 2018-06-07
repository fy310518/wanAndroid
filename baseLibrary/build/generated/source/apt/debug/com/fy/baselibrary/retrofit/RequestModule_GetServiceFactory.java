package com.fy.baselibrary.retrofit;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestModule_GetServiceFactory implements Factory<Retrofit> {
  private final RequestModule module;

  private final Provider<RxJava2CallAdapterFactory> callAdapterFactoryProvider;

  private final Provider<GsonConverterFactory> gsonConverterFactoryProvider;

  private final Provider<OkHttpClient> clientProvider;

  public RequestModule_GetServiceFactory(
      RequestModule module,
      Provider<RxJava2CallAdapterFactory> callAdapterFactoryProvider,
      Provider<GsonConverterFactory> gsonConverterFactoryProvider,
      Provider<OkHttpClient> clientProvider) {
    assert module != null;
    this.module = module;
    assert callAdapterFactoryProvider != null;
    this.callAdapterFactoryProvider = callAdapterFactoryProvider;
    assert gsonConverterFactoryProvider != null;
    this.gsonConverterFactoryProvider = gsonConverterFactoryProvider;
    assert clientProvider != null;
    this.clientProvider = clientProvider;
  }

  @Override
  public Retrofit get() {
    return Preconditions.checkNotNull(
        module.getService(
            callAdapterFactoryProvider.get(),
            gsonConverterFactoryProvider.get(),
            clientProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Retrofit> create(
      RequestModule module,
      Provider<RxJava2CallAdapterFactory> callAdapterFactoryProvider,
      Provider<GsonConverterFactory> gsonConverterFactoryProvider,
      Provider<OkHttpClient> clientProvider) {
    return new RequestModule_GetServiceFactory(
        module, callAdapterFactoryProvider, gsonConverterFactoryProvider, clientProvider);
  }

  /**
   * Proxies {@link RequestModule#getService(RxJava2CallAdapterFactory, GsonConverterFactory,
   * OkHttpClient)}.
   */
  public static Retrofit proxyGetService(
      RequestModule instance,
      RxJava2CallAdapterFactory callAdapterFactory,
      GsonConverterFactory gsonConverterFactory,
      OkHttpClient client) {
    return instance.getService(callAdapterFactory, gsonConverterFactory, client);
  }
}
