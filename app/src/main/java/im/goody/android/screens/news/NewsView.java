package im.goody.android.screens.news;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

import im.goody.android.Constants;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNewsBinding;

public class NewsView extends BaseView<NewsController, ScreenNewsBinding>
        implements SwipyRefreshLayout.OnRefreshListener {

    private NewsAdapter adapter;

    public NewsView(Context context, AttributeSet attrs) {
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

    //endregion

    @Override
    protected void onAttached() {
        setupRecyclerView();

        binding.mainNewsContainer.setOnRefreshListener(this);
        binding.mainNewsContainer.setColorSchemeResources(Constants.PROGRESS_COLORS);

        binding.mainNewsList.addOnScrollListener(loadMoreListener);
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

    void showData(List<NewsItemViewModel> data) {
        adapter = new NewsAdapter(data, controller);

        finishLoading();

        binding.mainNewsList.setAdapter(adapter);
    }

    void addData(List<NewsItemViewModel> items) {
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

    //endregion
}
