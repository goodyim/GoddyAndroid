package im.goody.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Provides;
import im.goody.android.di.components.DataComponent;
import im.goody.android.di.components.RootComponent;
import im.goody.android.di.modules.DataModule;
import im.goody.android.di.modules.RootModule;

public class App extends MultiDexApplication {
    private static Context appContext;
    private static DataComponent dataComponent;
    private static RootComponent rootComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
        initDaggerComponents();

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE))
                .build();

        picasso.setLoggingEnabled(true);

        Picasso.setSingletonInstance(picasso);
    }

    public static Context getAppContext() {
        return appContext;
    }

    //region ================= DI =================

    public static DataComponent getDataComponent() {
        return dataComponent;
    }

    public static RootComponent getRootComponent() {
        return rootComponent;
    }

    private void initDaggerComponents() {
        AppComponent appComponent = DaggerApp_AppComponent.builder()
                .appModule(new AppModule(appContext))
                .build();
        dataComponent = appComponent.plus(new DataModule());
        rootComponent = dataComponent.plus(new RootModule());
    }

    @dagger.Module
    class AppModule {
        private Context context;

        AppModule(Context context) {
            this.context = context;
        }

        @Provides
        Context provideContext () {
            return context;
        }
    }

    @dagger.Component(modules = AppModule.class)
    interface AppComponent {
        Context getContext();
        DataComponent plus(DataModule module);
    }

    //endregion
}
