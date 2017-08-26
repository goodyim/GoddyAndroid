package im.goody.android.screens.register;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenRegisterBinding;

public class RegisterView extends BaseView<RegisterController, ScreenRegisterBinding> {

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(RegisterViewModel data) {
        binding.setData(data);
    }

    @Override
    protected void onAttached() {
        binding.registerImage.setOnClickListener(v -> controller.chooseAvatar());
        binding.registerSubmit.setOnClickListener(v -> controller.register());
    }

    @Override
    protected void onDetached() {

    }
}
