package im.goody.android.data.network.req;

public class UserReq {
    private String user_name;
    private String email;
    private String password;

    public UserReq(String user_name, String email, String password) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
    }
}
