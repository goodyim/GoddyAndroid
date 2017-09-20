package im.goody.android.screens.profile;

import android.databinding.ObservableBoolean;

import im.goody.android.data.dto.User;

public class ProfileViewModel {
    private String name;
    private String avatarUrl;
    private int followers;
    private int deals;
    private int events;

    public final ObservableBoolean isFollowing = new ObservableBoolean();

    public ProfileViewModel(User user) {
        name = user.getName();
        avatarUrl = user.getAvatarUrl();
        followers = user.getFollowers();
        deals = user.getDealsCount();
        events = user.getEventsCount();

        isFollowing.set(user.isFollowing());
    }

    // ======= region getters =======

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getFollowers() {
        return followers;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setDeals(int deals) {
        this.deals = deals;
    }

    public void setEvents(int events) {
        this.events = events;
    }

    // endregion
}
