package im.goody.android.di.components;


import dagger.Subcomponent;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.modules.RootModule;
import im.goody.android.root.RootActivity;
import im.goody.android.root.RootPresenter;
import im.goody.android.screens.about.AboutController;
import im.goody.android.screens.detail_post.DetailPostController;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.login.LoginController;
import im.goody.android.screens.main.MainController;
import im.goody.android.screens.new_event.NewEventController;
import im.goody.android.screens.new_post.NewPostController;
import im.goody.android.screens.register.RegisterController;
import im.goody.android.screens.setting.SettingController;

@Subcomponent(modules = RootModule.class)
@DaggerScope(RootActivity.class)
public interface RootComponent {
    void inject(RootActivity activity);
    void inject(RootPresenter presenter);

    MainController.Component plusMain();
    LoginController.Component plusLogin();
    RegisterController.Component plusRegister();
    IntroController.Component plusIntro();
    NewPostController.Component plusNewPost();
    NewEventController.Component plusNewEvent();
    DetailPostController.Component plusDetailPost();

    AboutController.Component plusAbout();
    SettingController.Component plusSetting();
}
