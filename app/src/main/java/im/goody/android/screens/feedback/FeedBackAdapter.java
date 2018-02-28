package im.goody.android.screens.feedback;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.data.dto.Feedback;
import im.goody.android.databinding.ItemFeedbackBinding;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedbackHolder> {
    private List<Feedback> data;
    private Handler handler;

    public interface Handler {
        void openDetail(Feedback item);
    }

    public FeedBackAdapter(List<Feedback> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    @Override
    public FeedbackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFeedbackBinding binding = ItemFeedbackBinding.inflate(inflater, parent, false);
        return new FeedbackHolder(binding);
    }

    @Override
    public void onBindViewHolder(FeedbackHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class FeedbackHolder extends RecyclerView.ViewHolder {
        private ItemFeedbackBinding binding;

        FeedbackHolder(ItemFeedbackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Feedback feedback) {
            binding.setData(feedback);
            binding.executePendingBindings();

            binding.getRoot().setOnClickListener(v -> handler.openDetail(feedback));
        }
    }
}
