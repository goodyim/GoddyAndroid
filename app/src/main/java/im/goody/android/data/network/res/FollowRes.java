package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowRes {
    @JsonProperty("is_subsribed")
    private boolean isSubscribed;

    @JsonProperty("followers_count")
    private int followersCount;

    // ======= region getters =======

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    // end


    // ======= region setters =======

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    // end
}
