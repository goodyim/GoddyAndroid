package im.goody.android.screens.intro;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenIntroBinding;

public class IntroView extends BaseView<IntroController, ScreenIntroBinding> {

    private IntroPageAdapter adapter;

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            adapter = new IntroPageAdapter();
            binding.introPager.setAdapter(adapter);
            binding.introTabs.setupWithViewPager(binding.introPager);
        }
    }

    @Override
    protected void onAttached() {
        binding.introNext.setOnClickListener(v -> controller.close());
    }

    @Override
    protected void onDetached() {

    }
}
