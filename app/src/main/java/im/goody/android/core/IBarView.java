package im.goody.android.core;

import java.util.List;

import im.goody.android.ui.helpers.MenuItemHolder;

public interface IBarView {
    void setToolbarTitle(Integer titleRes);
    void setToolBarVisible(boolean visible);
    void setBackArrow(boolean enabled);

    void setToolBarMenuItem(List<MenuItemHolder> items);
}
