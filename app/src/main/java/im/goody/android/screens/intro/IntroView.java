package im.goody.android.screens.intro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenIntroBinding;

public class IntroView extends BaseView<IntroController, ScreenIntroBinding> {

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(PagerAdapter adapter) {
        binding.introPager.setAdapter(adapter);
        binding.introTabs.setupWithViewPager(binding.introPager, true);
    }

    @Override
    protected void onAttached() {
        binding.introClose.setOnClickListener(v -> controller.close());
    }

    @Override
    protected void onDetached() {

    }
}
