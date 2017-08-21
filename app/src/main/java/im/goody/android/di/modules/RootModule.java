package im.goody.android.di.modules;

import dagger.Module;
import dagger.Provides;
import im.goody.android.di.DaggerScope;
import im.goody.android.root.IRootPresenter;
import im.goody.android.root.RootActivity;
import im.goody.android.root.RootPresenter;

@Module
public class RootModule {
    @Provides
    @DaggerScope(RootActivity.class)
    IRootPresenter provideRootPresenter() {
        return new RootPresenter();
    }
}
