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

import static im.goody.android.Constants.MIN_NAME_LENGTH;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

@SuppressWarnings("unused")
public class LoginViewModel extends BaseObservable {
    private String name;
    private String password;

    @Bindable
    private Drawable nameRes;

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

        nameRes = emptyFieldDrawable;
        passwordRes = emptyFieldDrawable;
    }

    boolean isValid() {
        return isEmailValid() && isPasswordValid();
    }

    LoginReq body() {
        return new LoginReq()
                .setName(name)
                .setPassword(password);
    }

    public LoginViewModel setNameRes(Drawable nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public LoginViewModel setPasswordRes(Drawable passwordRes) {
        this.passwordRes = passwordRes;
        return this;
    }

    public LoginViewModel setName(String name) {
        this.name = name;
        setEmailDrawable();
        return this;
    }

    public LoginViewModel setPassword(String password) {
        this.password = password;
        setPasswordDrawable();
        return this;
    }

    public Drawable getNameRes() {
        return nameRes;
    }

    public Drawable getPasswordRes() {
        return passwordRes;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    private void setEmailDrawable() {
        if (TextUtils.isEmpty(name))
            nameRes = emptyFieldDrawable;
        else
        if (isEmailValid())
            nameRes = validFieldDrawable;
        else
            nameRes = invalidFieldDrawable;
        notifyPropertyChanged(BR.nameRes);
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
        return !TextUtils.isEmpty(name) && name.length() >= MIN_NAME_LENGTH;
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
