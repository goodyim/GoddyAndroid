package im.goody.android.screens.greet;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenGreetBinding;

public class GreetView extends BaseView<GreetController, ScreenGreetBinding> {
    public GreetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.greetContinue.setOnClickListener(v -> controller.next());
    }

    @Override
    protected void onDetached() {

    }
}
