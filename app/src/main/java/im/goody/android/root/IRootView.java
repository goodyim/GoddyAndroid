package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.core.IBarView;

interface IRootView extends IBarView {
    void showScreen(Class<? extends Controller> controllerClass);
    void showScreenAsRoot(Class<? extends Controller> controllerClass);
    void showProgress(@StringRes int titleRes);
    void hideProgress();
}
