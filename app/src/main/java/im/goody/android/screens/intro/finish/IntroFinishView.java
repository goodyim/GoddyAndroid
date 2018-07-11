package im.goody.android.screens.intro.finish;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.IntroFinishBinding;

public class IntroFinishView extends BaseView<IntroFinishController, IntroFinishBinding> {
    public IntroFinishView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.introFinishSubmit.setOnClickListener(v -> controller.confirm());
    }

    @Override
    protected void onDetached() {

    }
}
