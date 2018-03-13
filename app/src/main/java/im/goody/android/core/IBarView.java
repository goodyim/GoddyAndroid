package im.goody.android.core;

import android.view.View;

import im.goody.android.ui.helpers.BarBuilder;

public interface IBarView {
    void setToolbarTitle(Integer titleRes);
    void setToolBarVisible(boolean visible);
    void setHomeState(int state);
    void setHomeListener(View.OnClickListener listener);
    void setStatusBarVisible(boolean visible);

    void setTabs(BarBuilder.TabInfo tabInfo);
}
