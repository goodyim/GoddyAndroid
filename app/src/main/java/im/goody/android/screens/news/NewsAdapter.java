package im.goody.android.screens.news;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.BR;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.databinding.ItemEventBinding;
import im.goody.android.databinding.ItemPostBinding;
import im.goody.android.screens.news.NewsItemMenu.ChangeState;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

@SuppressWarnings("WeakerAccess")
class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private static final int TYPE_POST = R.layout.item_post;
    private static final int TYPE_EVENT = R.layout.item_event;
    private List<NewsItemViewModel> data;
    private MainItemHandler handler;

    NewsAdapter(List<NewsItemViewModel> data, MainItemHandler handler) {
        this.data = data;
        this.handler = handler;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType,
                parent, false);
        return new NewsHolder(binding);
    }

    @Override
    public void onViewRecycled(NewsHolder holder) {
        super.onViewRecycled(holder);

        holder.binding.unbind();
        holder.binding.setVariable(BR.viewModel, null);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getDeal().getEvent() == null ? TYPE_POST : TYPE_EVENT;
    }

    void addData(List<NewsItemViewModel> items) {
        int size = getItemCount();
        data.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    void removeItem(NewsItemViewModel viewModel) {
        int position = data.indexOf(viewModel);
        data.remove(viewModel);
        notifyItemRemoved(position);
    }

    interface MainItemHandler {
        void report(long id);

        void showDetail(long id);

        void share(String text);

        void openMap(Location location);

        void openProfile(String id);

        Observable<Deal> like(long id);

        Observable<ParticipateRes> changeParticipateState(long id);

        Observable<EventStateRes> changeEventState(long id);

        void showEdit(Deal deal);

        void openPhoto(String imageUrl);

        Observable<ResponseBody> deletePost(long id);
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        NewsHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NewsItemViewModel model) {
            binding.setVariable(BR.viewModel, model);
            binding.executePendingBindings();

            if (getItemViewType() == TYPE_POST) {
                bindPost(model);
            } else {
                bindEvent(model);
            }
        }

        private void bindEvent(NewsItemViewModel viewModel) {
            Deal deal = viewModel.getDeal();
            ItemEventBinding eventBinding = (ItemEventBinding) binding;

            eventBinding.eventContainer
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            eventBinding.actionPanel.panelItemComments
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            eventBinding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = TextUtils.buildShareText(deal);
                handler.share(text);
            });


            eventBinding.itemEventMenu.setOnClickListener(v -> {
                ChangeState changeState;

                if (!deal.isOwner()) {
                    changeState = ChangeState.HIDDEN;
                } else if (deal.getEvent().isOpen()) {
                    changeState = ChangeState.CLOSE;
                } else {
                    changeState = ChangeState.OPEN;
                }

                NewsItemMenu menu = new NewsItemMenu.Builder()
                        .setChangeState(changeState)
                        .setShowEdit(deal.isOwner())
                        .setShowDelete(deal.isOwner())
                        .build();

                menu.show(v).subscribe(id -> {
                    switch (id) {
                        case R.id.action_report:
                            handler.report(deal.getId());
                            break;
                        case R.id.action_change_event_state:
                            handler.changeEventState(deal.getId())
                                    .subscribe(viewModel::changeEventState,
                                            Throwable::printStackTrace);
                            break;
                        case R.id.action_edit_post:
                            handler.showEdit(deal);
                            break;
                        case R.id.action_delete_post:
                            handler.deletePost(deal.getId())
                                    .subscribe(response ->
                                                    removeItem(viewModel),
                                            Throwable::printStackTrace);
                    }
                });
            });

            eventBinding.itemEventLocation
                    .setOnClickListener(v -> handler.openMap(deal.getLocation()));

            eventBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like(deal.getId()).subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            eventBinding.itemEventJoin.setOnClickListener(v ->
                    handler.changeParticipateState(deal.getId())
                            .subscribe(
                                    response -> {
                                        viewModel.participates.set(response.isParticipates());
                                        viewModel.state.set(response.getStatus());

                                        if (response.isParticipates()) {
                                            handler.showDetail(viewModel.getDeal().getId());
                                        }
                                    },
                                    Throwable::printStackTrace));

            eventBinding.itemEventAvatar.setOnClickListener(v ->
                    handler.openProfile(String.valueOf(deal.getAuthor().getId())));

            eventBinding.itemEventImage.setOnClickListener(v ->
                    handler.openPhoto(NetUtils.buildDealImageUrl(deal)));

            eventBinding.itemEventDescription.setMentionListener(handler::openProfile);
        }


        private void bindPost(NewsItemViewModel viewModel) {
            Deal deal = viewModel.getDeal();
            ItemPostBinding postBinding = (ItemPostBinding) binding;

            postBinding.newsItemContainer
                    .setOnClickListener(v -> {
                        handler.showDetail(deal.getId());
                    });

            postBinding.actionPanel.panelItemComments
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            postBinding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = TextUtils.buildShareText(deal);
                handler.share(text);
            });

            postBinding.newItemMenu.setOnClickListener(v -> {
                NewsItemMenu menu = new NewsItemMenu.Builder()
                        .setChangeState(ChangeState.HIDDEN)
                        .setShowEdit(deal.isOwner())
                        .setShowDelete(deal.isOwner())
                        .build();

                menu.show(v).subscribe(id -> {
                    switch (id) {
                        case R.id.action_report:
                            handler.report(deal.getId());
                            break;
                        case R.id.action_edit_post:
                            handler.showEdit(deal);
                            break;
                        case R.id.action_delete_post:
                            handler.deletePost(deal.getId())
                                    .subscribe(response ->
                                                    removeItem(viewModel),
                                            Throwable::printStackTrace);
                    }
                });
            });

            postBinding.newsItemLocation
                    .setOnClickListener(v -> handler.openMap(deal.getLocation()));

            postBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like(deal.getId()).subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            postBinding.newsItemUserAvatar.setOnClickListener(v ->
                    handler.openProfile(String.valueOf(deal.getAuthor().getId())));

            postBinding.newsItemImage.setOnClickListener(v ->
                    handler.openPhoto(NetUtils.buildDealImageUrl(deal)));

            postBinding.newsItemDescription.setMentionListener(handler::openProfile);
        }
    }
}
