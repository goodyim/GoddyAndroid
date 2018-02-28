package im.goody.android.screens.feedback;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Feedback;
import im.goody.android.databinding.ScreenFeedbackBinding;


public class FeedBackView extends BaseView<FeedBackController, ScreenFeedbackBinding> implements FeedBackAdapter.Handler {
    public FeedBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.feedbackList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.feedbackList.setHasFixedSize(true);
    }

    @Override
    protected void onDetached() {

    }

    void showData(List<Feedback> data) {
        if (data.size() > 0) {
            binding.feedbackList.setVisibility(VISIBLE);
            binding.feedbackPlaceholder.setVisibility(GONE);

            binding.feedbackList.setAdapter(new FeedBackAdapter(data, this));
        } else {
            binding.feedbackList.setVisibility(GONE);
            binding.feedbackPlaceholder.setVisibility(VISIBLE);
        }
    }

    @Override
    public void openDetail(Feedback item) {
        controller.openDetail(item);
    }
}
