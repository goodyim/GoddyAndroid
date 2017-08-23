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
import im.goody.android.data.network.req.LoginReq;

import static android.util.Patterns.EMAIL_ADDRESS;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

@SuppressWarnings("unused")
public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;

    @Bindable
    private Drawable emailRes;

    @Bindable
    private Drawable passwordRes;

    private Drawable emptyFieldDrawable;
    private Drawable validFieldDrawable;
    private Drawable invalidFieldDrawable;

    LoginViewModel() {
        Context context = App.getAppContext();
        emptyFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_background);
        validFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_valid);
        invalidFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_invalid);

        emailRes = emptyFieldDrawable;
        passwordRes = emptyFieldDrawable;
    }

    boolean isValid() {
        return isEmailValid() && isPasswordValid();
    }

    LoginReq body() {
        return new LoginReq()
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
        if (TextUtils.isEmpty(email))
            emailRes = emptyFieldDrawable;
        else
        if (isEmailValid())
            emailRes = validFieldDrawable;
        else
            emailRes = invalidFieldDrawable;
        notifyPropertyChanged(BR.emailRes);
    }

    private void setPasswordDrawable() {
        if (TextUtils.isEmpty(password))
            passwordRes = emptyFieldDrawable;
        else
        if (isPasswordValid())
            passwordRes = validFieldDrawable;
        else
            passwordRes = invalidFieldDrawable;
        notifyPropertyChanged(BR.passwordRes);
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
