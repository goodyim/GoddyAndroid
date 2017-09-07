package im.goody.android.screens.detail_post;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import im.goody.android.Constants;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenDetailBinding;
import im.goody.android.utils.UIUtils;

public class DetailPostView extends BaseView<DetailPostController, ScreenDetailBinding>
        implements SwipeRefreshLayout.OnRefreshListener {
    private DetailPostAdapter adapter;

    public DetailPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.detailPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.detailPostList.setHasFixedSize(true);
        binding.detailPostList.setAdapter(null);

        binding.detailCommentSend.setOnClickListener(v -> {
            UIUtils.hideKeyboard(getFocusedChild());
            controller.sendComment();
        });

        binding.detailPostRefresh.setOnRefreshListener(this);
        binding.detailPostRefresh.setColorSchemeResources(Constants.PROGRESS_COLORS);

        startLoading();
    }

    @Override
    protected void onDetached() {

    }

    // ======= region OnRefreshListener =======

    @Override
    public void onRefresh() {
        controller.loadData();
    }

    // endregion

    public void setData(DetailPostViewModel data) {
        binding.setData(data);
    }

    public void scrollToPosition(int position) {
        binding.detailPostList.scrollToPosition(position);
    }

    public int getCurrentPosition() {
        return ((LinearLayoutManager) binding.detailPostList.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    public void showData(DetailPostViewModel data) {
        adapter = new DetailPostAdapter(data.getDeal());

        finishLoading();

        binding.detailPostList.setAdapter(adapter);

        scrollToPosition(data.getPosition());
    }

    public void commentCreated() {
        hideCommentProgress();
        if (adapter != null) {
            adapter.notifyCommentAdded();
            binding.detailPostList.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    public void finishLoading() {
        binding.detailPostRefresh.setRefreshing(false);

        if (binding.detailPostList.getVisibility() != VISIBLE)
            binding.detailPostList.setVisibility(VISIBLE);
    }

    public void startLoading() {
        binding.detailPostRefresh.setRefreshing(true);
    }

    public void hideCommentProgress() {
        binding.commentProgress.setVisibility(GONE);
    }

    public void showCommentProgress() {
        binding.commentProgress.setVisibility(VISIBLE);
    }
}
