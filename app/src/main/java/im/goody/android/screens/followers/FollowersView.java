package im.goody.android.screens.followers;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Follower;
import im.goody.android.databinding.ScreenFollowersBinding;

public class FollowersView extends BaseView<FollowersController, ScreenFollowersBinding> implements FollowersAdapter.Handler {
    public FollowersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.followersList.setHasFixedSize(true);
    }

    @Override
    protected void onDetached() {

    }

    public void setData(List<Follower> data) {
        FollowersAdapter adapter = new FollowersAdapter(data, this);
        binding.followersList.setAdapter(adapter);
    }

    @Override
    public void itemCLicked(long id) {
        controller.openProfile(id);
    }
}
