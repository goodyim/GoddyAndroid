package im.goody.android.di.components;


import dagger.Subcomponent;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.modules.RootModule;
import im.goody.android.root.RootActivity;
import im.goody.android.root.RootPresenter;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.main.MainController;

@Subcomponent(modules = RootModule.class)
@DaggerScope(RootActivity.class)
public interface RootComponent {
    void inject(RootActivity activity);
    void inject(RootPresenter presenter);

    MainController.Component plusMain();
    IntroController.Component plusIntro();
}
