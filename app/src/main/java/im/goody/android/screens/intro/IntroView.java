package im.goody.android.screens.intro;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import im.goody.android.R;
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
        binding.introPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    protected void onDetached() {
        binding.introPager.removeOnPageChangeListener(pageChangeListener);
    }

    private final ViewPager.OnPageChangeListener pageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                @Override
                public void onPageSelected(int position) {
                    if (position == adapter.getCount() - 1)
                        binding.introNext.setText(R.string.intro_start);
                    else
                        binding.introNext.setText(R.string.intro_close);
                }

                @Override
                public void onPageScrollStateChanged(int state) {}
            };
}
