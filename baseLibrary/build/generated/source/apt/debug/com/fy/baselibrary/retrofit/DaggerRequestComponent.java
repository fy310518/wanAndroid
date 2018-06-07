package com.fy.baselibrary.retrofit;

import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerRequestComponent implements RequestComponent {
  private Provider<RxJava2CallAdapterFactory> getCallAdapterFactoryProvider;

  private Provider<GsonConverterFactory> getGsonConvertFactoryProvider;

  private Provider<HttpLoggingInterceptor> getResponseInterceptProvider;

  private Provider<Interceptor> getHeaderProvider;

  private Provider<OkHttpClient> getClientProvider;

  private Provider<Retrofit> getServiceProvider;

  private MembersInjector<RequestUtils> requestUtilsMembersInjector;

  private DaggerRequestComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static RequestComponent create() {
    return builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.getCallAdapterFactoryProvider =
        DoubleCheck.provider(
            RequestModule_GetCallAdapterFactoryFactory.create(builder.requestModule));

    this.getGsonConvertFactoryProvider =
        DoubleCheck.provider(
            RequestModule_GetGsonConvertFactoryFactory.create(builder.requestModule));

    this.getResponseInterceptProvider =
        DoubleCheck.provider(
            RequestModule_GetResponseInterceptFactory.create(builder.requestModule));

    this.getHeaderProvider =
        DoubleCheck.provider(RequestModule_GetHeaderFactory.create(builder.requestModule));

    this.getClientProvider =
        DoubleCheck.provider(
            RequestModule_GetClientFactory.create(
                builder.requestModule, getResponseInterceptProvider, getHeaderProvider));

    this.getServiceProvider =
        DoubleCheck.provider(
            RequestModule_GetServiceFactory.create(
                builder.requestModule,
                getCallAdapterFactoryProvider,
                getGsonConvertFactoryProvider,
                getClientProvider));

    this.requestUtilsMembersInjector = RequestUtils_MembersInjector.create(getServiceProvider);
  }

  @Override
  public void inJect(RequestUtils requestConn) {
    requestUtilsMembersInjector.injectMembers(requestConn);
  }

  public static final class Builder {
    private RequestModule requestModule;

    private Builder() {}

    public RequestComponent build() {
      if (requestModule == null) {
        this.requestModule = new RequestModule();
      }
      return new DaggerRequestComponent(this);
    }

    public Builder requestModule(RequestModule requestModule) {
      this.requestModule = Preconditions.checkNotNull(requestModule);
      return this;
    }
  }
}
