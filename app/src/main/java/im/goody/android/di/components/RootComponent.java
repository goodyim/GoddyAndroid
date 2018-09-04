package im.goody.android.di.components;


import dagger.Subcomponent;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.modules.RootModule;
import im.goody.android.root.RootActivity;
import im.goody.android.root.RootPresenter;
import im.goody.android.screens.about.AboutController;
import im.goody.android.screens.choose_help.ChooseHelpController;
import im.goody.android.screens.detail_post.DetailPostController;
import im.goody.android.screens.feedback.FeedBackController;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.intro.finish.IntroFinishController;
import im.goody.android.screens.greet.GreetController;
import im.goody.android.screens.intro.location.LocationNotificationsController;
import im.goody.android.screens.intro.resources.ResourcesController;
import im.goody.android.screens.login.LoginController;
import im.goody.android.screens.main.MainController;
import im.goody.android.screens.news.NewsController;
import im.goody.android.screens.near_events.NearEventsController;
import im.goody.android.screens.new_event.NewEventController;
import im.goody.android.screens.new_post.NewPostController;
import im.goody.android.screens.participants.ParticipantsController;
import im.goody.android.screens.photo.PhotoController;
import im.goody.android.screens.profile.ProfileController;
import im.goody.android.screens.profile.events.ProfileEventsController;
import im.goody.android.screens.register.RegisterController;
import im.goody.android.screens.setting.SettingController;

@Subcomponent(modules = RootModule.class)
@DaggerScope(RootActivity.class)
public interface RootComponent {
    void inject(RootActivity activity);
    void inject(RootPresenter presenter);

    NewsController.Component plusNews();

    LoginController.Component plusLogin();
    RegisterController.Component plusRegister();
    IntroController.Component plusIntro();
    NewPostController.Component plusNewPost();
    NewEventController.Component plusNewEvent();
    DetailPostController.Component plusDetailPost();
    PhotoController.Component plusPhoto();

    AboutController.Component plusAbout();
    SettingController.Component plusSetting();

    ProfileController.Component plusProfile();

    NearEventsController.Component plusNearEvents();

    FeedBackController.Component plusFeedback();

    MainController.Component plusMain();

    ProfileEventsController.Component plusProfileEvent();

    ChooseHelpController.Component plusChooseHelp();

    GreetController.Component plusGreet();

    ResourcesController.Component plusResources();

    LocationNotificationsController.Component plusIntroLocation();

    IntroFinishController.Component plusIntroFinish();

    ParticipantsController.Component plusParticipant();
}
