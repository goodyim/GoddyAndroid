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
import im.goody.android.data.dto.Deal;
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

    private RecyclerView.OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            FloatingActionButton fab = binding.mainMenuFab;
            if (dy > FAB_HIDE_THRESHOLD && fab.isShown() && !isMenuOpened)
                fab.hide();
            else if (dy < -FAB_HIDE_THRESHOLD && !fab.isShown())
                fab.show();
        }
    };

    @Override
    protected void onAttached() {
        binding.mainNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainNewsList.setHasFixedSize(true);
        binding.mainNewsList.setAdapter(null);
        binding.mainNewsList.setVisibility(GONE);

        binding.mainNewsContainer.setOnRefreshListener(this);
        binding.mainNewsContainer.setColorSchemeResources(Constants.PROGRESS_COLORS);

        binding.mainMenuFab.setOnClickListener(v -> {
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
        binding.mainNewsList.addOnScrollListener(onScrollListener);

        binding.mainNewsContainer.post(() -> binding.mainNewsContainer.setRefreshing(true));
    }

    @Override
    protected void onDetached() {

    }

    public void showData(List<Deal> data) {
        adapter = new MainAdapter(data, controller);

        finishLoading();

        binding.mainNewsList.setVisibility(VISIBLE);
        binding.mainNewsList.setAdapter(adapter);
    }

    public void addData(List<Deal> items) {
        finishLoading();
        adapter.addData(items);
    }

    public void finishLoading() {
        binding.mainNewsContainer.post(() -> binding.mainNewsContainer.setRefreshing(false));
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

    private void rotateMenuFab() {
        FloatingActionButton fab = binding.mainMenuFab;
        float degrees = 45 * (isMenuOpened ? -1 : 1);
        fab.animate()
                .rotationBy(degrees)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }
}
