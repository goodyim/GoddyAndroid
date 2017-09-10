package im.goody.android.screens.detail_post;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ItemDetailPostBinding;

class DetailPostAdapter extends RecyclerView.Adapter<DetailPostAdapter.DetailPostHolder> {
    private Deal deal;
    private DetailPostHandler handler;

    DetailPostAdapter(Deal deal, DetailPostHandler handler) {
        this.deal = deal;
        this.handler = handler;
    }

    @Override
    public DetailPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType,
                parent, false);
        return new DetailPostHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? R.layout.item_detail_post : R.layout.comment;
    }

    @Override
    public void onBindViewHolder(DetailPostHolder holder, int position) {
        Object target = position == 0 ? deal : deal.getComments().get(position - 1);
        holder.bind(target);
    }

    @Override
    public int getItemCount() {
        return deal == null ? 0 : deal.getComments().size() + 1;
    }

    void notifyCommentAdded() {
        notifyItemChanged(0);
        notifyItemInserted(deal.getComments().size());
    }

    interface DetailPostHandler {
        void share(Deal deal);
    }

    class DetailPostHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        DetailPostHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object object) {
            binding.setVariable(BR.data, object);

            if (object instanceof Deal) {
                bindPost((Deal) object);
            }

            binding.executePendingBindings();
        }

        private void bindPost(Deal deal) {
            ItemDetailPostBinding postBinding = (ItemDetailPostBinding) binding;

            postBinding.actionPanel.panelItemShare.setOnClickListener(v -> handler.share(deal));
        }
    }
}
