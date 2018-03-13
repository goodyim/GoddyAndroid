package im.goody.android.screens.login;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import im.goody.android.data.network.req.LoginReq;

import static im.goody.android.Constants.MIN_NAME_LENGTH;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

@SuppressWarnings("unused")
public class LoginViewModel extends BaseObservable {
    private String name;
    private String password;

    boolean isValid() {
        return isEmailValid() && isPasswordValid();
    }

    LoginReq body() {
        return new LoginReq()
                .setName(name)
                .setPassword(password);
    }

    public LoginViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public LoginViewModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    private boolean isEmailValid() {
        return !TextUtils.isEmpty(name) && name.length() >= MIN_NAME_LENGTH;
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
