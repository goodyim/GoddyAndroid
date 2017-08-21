package im.goody.android.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import im.goody.android.data.local.PreferencesManager;

@Module
public class LocalModule {
    @Provides
    @Singleton
    PreferencesManager providePreferencesManager(Context context) {
        return new PreferencesManager(context);
    }
}
