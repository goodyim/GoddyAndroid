package im.goody.android.screens.detail_post;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ScreenDetailBinding;

public class DetailPostView extends BaseView<DetailPostController, ScreenDetailBinding> {
    private DetailPostAdapter adapter;

    public DetailPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.detailPostList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.detailPostList.setHasFixedSize(true);
        binding.detailPostList.setAdapter(null);

        startLoading();
    }

    @Override
    protected void onDetached() {

    }

    public void scrollToPosition(int position) {
        binding.detailPostList.scrollToPosition(position);
    }

    public int getCurrentPosition() {
        return ((LinearLayoutManager) binding.detailPostList.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    public void showData(Deal data) {
        adapter = new DetailPostAdapter(data);

        finishLoading();

        binding.detailPostList.setAdapter(adapter);
    }

    public void finishLoading() {
        binding.detailPostProgress.setVisibility(GONE);
        binding.detailPostList.setVisibility(VISIBLE);
    }

    public void startLoading() {
        binding.detailPostProgress.setVisibility(VISIBLE);
        binding.detailPostList.setVisibility(GONE);
    }
}
