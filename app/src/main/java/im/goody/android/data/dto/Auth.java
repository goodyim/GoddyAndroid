package im.goody.android.data.dto;

public class Auth {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public Auth setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Auth setPassword(String password) {
        this.password = password;
        return this;
    }
}
