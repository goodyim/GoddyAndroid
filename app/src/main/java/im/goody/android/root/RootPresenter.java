package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.about.AboutController;
import im.goody.android.screens.detail_post.DetailPostController;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.login.LoginController;
import im.goody.android.screens.main.MainController;
import im.goody.android.screens.near_events.NearEventsController;
import im.goody.android.screens.new_event.NewEventController;
import im.goody.android.screens.new_post.NewPostController;
import im.goody.android.screens.photo.PhotoController;
import im.goody.android.screens.profile.ProfileController;
import im.goody.android.screens.register.RegisterController;
import im.goody.android.screens.setting.SettingController;
import im.goody.android.ui.helpers.BarBuilder;

public class RootPresenter implements IRootPresenter {

    @Inject IRepository repository;
    private IRootView rootView;

    public RootPresenter() {
        RootComponent component = App.getRootComponent();
        if (component != null)
            component.inject(this);
    }

    //region ================= RootPresenter =================

    void takeView(IRootView rootView) {
        this.rootView = rootView;

        if (repository.isSigned()) {
            rootView.showDrawerHeader(repository.getUserData());
        }
    }

    void dropView() {
        this.rootView = null;
    }

    //endregion

    //region ================= IRootPresenter - Service methods =================

    @Override
    public BarBuilder newBarBuilder() {
        return new BarBuilder(rootView);
    }

    @Override
    public Class<? extends Controller> getStartController() {
        if (repository.isFirstLaunch())
            return IntroController.class;
        else if (repository.isSigned())
            return MainController.class;
        else
            return LoginController.class;
    }

    @Override
    public void showProgress(@StringRes int messageResId) {
        rootView.showProgress(messageResId);
    }

    @Override
    public void hideProgress() {
        rootView.hideProgress();
    }

    //endregion

    //region ================= IRootPresenter - Show screens methods =================

    @Override
    public void showNews() {
        if (rootView != null) {
            rootView.showScreenAsRoot(MainController.class);
            rootView.showDrawerHeader(repository.getUserData());
        }
    }

    @Override
    public void showUserPosts(String userId) {
        if (rootView != null) {
            rootView.showScreen(MainController.class, userId, true);
            rootView.showDrawerHeader(repository.getUserData());
        }
    }


    @Override
    public void showMyPosts() {
        if (rootView != null) {
            long id = repository.getUserData().getUser().getId();

            rootView.showScreenAsRoot(MainController.class, String.valueOf(id), false);
            rootView.showDrawerHeader(repository.getUserData());
        }
    }

    @Override
    public void showLoginScreen() {
        if (rootView != null) {
            rootView.showScreenAsRoot(LoginController.class);
        }
    }

    @Override
    public void showRegisterScreen() {
        if (rootView != null)
            rootView.showScreen(RegisterController.class);
    }

    @Override
    public void showNewPostScreen() {
        if (rootView != null)
            rootView.showScreen(NewPostController.class);
    }

    @Override
    public void showDetailScreen(long id) {
        if (rootView != null) {
            rootView.showScreen(DetailPostController.class, id);
        }
    }

    @Override
    public void showAboutScreen() {
        if (rootView != null)
            rootView.showScreenAsRoot(AboutController.class);
    }

    @Override
    public void showSettingScreen() {
        if (rootView != null)
            rootView.showScreenAsRoot(SettingController.class);
    }

    @Override
    public void showNewEventScreen() {
        if (rootView != null)
            rootView.showScreen(NewEventController.class);
    }

    @Override
    public void showMyProfile() {
        showProfile(String.valueOf(repository.getUserData().getUser().getId()));
    }

    @Override
    public void showProfile(String id) {
        if (rootView != null)
            rootView.showScreen(ProfileController.class, id);
    }

    @Override
    public void showPhotoScreen(String imageUrl) {
        if (rootView != null)
            rootView.showScreen(PhotoController.class, imageUrl);
    }

    @Override
    public void showNearEventsScreen() {
        if (rootView != null)
            rootView.showScreenAsRoot(NearEventsController.class);
    }

    @Override
    public void showEditPostScreen(Deal deal) {
        if (rootView != null)
            rootView.showScreen(NewPostController.class, deal);
    }

    @Override
    public void showEditEventScreen(Deal deal) {
        if (rootView != null)
            rootView.showScreen(NewEventController.class, deal);
    }

    @Override
    public void showParticipatingEvents() {
        if(rootView != null) {
            long id = repository.getUserData().getUser().getId();

            rootView.showScreenAsRoot(MainController.class, String.valueOf(id), false, true);
        }
    }

    //endregion

    @Override
    public void launched() {
        if (repository.isFirstLaunch()) repository.firstLaunched();
    }

    @Override
    public void logout() {
        repository.logout();
        showLoginScreen();
    }
}
