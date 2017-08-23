package im.goody.android.data.network.req;

import android.graphics.Bitmap;

public class RegisterReq {
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

    public RegisterReq setName(String name) {
        this.name = name;
        return this;
    }

    public RegisterReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterReq setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterReq setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        return this;
    }

    //endregion
}
