package im.goody.android.data.network;

import java.io.IOException;

import im.goody.android.data.local.PreferencesManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String AUTH_HEADER_NAME = "X-User-Token";

    public AuthInterceptor(PreferencesManager manager) {
        this.manager = manager;
    }

    private PreferencesManager manager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        if(manager.isTokenPresent()) {
            builder.addHeader(AUTH_HEADER_NAME, manager.getUserToken());
        }

        return chain.proceed(builder.build());
    }
}
