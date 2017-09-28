package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.ui.helpers.BarBuilder;

public interface IRootPresenter {
    // Service methods
    BarBuilder newBarBuilder();
    Class<? extends Controller> getStartController();

    void showProgress(@StringRes int messageResId);
    void hideProgress();

    // Show screen methods
    void showMainScreen();
    void showLoginScreen();
    void showRegisterScreen();
    void showNewPostScreen();
    void showDetailScreen(long id);
    void showAboutScreen();
    void showSettingScreen();
    void showNewEventScreen();
    //first start
    void launched();

    void logout();

    void showMyProfile();
    void showProfile(long id);
}
