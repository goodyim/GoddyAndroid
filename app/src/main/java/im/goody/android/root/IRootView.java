package im.goody.android.root;

import com.bluelinelabs.conductor.Controller;

import im.goody.android.core.IBarView;

interface IRootView extends IBarView {
    void showScreen(Controller controller);
}
