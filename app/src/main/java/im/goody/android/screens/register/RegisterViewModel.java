package im.goody.android.screens.register;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import java.util.Calendar;

import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.validation.Validatable;
import im.goody.android.data.validation.ValidateResult;
import im.goody.android.utils.DateUtils;

import static android.util.Patterns.EMAIL_ADDRESS;
import static im.goody.android.Constants.MIN_PASSWORD_LENGTH;
import static im.goody.android.Constants.SEX_MALE;

@SuppressWarnings("unused")
public class RegisterViewModel extends BaseObservable implements Validatable {

    private String name;
    private String email;
    private String password;

    public ObservableField<Calendar> birthday = new ObservableField<>();

    private int sex = SEX_MALE;

    RegisterReq body() {
        return new RegisterReq()
                .setEmail(email)
                .setPassword(password)
                .setName(name)
                .setBirthday(getStringDate())
                .setSex(sex);
    }

    // ======= region getters =======

    public int getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    //endregion

    // ======= region setters =======

    public void setSex(int id) {
        switch (id) {
            case R.id.sex_male:
                sex = Constants.SEX_MALE;
                break;
            case R.id.sex_female:
                sex = Constants.SEX_FEMALE;
        }
    }

    public RegisterViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public RegisterViewModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterViewModel setPassword(String password) {
        this.password = password;
        return this;
    }

    // endregion

    // ======= region private methods =======

    private boolean isNameValid() {
        return !TextUtils.isEmpty(name) && name.matches("[a-z]{4,}");
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }

    private String getStringDate() {
        if (birthday.get() != null)
            return DateUtils.dateToString(birthday.get().getTime());
        else return null;
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();

        if (!isNameValid()) {
            result.getErrorResources().add(R.string.invalid_name);
        }

        if (!isEmailValid()) {
            result.getErrorResources().add(R.string.invalid_email);
        }

        if (!isPasswordValid()) {
            result.getErrorResources().add(R.string.invalid_password);
        }

        return result;
    }

    // endregion
}
