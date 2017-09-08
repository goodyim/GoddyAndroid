package im.goody.android.screens.about;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenAboutBinding;

public class AboutView extends BaseView<AboutController, ScreenAboutBinding> {

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.aboutBackBtn.setOnClickListener(view -> controller.onBackPressed());
    }

    @Override
    protected void onDetached() {
        binding.aboutBackBtn.setOnClickListener(null);
    }
}
