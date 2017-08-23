package im.goody.android.data.dto;

import android.graphics.Bitmap;

public class Register {
    private String name;
    private String email;
    private String password;
    private Bitmap avatar;

    // ======= region getters =======

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    // endregion

    // ======= region setters =======

    public Register setName(String name) {
        this.name = name;
        return this;
    }

    public Register setEmail(String email) {
        this.email = email;
        return this;
    }

    public Register setPassword(String password) {
        this.password = password;
        return this;
    }

    public Register setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        return this;
    }

    //endregion
}
