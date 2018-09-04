package im.goody.android.screens.detail_post;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Comment;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.databinding.CommentBinding;
import im.goody.android.databinding.ItemDetailEventBinding;
import im.goody.android.databinding.ItemDetailPostBinding;
import im.goody.android.utils.NetUtils;
import io.reactivex.Observable;

class DetailPostAdapter extends RecyclerView.Adapter<DetailPostAdapter.DetailPostHolder> {
    private DetailPostBodyViewModel viewModel;
    private DetailPostHandler handler;
    private CommentOptionsDialog commentDialog;
    private long userId;

    DetailPostAdapter(DetailPostBodyViewModel viewModel, DetailPostHandler handler, long userId) {
        this.viewModel = viewModel;
        this.handler = handler;
        this.userId = userId;

        commentDialog = new CommentOptionsDialog();
    }

    @NonNull
    @Override
    public DetailPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull DetailPostHolder holder, int position) {
        Object target = position == 0
                ? viewModel
                : viewModel.getDeal().getComments().get(position - 1);
        holder.bind(target);
    }

    @Override
    public int getItemCount() {
        return viewModel == null ? 0 : viewModel.getDeal().getComments().size() + 1;
    }

    void notifyCommentRemoved(int number) {
        notifyItemRemoved(number + 1);
    }

    void notifyCommentAdded() {
        notifyItemInserted(viewModel.getDeal().getComments().size());
    }

    interface DetailPostHandler {
        void share(Deal deal);

        void openMap(Location location);

        void openProfile(String id);

        Observable<ParticipateRes> changeParticipateState();

        Observable<Deal> like();

        void openPhoto(String s);

        void reply(String author);

        void deleteComment(int commentPosition);

        void openParticipants(long id);
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
                    break;
                case R.layout.comment:
                    bindComment((Comment) object);
            }

            binding.executePendingBindings();
        }

        private void bindComment(Comment comment) {
            CommentBinding commentBinding = (CommentBinding) binding;

            commentBinding.commentAvatar.setOnClickListener(v ->
                    handler.openProfile(String.valueOf(comment.getAuthor().getId()))
            );

            commentBinding.commentBody.setMentionListener(handler::openProfile);

            commentBinding.commentContainer.setOnClickListener(v -> {
                boolean isShowDelete = viewModel.getDeal().isOwner() || userId == comment.getAuthor().getId();
                int itemsId = isShowDelete ? R.array.comment_options_extended : R.array.comment_options;

                commentDialog.show(commentBinding.getRoot().getContext(), itemsId)
                        .subscribe(id -> {
                            switch (id) {
                                case CommentOptionsDialog.ACTION_REPLY:
                                    handler.reply(comment.getAuthor().getName());
                                    break;
                                case CommentOptionsDialog.ACTION_DELETE:
                                    handler.deleteComment(getCommentPosition());
                            }
                        });
            });
        }

        private void bindPost(DetailPostBodyViewModel body) {
            ItemDetailPostBinding postBinding = (ItemDetailPostBinding) binding;
            Deal deal = body.getDeal();

            postBinding.actionPanel.panelItemShare.setOnClickListener(v -> handler.share(deal));

            postBinding.newsItemLocation
                    .setOnClickListener(v -> handler.openMap(deal.getLocation()));

            postBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like().subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            postBinding.newsItemUserAvatar.setOnClickListener(v ->
                    handler.openProfile(String.valueOf(deal.getAuthor().getId())));

            postBinding.newsItemImage.setOnClickListener(v ->
                    handler.openPhoto(NetUtils.buildDealImageUrl(deal)));

            postBinding.newsItemDescription.setMentionListener(handler::openProfile);
        }

        private void bindEvent(DetailPostBodyViewModel body) {
            Deal deal = body.getDeal();

            ItemDetailEventBinding eventBinding = (ItemDetailEventBinding) binding;

            eventBinding.actionPanel.panelItemShare.setOnClickListener(v -> handler.share(deal));

            eventBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like().subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            eventBinding.detailEventLocation
                    .setOnClickListener(v -> handler.openMap(deal.getLocation()));

            eventBinding.detailEventJoin.setOnClickListener(v ->
                    handler.changeParticipateState()
                            .subscribe(
                                    response -> {
                                        viewModel.state.set(response.getStatus());
                                        viewModel.participates.set(response.isParticipates());
                                        viewModel.lastParticipants.set(response.getLastParticipants());
                                        viewModel.participantsCount.set(response.getParticipantsCount());
                                    },
                                    Throwable::printStackTrace));

            eventBinding.detailEventAvatar.setOnClickListener(v ->
                    handler.openProfile(String.valueOf(deal.getAuthor().getId())));

            eventBinding.detailEventImage.setOnClickListener(v ->
                    handler.openPhoto(NetUtils.buildDealImageUrl(deal)));

            eventBinding.detailEventDescription.setMentionListener(handler::openProfile);

            eventBinding.detailEventParticipants.setOnClickListener(v -> handler.openParticipants(deal.getId()));
            eventBinding.detailEventParticipantsCount.setOnClickListener(v -> handler.openParticipants(deal.getId()));
        }

        private int getCommentPosition() {
            return getAdapterPosition() - 1;
        }
    }
}
