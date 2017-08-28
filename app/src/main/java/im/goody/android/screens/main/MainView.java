package im.goody.android.screens.main;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ScreenMainBinding;

import static im.goody.android.Constants.FAB_HIDE_THRESHOLD;

public class MainView extends BaseView<MainController, ScreenMainBinding> {
    private MainAdapter adapter;

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

        binding.mainNewsContainer.setMaterialRefreshListener(new DataListener());

        binding.mainNewFab.setOnClickListener(v -> controller.showNewPostScreen());
        binding.mainNewsList.addOnScrollListener(onScrollListener);

        binding.mainNewsContainer.autoRefresh();
    }

    @Override
    protected void onDetached() {

    }

    public void showData(List<Deal> data) {
        adapter = new MainAdapter(data, controller);

        finishRefresh();

        binding.mainNewsList.setVisibility(VISIBLE);
        binding.mainNewsList.setAdapter(adapter);
    }

    public void addData(List<Deal> items) {
        finishLoadMore();
        adapter.addData(items);
    }

    public void finishRefresh() {
        binding.mainNewsContainer.finishRefresh();
    }

    public void finishLoadMore() {
        binding.mainNewsContainer.finishRefreshLoadMore();
    }


    // region ========= MaterialRefreshListener =============

    private class DataListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            if (controller != null)
                controller.refreshData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            controller.loadMore();
        }
    }

    // endregion
}
