package im.goody.android.screens.main;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ScreenMainBinding;

import static im.goody.android.Constants.FAB_HIDE_THRESHOLD;

public class MainView extends BaseView<MainController, ScreenMainBinding> implements SwipeRefreshLayout.OnRefreshListener {

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private RecyclerView.OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            FloatingActionButton fab = binding.mainNewFab;
            if (dy > FAB_HIDE_THRESHOLD && fab.isShown())
                fab.hide();
            else if (dy < -FAB_HIDE_THRESHOLD &&!fab.isShown())
                fab.show();
        }
    };

    @Override
    protected void onAttached() {
        binding.mainNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainNewsList.setHasFixedSize(true);
        binding.mainNewsList.setAdapter(null);
        binding.mainNewsList.setVisibility(GONE);
        binding.progressBar.setVisibility(VISIBLE);

        binding.mainNewsContainer.setOnRefreshListener(this);
        binding.mainNewFab.setOnClickListener(v -> controller.showNewPostScreen());

        binding.mainNewsList.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void onDetached() {

    }

    public void showData(List<Deal> data) {
        if (binding.mainNewsContainer.isRefreshing())
            binding.mainNewsContainer.setRefreshing(false);
        binding.progressBar.setVisibility(GONE);
        binding.mainNewsList.setVisibility(VISIBLE);
        binding.mainNewsList.setAdapter(new MainAdapter(data));
    }

    // region ========= OnRefreshListener =============

    @Override
    public void onRefresh() {
        binding.mainNewsContainer.setRefreshing(true);
        controller.refreshData();
    }

    // endregion
}
