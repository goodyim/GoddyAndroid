package im.goody.android.screens.detail_post;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.databinding.ItemDetailEventBinding;
import im.goody.android.databinding.ItemDetailPostBinding;
import io.reactivex.Observable;

class DetailPostAdapter extends RecyclerView.Adapter<DetailPostAdapter.DetailPostHolder> {
    private DetailPostBodyViewModel viewModel;
    private DetailPostHandler handler;

    DetailPostAdapter(DetailPostBodyViewModel viewModel, DetailPostHandler handler) {
        this.viewModel = viewModel;
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
       if (position == 0) {
           return viewModel.getDeal().getEvent() == null
                   ? R.layout.item_detail_post
                   : R.layout.item_detail_event;
       } else {
           return R.layout.comment;
       }
    }

    @Override
    public void onBindViewHolder(DetailPostHolder holder, int position) {
        Object target = position == 0
                ? viewModel
                : viewModel.getDeal().getComments().get(position - 1);
        holder.bind(target);
    }

    @Override
    public int getItemCount() {
        return viewModel == null ? 0 : viewModel.getDeal().getComments().size() + 1;
    }

    void notifyCommentAdded() {
        notifyItemInserted(viewModel.getDeal().getComments().size());
    }

    interface DetailPostHandler {
        void share(Deal deal);

        void openMap(Location location);

        Observable<Deal> like();
    }

    class DetailPostHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        DetailPostHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object object) {
            binding.setVariable(BR.data, object);

            switch (getItemViewType()) {
                case R.layout.item_detail_event:
                    bindEvent((DetailPostBodyViewModel) object);
                    break;
                case R.layout.item_detail_post:
                    bindPost((DetailPostBodyViewModel) object);
            }

            binding.executePendingBindings();
        }

        private void bindPost(DetailPostBodyViewModel body) {
            ItemDetailPostBinding postBinding = (ItemDetailPostBinding) binding;

            postBinding.actionPanel.panelItemShare.setOnClickListener(v -> handler.share(body.getDeal()));

            postBinding.newsItemLocation
                    .setOnClickListener(v -> handler.openMap(body.getDeal().getLocation()));

            postBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like().subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));
        }

        private void bindEvent (DetailPostBodyViewModel body) {
            ItemDetailEventBinding eventBinding = (ItemDetailEventBinding) binding;

            eventBinding.actionPanel.panelItemShare.setOnClickListener(v -> handler.share(body.getDeal()));

            eventBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like().subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            eventBinding.detailEventLocation
                    .setOnClickListener(v -> handler.openMap(body.getDeal().getLocation()));
        }
    }
}
