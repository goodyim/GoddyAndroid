package im.goody.android.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import im.goody.android.data.IRepository;
import im.goody.android.data.Repository;

@Module
public class DataModule {
    @Provides
    @Singleton
    IRepository provideRepository() {
        return new Repository();
    }
}
