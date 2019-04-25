package im.goody.android.di.modules;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mklimek.sslutilsandroid.SslUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;

import dagger.Module;
import dagger.Provides;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.data.network.AuthInterceptor;
import im.goody.android.data.network.RestService;
import im.goody.android.utils.AppConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context, AuthInterceptor interceptor) {
        return createClient(context, interceptor);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttp) {
        return createRetrofit(okHttp);
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

    @Provides
    @Singleton
    AuthInterceptor provideAuthInterceptor(PreferencesManager manager) {
        return new AuthInterceptor(manager);
    }

    private OkHttpClient createClient(Context context, AuthInterceptor interceptor) {
        SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "goody.cer");
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(AppConfig.MAX_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(AppConfig.MAX_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslContext.getSocketFactory())
                .writeTimeout(AppConfig.MAX_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private Retrofit createRetrofit(OkHttpClient okHttp) {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(createConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(okHttp)
                .build();
    }

    private Converter.Factory createConverterFactory() {
        return JacksonConverterFactory.create(new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, true));
    }
}