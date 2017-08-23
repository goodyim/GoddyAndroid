package im.goody.android.root;

import android.support.annotation.StringRes;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.core.IBarView;

interface IRootView extends IBarView {
    void showScreen(Controller controller);
    void showProgress(@StringRes int titleRes);
    void hideProgress();
}
