package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.core.IBarView;
import im.goody.android.data.network.res.UserRes;

interface IRootView extends IBarView {
    void showScreen(Class<? extends Controller> controllerClass, Object... args);
    void showScreenAsRoot(Class<? extends Controller> controllerClass, Object... args);
    void showDrawerHeader(UserRes user);

    void showProgress(@StringRes int titleRes);
    void hideProgress();
}
