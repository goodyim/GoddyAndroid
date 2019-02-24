package im.goody.android.screens.feedback;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.Constants.NotificationExtra;
import im.goody.android.R;
import im.goody.android.data.dto.Feedback;
import im.goody.android.databinding.ItemFeedbackBinding;
import im.goody.android.databinding.ItemPhoneRequestBinding;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.FeedbackHolder> {
    private List<Feedback> data;
    private Handler handler;

    private static final int TYPE_FEEDBACK = R.layout.item_feedback;
    private static final int TYPE_REQUEST = R.layout.item_phone_request;

    public void removeItem(int position) {
        notifyItemRemoved(position);
    }

    public interface Handler {
        void openDetail(Feedback item);

        void openProfile(long id);

        void allow(int position);
        void deny(int position);
    }

    FeedBackAdapter(List<Feedback> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    @NonNull
    @Override
    public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new FeedbackHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        Feedback item = data.get(position);

        return item.getType().equals(NotificationExtra.TYPE_PHONE_REQUEST)
                ? TYPE_REQUEST
                : TYPE_FEEDBACK;
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class FeedbackHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        FeedbackHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Feedback feedback) {
            switch (getItemViewType()) {
                case TYPE_FEEDBACK:
                    bindFeedback(feedback);
                    break;
                case TYPE_REQUEST:
                    bindRequest(feedback);
                    break;
            }

            binding.executePendingBindings();

        }

        private void bindRequest(Feedback feedback) {
            ItemPhoneRequestBinding phoneRequestBinding = (ItemPhoneRequestBinding) binding;

            phoneRequestBinding.setData(feedback);

            phoneRequestBinding.getRoot().setOnClickListener(v -> handler.openProfile(feedback.getAuthor().getId()));

            phoneRequestBinding.requestAllow.setOnClickListener(v -> handler.allow(getAdapterPosition()));
            phoneRequestBinding.requestDeny.setOnClickListener(v -> handler.deny(getAdapterPosition()));
        }

        private void bindFeedback(Feedback feedback) {
            ItemFeedbackBinding feedbackBinding = (ItemFeedbackBinding) binding;

            feedbackBinding.setData(feedback);
            feedbackBinding.getRoot().setOnClickListener(v -> handler.openDetail(feedback));
        }
    }
}
