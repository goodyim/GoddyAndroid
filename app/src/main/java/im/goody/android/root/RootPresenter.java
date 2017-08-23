package im.goody.android.root;

import im.goody.android.R;
import im.goody.android.screens.intro.IntroController;
import im.goody.android.screens.main.MainController;
import im.goody.android.ui.helpers.BarBuilder;

public class RootPresenter implements IRootPresenter {
    private IRootView rootView;

    void takeView(IRootView rootView) {
        this.rootView = rootView;
    }

    void dropView() {
        this.rootView = null;
    }

    @Override
    public BarBuilder newBarBuilder() {
        return new BarBuilder(rootView);
    }

    @Override
    public void showIntroScreen() {
        if (rootView != null)
            rootView.showScreen(new IntroController());
    }

    @Override
    public void showMainScreen() {
        if (rootView != null)
            rootView.showScreen(new MainController());
    }

    @Override
    public void hideProgress() {
        rootView.hideProgress();
    }

    @Override
    public void showRegisterProgress() {
        rootView.showProgress(R.string.register_progress_title);
    }

    @Override
    public void showLoginProgress() {
        rootView.showProgress(R.string.login_progress_title);
    }
}
