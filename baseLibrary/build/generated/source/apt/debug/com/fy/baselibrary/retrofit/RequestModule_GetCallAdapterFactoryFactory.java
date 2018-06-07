package com.fy.baselibrary.retrofit;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestModule_GetCallAdapterFactoryFactory
    implements Factory<RxJava2CallAdapterFactory> {
  private final RequestModule module;

  public RequestModule_GetCallAdapterFactoryFactory(RequestModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public RxJava2CallAdapterFactory get() {
    return Preconditions.checkNotNull(
        module.getCallAdapterFactory(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<RxJava2CallAdapterFactory> create(RequestModule module) {
    return new RequestModule_GetCallAdapterFactoryFactory(module);
  }

  /** Proxies {@link RequestModule#getCallAdapterFactory()}. */
  public static RxJava2CallAdapterFactory proxyGetCallAdapterFactory(RequestModule instance) {
    return instance.getCallAdapterFactory();
  }
}
