package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.data.dto.Deal;
import im.goody.android.ui.helpers.BarBuilder;

public interface IRootPresenter {
    // Service methods
    BarBuilder newBarBuilder();
    Class<? extends Controller> getStartController();

    void showProgress(@StringRes int messageResId);
    void hideProgress();

    // Show screen methods
    void showNews();
    void showUserPosts(String id);
    void showMyPosts();

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
    void showProfile(String id);

    void showPhotoScreen(String imageUrl);

    void showNearEventsScreen();

    void showEditPostScreen(Deal deal);
    void showEditEventScreen(Deal deal);

    void showParticipatingEvents();
}
