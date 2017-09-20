package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private long id;

    @JsonProperty("user_name")
    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("followers_count")
    private int followers;

    @JsonProperty("followees_count")
    private int followees;

    @JsonProperty("good_deals_count")
    private int dealsCount;

    @JsonProperty("events_count")
    private int eventsCount;

    @JsonProperty("is_following")
    private boolean isFollowing;

    // ======= region getters =======

    public boolean isFollowing() {
        return isFollowing;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowees() {
        return followees;
    }

    public int getDealsCount() {
        return dealsCount;
    }

    public int getEventsCount() {
        return eventsCount;
    }

    // endregion

    // ======= region setters =======

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setFollowees(int followees) {
        this.followees = followees;
    }

    public void setDealsCount(int dealsCount) {
        this.dealsCount = dealsCount;
    }

    public void setEventsCount(int eventsCount) {
        this.eventsCount = eventsCount;
    }

    // endregion
}
