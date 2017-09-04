package im.goody.android.core;

import android.view.View;

import java.util.List;

import im.goody.android.ui.helpers.MenuItemHolder;

public interface IBarView {
    void setToolbarTitle(Integer titleRes);
    void setToolBarVisible(boolean visible);
    void setHomeState(int state);
    void setHomeListener(View.OnClickListener listener);
    void setStatusBarVisible(boolean visible);
    void setToolBarMenuItem(List<MenuItemHolder> items);
}
