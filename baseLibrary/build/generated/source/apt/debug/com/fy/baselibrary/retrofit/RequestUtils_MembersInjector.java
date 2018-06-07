package com.fy.baselibrary.retrofit;

import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RequestUtils_MembersInjector implements MembersInjector<RequestUtils> {
  private final Provider<Retrofit> netRetrofitProvider;

  public RequestUtils_MembersInjector(Provider<Retrofit> netRetrofitProvider) {
    assert netRetrofitProvider != null;
    this.netRetrofitProvider = netRetrofitProvider;
  }

  public static MembersInjector<RequestUtils> create(Provider<Retrofit> netRetrofitProvider) {
    return new RequestUtils_MembersInjector(netRetrofitProvider);
  }

  @Override
  public void injectMembers(RequestUtils instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.netRetrofit = netRetrofitProvider.get();
  }

  public static void injectNetRetrofit(
      RequestUtils instance, Provider<Retrofit> netRetrofitProvider) {
    instance.netRetrofit = netRetrofitProvider.get();
  }
}
