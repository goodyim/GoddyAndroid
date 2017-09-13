package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.about.AboutController;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.login.LoginController;
import im.goody.android.screens.main.MainController;
import im.goody.android.screens.new_event.NewEventController;
import im.goody.android.screens.new_post.NewPostController;
import im.goody.android.screens.register.RegisterController;
import im.goody.android.screens.setting.SettingController;
import im.goody.android.ui.helpers.BarBuilder;

public class RootPresenter implements IRootPresenter {

    @Inject
    IRepository repository;
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
    public void showMainScreen(boolean isRoot) {
        if (rootView != null) {
            if (isRoot) {
                rootView.showScreenAsRoot(MainController.class);
                rootView.showDrawerHeader(repository.getUserData());
            }
            else
                rootView.showScreen(MainController.class);
        }
    }

    @Override
    public void showLoginScreen(boolean isRoot) {
        if (rootView != null) {
            if (isRoot)
                rootView.showScreenAsRoot(LoginController.class);
            else
                rootView.showScreen(LoginController.class);
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
            rootView.showDetailScreen(id);
        }
    }

    @Override
    public void showAboutScreen() {
        if (rootView != null)
            rootView.showScreen(AboutController.class);
    }

    @Override
    public void showSettingScreen() {
        if (rootView != null)
            rootView.showScreen(SettingController.class);
    }

    @Override
    public void showNewEventScreen() {
        if (rootView != null)
            rootView.showScreen(NewEventController.class);
    }

    //endregion

    @Override
    public void launched() {
        if (repository.isFirstLaunch()) repository.firstLaunched();
    }
}
