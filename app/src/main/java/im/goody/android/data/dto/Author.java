package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {
    private String id;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    // ======= region getters =======

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getId() {
        return id;
    }

    // endregion

    // ======= region setters =======

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    public Author setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    // endregion
}
