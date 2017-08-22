package im.goody.android.screens.auth.login;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenLoginBinding;

public class LoginView extends BaseView<LoginController, ScreenLoginBinding> {
    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAuthData(LoginViewModel data) {
        binding.setUser(data);
    }

    public void showProgress() {
        binding.signInProgress.setVisibility(VISIBLE);
        binding.signInSubmit.setEnabled(false);
        binding.signInRedirect.setEnabled(false);
        binding.signInFacebook.setEnabled(false);
    }

    public void showInvalidFieldsMessage() {
        showSnackbarMessage(R.string.invalid_fields_message);
    }

    public void showAuthError(String text) {
        binding.signInProgress.setVisibility(GONE);
        binding.signInSubmit.setEnabled(true);
        binding.signInRedirect.setEnabled(true);
        binding.signInFacebook.setEnabled(true);

        showSnackbarMessage(text);
    }

    @Override
    protected void onAttached() {
        binding.signInSubmit.setOnClickListener(v -> controller.login());
    }

    @Override
    protected void onDetached() {

    }
}
