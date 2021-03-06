package im.goody.android.screens.main;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import im.goody.android.Constants;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenMainBinding;

import static im.goody.android.Constants.DEFAULT_ANIMATION_DURATION;
import static im.goody.android.Constants.FAB_HIDE_THRESHOLD;

public class MainView extends BaseView<MainController, ScreenMainBinding>
        implements SwipyRefreshLayout.OnRefreshListener {

    private MainAdapter adapter;
    private boolean isMenuOpened = false;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // ======= region listeners =======

    private RecyclerView.OnScrollListener loadMoreListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int position = linearLayoutManager.findLastVisibleItemPosition();
            int itemCount = recyclerView.getAdapter().getItemCount();
            int updatePosition = itemCount < Constants.PAGE_ITEM_COUNT ?
                    Constants.PAGE_ITEM_COUNT / 2 : itemCount - 1 - Constants.PAGE_ITEM_COUNT / 2;
            if (position > updatePosition) {
                removeScrollListener();
                controller.loadMore();
            }
        }
    };

    private RecyclerView.OnScrollListener hideShowFabListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            FloatingActionButton fab = binding.mainCreateFab;
            if (dy > FAB_HIDE_THRESHOLD && fab.isShown() && !isMenuOpened)
                fab.hide();
            else if (dy < -FAB_HIDE_THRESHOLD && !fab.isShown())
                fab.show();
        }
    };

    //endregion

    @Override
    protected void onAttached() {
        setupRecyclerView();

        binding.mainNewsContainer.setOnRefreshListener(this);
        binding.mainNewsContainer.setColorSchemeResources(Constants.PROGRESS_COLORS);

        binding.mainCreateFab.setOnClickListener(v -> {
            rotateMenuFab();
            if (isMenuOpened) {
                binding.mainNewEvent.hide();
                binding.mainNewPost.hide();
            } else {
                binding.mainNewPost.show();
                binding.mainNewEvent.show();
            }
            isMenuOpened = !isMenuOpened;
        });

        binding.mainNewPost.setOnClickListener(v -> controller.showNewPostScreen());
        binding.mainNewEvent.setOnClickListener(v -> controller.showNewEventScreen());

        binding.mainNewsList.addOnScrollListener(loadMoreListener);
        binding.mainNewsList.addOnScrollListener(hideShowFabListener);
    }

    @Override
    protected void onDetached() {
        removeScrollListener();
    }

    void addScrollListener() {
        binding.mainNewsList.addOnScrollListener(loadMoreListener);
    }

    void removeScrollListener() {
        binding.mainNewsList.removeOnScrollListener(loadMoreListener);
    }

    void showData(List<MainItemViewModel> data) {
        adapter = new MainAdapter(data, controller);

        finishLoading();

//        if (binding.mainNewsList.getVisibility() == GONE)
//            binding.mainNewsList.setVisibility(VISIBLE);

        binding.mainNewsList.setAdapter(adapter);
    }

    void addData(List<MainItemViewModel> items) {
        finishLoading();
        adapter.addData(items);
    }

    void finishLoading() {
        if (binding.mainProgress.getVisibility() == VISIBLE)
            binding.mainProgress.setVisibility(GONE);

        if (binding.mainNewsContainer.isRefreshing())
            binding.mainNewsContainer.setRefreshing(false);
    }

    void startLoading() {
        binding.mainNewsContainer.setRefreshing(true);
    }

    void scrollToPosition(int position) {
        binding.mainNewsList.scrollToPosition(position);
    }

    void hideNewButton() {
        binding.mainCreateFab.setVisibility(GONE);
        binding.mainNewsList.removeOnScrollListener(hideShowFabListener);
    }

    int getCurrentPosition() {
        return ((LinearLayoutManager) binding.mainNewsList.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    // region ========= MaterialRefreshListener =============

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        switch (direction) {
            case TOP:
                controller.refreshData();
                break;
            case BOTTOM:
                controller.loadMore();
        }
    }

    // endregion

    // ======= region private methods =======

    private void setupRecyclerView() {
        binding.mainNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainNewsList.setHasFixedSize(true);
        binding.mainNewsList.setAdapter(null);
    }

    private void rotateMenuFab() {
        FloatingActionButton fab = binding.mainCreateFab;
        float degrees = 45 * (isMenuOpened ? 0 : 1);
        fab.animate()
                .rotation(degrees)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }

    //endregion
}
