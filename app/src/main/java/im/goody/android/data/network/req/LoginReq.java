package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public class LoginReq {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginReq setPassword(String password) {
        this.password = password;
        return this;
    }
}
