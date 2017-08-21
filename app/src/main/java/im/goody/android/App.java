package im.goody.android;

import android.app.Application;
import android.content.Context;

import dagger.Provides;
import im.goody.android.di.components.DataComponent;
import im.goody.android.di.components.RootComponent;
import im.goody.android.di.modules.DataModule;
import im.goody.android.di.modules.RootModule;

public class App extends Application {
    private static Context appContext;
    private static RootComponent rootComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        initDaggerComponents();
    }

    public static RootComponent getRootComponent() {
        return rootComponent;
    }

    public static Context getAppContext() {
        return appContext;
    }

    //region ================= DI =================

    private void initDaggerComponents() {
        AppComponent appComponent = DaggerApp_AppComponent.builder()
                .appModule(new AppModule(appContext))
                .build();
        DataComponent dataComponent = appComponent.plus(new DataModule());
        rootComponent = dataComponent.plus(new RootModule());
    }

    @dagger.Module
    public class AppModule {
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
    public interface AppComponent {
        Context getContext();
        DataComponent plus(DataModule module);
    }

    //endregion
}
