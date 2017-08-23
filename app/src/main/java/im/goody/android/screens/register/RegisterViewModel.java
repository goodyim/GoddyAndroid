package im.goody.android.screens.register;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.text.TextUtils;

import java.io.IOException;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Register;
import im.goody.android.utils.BitmapUtils;

import static android.util.Patterns.EMAIL_ADDRESS;
import static im.goody.android.Constants.MIN_NAME_LENGTH;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

@SuppressWarnings("unused")
public class RegisterViewModel extends BaseObservable{

    private String name;
    private String email;
    private String password;

    private Context context;

    @Bindable
    private Drawable nameRes;

    @Bindable
    private Drawable emailRes;

    @Bindable
    private Drawable passwordRes;

    @Bindable
    private RoundedBitmapDrawable avatar;

    RegisterViewModel() {
        context = App.getAppContext();
        nameRes = ContextCompat.getDrawable(context, R.drawable.auth_field_background);
        context = App.getAppContext();
        emailRes = ContextCompat.getDrawable(context, R.drawable.auth_field_background);
        passwordRes = ContextCompat.getDrawable(context, R.drawable.auth_field_background);
    }

    boolean isValid() {
        return isEmailValid() && isPasswordValid()
                && isNameValid() && avatar != null;
    }

    Register body() {
        return new Register()
                .setEmail(email)
                .setAvatar(avatar.getBitmap())
                .setPassword(password)
                .setName(name);
    }

    // ======= region getters =======

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Drawable getEmailRes() {
        return emailRes;
    }

    public Drawable getPasswordRes() {
        return passwordRes;
    }

    public String getName() {
        return name;
    }

    public Drawable getNameRes() {
        return nameRes;
    }

    public RoundedBitmapDrawable getAvatar() {
        return avatar;
    }

    //endregion

    // ======= region setters =======

    public RegisterViewModel setName(String name) {
        this.name = name;
        setNameDrawable();
        return this;
    }

    public RegisterViewModel setEmail(String email) {
        this.email = email;
        setEmailDrawable();
        return this;
    }

    public RegisterViewModel setPassword(String password) {
        this.password = password;
        setPasswordDrawable();
        return this;
    }

    public RegisterViewModel setNameRes(Drawable nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public RegisterViewModel setEmailRes(Drawable emailRes) {
        this.emailRes = emailRes;
        return this;
    }

    public RegisterViewModel setPasswordRes(Drawable passwordRes) {
        this.passwordRes = passwordRes;
        return this;
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

    private void setNameDrawable() {
        int res = isNameValid() ? R.drawable.auth_field_valid : R.drawable.auth_field_invalid;
        if (TextUtils.isEmpty(name)) res = R.drawable.auth_field_background;
        nameRes = ContextCompat.getDrawable(context, res);
        notifyPropertyChanged(BR.nameRes);
    }

    public RegisterViewModel setAvatar(RoundedBitmapDrawable avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
        return this;
    }

    void setAvatar(Uri imageUri) {
        try {
            Bitmap original = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            avatar = BitmapUtils.prepareAvatar(original, context);
        } catch (IOException e) {
            e.printStackTrace();
            avatar = null;
        }
        notifyPropertyChanged(BR.avatar);
    }

    // endregion

    private boolean isNameValid() {
        return !TextUtils.isEmpty(name) && name.length() >= MIN_NAME_LENGTH;
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }
}
