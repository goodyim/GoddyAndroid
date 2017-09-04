package im.goody.android.screens.register;

import android.content.ContentResolver;
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
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.utils.BitmapUtils;

import static android.util.Patterns.EMAIL_ADDRESS;
import static im.goody.android.Constants.MIN_NAME_LENGTH;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;

@SuppressWarnings("unused")
public class RegisterViewModel extends BaseObservable{

    private String name;
    private String email;
    private String password;

    private Uri avatarUri;

    @Bindable
    private Drawable nameRes;

    @Bindable
    private Drawable emailRes;

    @Bindable
    private Drawable passwordRes;

    @Bindable
    private RoundedBitmapDrawable avatar;
    private Drawable emptyFieldDrawable;

    private Drawable validFieldDrawable;
    private Drawable invalidFieldDrawable;
    RegisterViewModel() {
        Context context = App.getAppContext();
        emptyFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_background);
        validFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_valid);
        invalidFieldDrawable = ContextCompat.getDrawable(context, R.drawable.field_invalid);

        nameRes = emptyFieldDrawable;
        emailRes = emptyFieldDrawable;
        passwordRes = emptyFieldDrawable;
    }

    boolean isValid() {
        return isEmailValid() && isPasswordValid() && isNameValid();
    }

    RegisterReq body() {
        return new RegisterReq()
                .setEmail(email)
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

    Uri getAvatarUri() {
        return avatarUri;
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

    public void setAvatarUri(Uri avatarUri) {
        this.avatarUri = avatarUri;
    }

    public RegisterViewModel setAvatar(RoundedBitmapDrawable avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
        return this;
    }

    void setAvatar(Uri imageUri, boolean isNewFile) {
        ContentResolver resolver = App.getAppContext().getContentResolver();

        if (isNewFile) resolver.notifyChange(imageUri, null);

        try {
            Bitmap original = MediaStore.Images.Media.getBitmap(resolver, imageUri);
            avatar = BitmapUtils.prepareAvatar(original, App.getAppContext());
        } catch (IOException e) {
            e.printStackTrace();
            avatar = null;
        }

        notifyPropertyChanged(BR.avatar);
    }

    // endregion

    // ======= region private methods =======

    private boolean isNameValid() {
        return !TextUtils.isEmpty(name) && name.length() >= MIN_NAME_LENGTH;
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
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

    private void setNameDrawable() {
        if (TextUtils.isEmpty(name))
            nameRes = emptyFieldDrawable;
        else
        if (isNameValid())
            nameRes = validFieldDrawable;
        else
            nameRes = invalidFieldDrawable;
        notifyPropertyChanged(BR.nameRes);
    }

    // endregion
}
