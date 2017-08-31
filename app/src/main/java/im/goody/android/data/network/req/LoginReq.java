package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public class LoginReq {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public LoginReq setName(String name) {
        this.name = name;
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
