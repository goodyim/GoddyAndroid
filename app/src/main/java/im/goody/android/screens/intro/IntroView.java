package im.goody.android.screens.intro;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenIntroBinding;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;

public class IntroView extends BaseView<IntroController, ScreenIntroBinding> {

    private IntroPageAdapter adapter;

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
    }

    @Override
    protected void onDetached() {
    }

    public void setData(ChooseHelpViewModel viewModel) {
        adapter = new IntroPageAdapter(controller, viewModel);
        binding.introPager.setAdapter(adapter);
        binding.introPager.setOffscreenPageLimit(IntroPageAdapter.PAGE_COUNT);
        binding.introTabs.setupWithViewPager(binding.introPager);
    }
}
