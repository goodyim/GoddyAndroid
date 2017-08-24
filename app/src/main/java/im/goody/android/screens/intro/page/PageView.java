package im.goody.android.screens.intro.page;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ItemIntroBinding;

public class PageView extends BaseView<PageController, ItemIntroBinding> {
    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPage(Page page) {
        binding.setPage(page);
    }

    @Override
    protected void onAttached() {
    }

    @Override
    protected void onDetached() {

    }
}
