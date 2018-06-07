package com.fy.baselibrary.retrofit;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import okhttp3.Interceptor;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestModule_GetHeaderFactory implements Factory<Interceptor> {
  private final RequestModule module;

  public RequestModule_GetHeaderFactory(RequestModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public Interceptor get() {
    return Preconditions.checkNotNull(
        module.getHeader(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Interceptor> create(RequestModule module) {
    return new RequestModule_GetHeaderFactory(module);
  }

  /** Proxies {@link RequestModule#getHeader()}. */
  public static Interceptor proxyGetHeader(RequestModule instance) {
    return instance.getHeader();
  }
}
