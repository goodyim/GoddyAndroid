package im.goody.android.screens.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenLoginBinding;
import im.goody.android.ui.dialogs.InfoDialog;
import im.goody.android.utils.UIUtils;

public class LoginView extends BaseView<LoginController, ScreenLoginBinding> {
    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAuthData(LoginViewModel data) {
        binding.setUser(data);
    }

    @Override
    protected void onAttached() {
        binding.signInSubmit.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getFocusedChild());
            controller.login();
        });
        binding.signInRedirectAction.setOnClickListener(v -> controller.goToRegister());
        binding.signInForgotPassword.setOnClickListener(v -> controller.forgotPasswordClicked());
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
