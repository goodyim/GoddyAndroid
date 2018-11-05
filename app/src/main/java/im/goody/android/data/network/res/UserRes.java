package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRes {
    private User user;

    public User getUser() {
        return user;
    }

    public UserRes setUser(User user) {
        this.user = user;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        private int id;
        private String token;

        @JsonProperty("user_name")
        private String name;

        @JsonProperty("avatar_url")
        private String avatarUrl;

        // ======= region Getters =======


        public String getName() {
            return name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public int getId() {
            return id;
        }

        public String getToken() {
            return token;
        }

        // endregion

        // ======= region Setters =======

        public User setId(int id) {
            this.id = id;
            return this;
        }

        public User setToken(String token) {
            this.token = token;
            return this;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

        public User setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

    }
    //endregion
}
