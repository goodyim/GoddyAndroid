package im.goody.android.root;

import android.support.annotation.StringRes;

import im.goody.android.core.BaseController;
import im.goody.android.ui.helpers.BarBuilder;

public interface IRootPresenter {
    // Service methods
    BarBuilder newBarBuilder();
    BaseController getStartController();

    void showProgress(@StringRes int messageResId);
    void hideProgress();

    // Show screen methods
    void showMainScreen();
    void showLoginScreen();
    void showRegisterScreen();
    void showNewPostScreen();

    //first start
    void launched();
}
