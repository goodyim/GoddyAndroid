package im.goody.android.screens.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenProfileBinding;


public class ProfileView extends BaseView<ProfileController, ScreenProfileBinding>
        implements SwipeRefreshLayout.OnRefreshListener {
    private String title;
    private Activity activity;

    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        title = context.getString(R.string.profile);
    }

    @Override
    protected void onAttached() {
        binding.profileRefresh.setRefreshing(true);
        binding.profileRefresh.setColorSchemeResources(Constants.PROGRESS_COLORS);
        binding.profileRefresh.setOnRefreshListener(this);

        binding.profileFollow.setOnClickListener(v -> controller.follow());
        binding.profileShowPosts.setOnClickListener(v -> controller.showPosts());

        binding.profileAvatar.setOnClickListener(v -> controller.showAvatar());

        binding.toolbar.setNavigationOnClickListener(v -> controller.backClicked());

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.setTitle(title);
                    changeStatusColor(R.color.primary_dark);
                    isShow = true;
                } else if (isShow) {
                    binding.collapsingToolbar.setTitle(" ");
                    changeStatusColor(R.color.primary);
                    isShow = false;
                }
            }
        });
    }

    private void changeStatusColor(int colorRes) {
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(activity, colorRes);

            activity.getWindow().setStatusBarColor(color);
        }
    }

    @Override
    protected void onDetached() {
        changeStatusColor(R.color.primary_dark);
    }

    public void hideFollowButton() {
        binding.profileFollow.setVisibility(GONE);
    }

    @Override
    public void onRefresh() {
        controller.loadData();
    }

    public void setData(ProfileViewModel viewModel) {
        finishRefresh();
        binding.setUser(viewModel);
    }

    void takeActivity(Activity activity) {
        this.activity = activity;
    }

    public void startRefresh() {
        binding.profileRefresh.setRefreshing(true);
    }

    public void finishRefresh() {
        binding.profileRefresh.setRefreshing(false);
    }
}
