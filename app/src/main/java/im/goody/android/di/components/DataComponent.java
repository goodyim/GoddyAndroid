package im.goody.android.di.components;

import javax.inject.Singleton;

import dagger.Subcomponent;
import im.goody.android.data.Repository;
import im.goody.android.di.modules.DataModule;
import im.goody.android.di.modules.LocalModule;
import im.goody.android.di.modules.NetworkModule;
import im.goody.android.di.modules.RootModule;

@Subcomponent(modules = {LocalModule.class, NetworkModule.class, DataModule.class})
@Singleton
public interface DataComponent {
    void inject(Repository repository);
    RootComponent plus(RootModule module);
}
