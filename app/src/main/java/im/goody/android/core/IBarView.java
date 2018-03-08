package im.goody.android.core;

import android.view.View;

public interface IBarView {
    void setToolbarTitle(Integer titleRes);
    void setToolBarVisible(boolean visible);
    void setHomeState(int state);
    void setHomeListener(View.OnClickListener listener);
    void setStatusBarVisible(boolean visible);
}
