package im.goody.android.screens.login;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Auth;

import static android.util.Patterns.EMAIL_ADDRESS;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;
    private Context context;

    @Bindable
    private Drawable emailRes;

    @Bindable
    private Drawable passwordRes;

    public LoginViewModel() {
        context = App.getAppContext();
        emailRes = ContextCompat.getDrawable(context, R.drawable.auth_field_background);
        passwordRes = ContextCompat.getDrawable(context, R.drawable.auth_field_background);
    }

    public boolean isValid() {
        return isEmailValid() && isPasswordValid();
    }

    public Auth body() {
        return new Auth()
                .setEmail(email)
                .setPassword(password);
    }

    public LoginViewModel setEmailRes(Drawable emailRes) {
        this.emailRes = emailRes;
        return this;
    }

    public LoginViewModel setPasswordRes(Drawable passwordRes) {
        this.passwordRes = passwordRes;
        return this;
    }

    public LoginViewModel setEmail(String email) {
        this.email = email;
        setEmailDrawable();
        return this;
    }

    public LoginViewModel setPassword(String password) {
        this.password = password;
        setPasswordDrawable();
        return this;
    }

    public Drawable getEmailRes() {
        return emailRes;
    }

    public Drawable getPasswordRes() {
        return passwordRes;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private void setEmailDrawable() {
        int res = isEmailValid() ? R.drawable.auth_field_valid : R.drawable.auth_field_invalid;
        if (TextUtils.isEmpty(email)) res = R.drawable.auth_field_background;
        emailRes = ContextCompat.getDrawable(context, res);
        notifyPropertyChanged(BR.emailRes);
    }

    private void setPasswordDrawable() {
        int res = isPasswordValid() ? R.drawable.auth_field_valid : R.drawable.auth_field_invalid;
        if (TextUtils.isEmpty(password)) res = R.drawable.auth_field_background;
        passwordRes = ContextCompat.getDrawable(context, res);
        notifyPropertyChanged(BR.passwordRes);
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
