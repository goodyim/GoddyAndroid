package im.goody.android.screens.register;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextWatcher;
import android.util.AttributeSet;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenRegisterBinding;
import im.goody.android.ui.dialogs.InfoDialog;
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
        binding.registerBirthday.setOnClickListener(v -> controller.chooseDate());
        binding.registerRedirect.setOnClickListener(v -> controller.redirectToLogin());

        binding.registerSubmit.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getFocusedChild());
            controller.register();
        });

        setPhoneMask();

    }

    private void setPhoneMask() {
        TextWatcher watcher;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            watcher = new PhoneNumberFormattingTextWatcher("RU");
        } else {
            watcher = new PhoneNumberFormattingTextWatcher();
        }

        binding.registerPhone.addTextChangedListener(watcher);
    }

    @Override
    protected void onDetached() {

    }

    @NonNull
    @Override
    protected InfoDialog getMessageDialog(String message) {
        return new InfoDialog(message, R.string.app_name, R.string.lets_go);
    }
}
