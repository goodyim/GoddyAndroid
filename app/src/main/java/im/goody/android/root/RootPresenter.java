package im.goody.android.root;

import android.support.annotation.StringRes;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.core.BaseController;
import im.goody.android.data.IRepository;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.login.LoginController;
import im.goody.android.screens.main.MainController;
import im.goody.android.screens.register.RegisterController;
import im.goody.android.ui.helpers.BarBuilder;

public class RootPresenter implements IRootPresenter {

    private IRootView rootView;

    @Inject
    IRepository repository;

    public RootPresenter() {
        RootComponent component = App.getRootComponent();
        if (component != null)
            component.inject(this);
    }

    //region ================= RootPresenter =================

    void takeView(IRootView rootView) {
        this.rootView = rootView;
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
    public BaseController getStartController() {
        if (repository.isSigned())
            return new MainController();
        else
            return new LoginController();
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
    public void showIntroScreen() {
        if (rootView != null)
            rootView.showScreen(IntroController.class);
    }

    @Override
    public void showMainScreen() {
        if (rootView != null)
            rootView.showScreenAsRoot(MainController.class);
    }

    @Override
    public void showLoginScreen() {
        if (rootView != null)
            rootView.showScreen(LoginController.class);
    }

    @Override
    public void showRegisterScreen() {
        if (rootView != null)
            rootView.showScreen(RegisterController.class);
    }

    //endregion
}
