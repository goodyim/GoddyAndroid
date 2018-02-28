package im.goody.android.screens.register;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenRegisterBinding;
import im.goody.android.utils.UIUtils;

public class RegisterView extends BaseView<RegisterController, ScreenRegisterBinding> {

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(RegisterViewModel data) {
        binding.setData(data);
    }

    @Override
    protected void onAttached() {
        binding.registerImage.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getFocusedChild());
            controller.chooseAvatar();
        });

        binding.registerSubmit.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getFocusedChild());
            controller.register();
        });

        binding.registerName.setFilters(new InputFilter[] {
                (charSequence, i, i1, spanned, i2, i3) -> charSequence.toString().toLowerCase()
        });
    }

    @Override
    protected void onDetached() {

    }
}
