package im.goody.android.screens.profile;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.net.Uri;

import im.goody.android.data.dto.User;
import im.goody.android.data.network.res.FollowRes;

public class ProfileViewModel {
    private String name;
    private String id;
    private String avatarUrl;
    private int deals;
    private int events;
    private String registrationDate;

    public final boolean isMineProfile;

    public final ObservableBoolean isFollowing = new ObservableBoolean();

    public final ObservableInt followers = new ObservableInt();

    private Uri tempImage;

    ProfileViewModel(User user, boolean isMineFrofile) {
        name = user.getName();
        id = String.valueOf(user.getId());
        avatarUrl = user.getAvatarUrl();
        deals = user.getDealsCount();
        events = user.getEventsCount();
        registrationDate = user.getRegistrationDate();

        this.isMineProfile = isMineFrofile;

        followers.set(user.getFollowers());
        isFollowing.set(user.isFollowing());
    }

    // ======= region getters =======

    public String getId() {
        return id;
    }

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

    public void setTempImage(Uri tempImage) {
        this.tempImage = tempImage;
    }

    public Uri getTempImage() {
        return tempImage;
    }
    // endregion
}
