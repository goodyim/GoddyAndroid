package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRes {
    @JsonProperty("user_id")
    private int id;
    private String token;

    // ======= region Getters =======

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    // endregion

    // ======= region Setters =======

    public void setId(int id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //endregion
}
