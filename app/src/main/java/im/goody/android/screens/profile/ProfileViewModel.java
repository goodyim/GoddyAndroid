package im.goody.android.screens.profile;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import im.goody.android.data.dto.User;
import im.goody.android.data.network.res.FollowRes;

public class ProfileViewModel {
    private String name;
    private String avatarUrl;
    private int deals;
    private int events;
    private String registrationDate;

    public final ObservableBoolean isFollowing = new ObservableBoolean();

    public final ObservableInt followers = new ObservableInt();
    ProfileViewModel(User user) {
        name = user.getName();
        avatarUrl = user.getAvatarUrl();
        deals = user.getDealsCount();
        events = user.getEventsCount();
        registrationDate = user.getRegistrationDate();

        followers.set(user.getFollowers());
        isFollowing.set(user.isFollowing());
    }

    // ======= region getters =======

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getDeals() {
        return deals;
    }

    public int getEvents() {
        return events;
    }

    public ObservableBoolean getIsFollowing() {
        return isFollowing;
    }

    // endregion

    // ======= region setters =======

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDeals(int deals) {
        this.deals = deals;
    }

    public void setEvents(int events) {
        this.events = events;
    }

    void updateFollowState(FollowRes result) {
        followers.set(result.getFollowersCount());
        isFollowing.set(result.isSubscribed());
    }

    // endregion
}
