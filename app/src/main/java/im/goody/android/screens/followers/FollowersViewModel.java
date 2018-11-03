package im.goody.android.screens.followers;

import java.util.List;

import im.goody.android.data.dto.Follower;

public class FollowersViewModel {
    private List<Follower> data;

    public List<Follower> getData() {
        return data;
    }

    public void setData(List<Follower> data) {
        this.data = data;
    }
}
