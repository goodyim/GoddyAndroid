package im.goody.android.ui.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import im.goody.android.core.IBarView;

public class BarBuilder {
    private IBarView view;

    private boolean backArrow;
    private boolean toolbarVisible;
    @Nullable
    private Integer titleRes;
    private List<MenuItemHolder> items = new ArrayList<>();

    public BarBuilder(IBarView view) {
        this.view = view;
    }

    @NonNull
    public BarBuilder setTitleRes(@StringRes @NonNull Integer titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    @NonNull
    public BarBuilder setBackArrow(boolean enable) {
        this.backArrow = enable;
        return this;
    }

    @NonNull
    public BarBuilder setToolbarVisible(boolean toolbarVisible) {
        this.toolbarVisible = toolbarVisible;
        return this;
    }

    @NonNull
    public BarBuilder addAction(MenuItemHolder item) {
        this.items.add(item);
        return this;
    }

    public void build() {
        if (view != null) {
            view.setToolBarVisible(toolbarVisible);

            if (titleRes != null)
                view.setToolbarTitle(titleRes);

            view.setBackArrow(backArrow);
            view.setToolBarMenuItem(items);
        }
    }
}
