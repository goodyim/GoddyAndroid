package im.goody.android.screens.main;

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
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.databinding.ItemEventBinding;
import im.goody.android.databinding.ItemNewsBinding;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private static final int TYPE_POST = R.layout.item_news;
    private static final int TYPE_EVENT = R.layout.item_event;
    private List<MainItemViewModel> data;
    private MainItemHandler handler;

    MainAdapter(List<MainItemViewModel> data, MainItemHandler handler) {
        this.data = data;
        this.handler = handler;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType,
                parent, false);
        return new MainHolder(binding);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
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

    void addData(List<MainItemViewModel> items) {
        int size = getItemCount();
        data.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    interface MainItemHandler {
        void report(long id);

        void showDetail(long id);

        void share(String text);

        void openMap(Location location);

        void openProfile(long id);

        Observable<Deal> like(long id);

        Observable<ParticipateRes> changeParticipateState(long id);
    }

    class MainHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        MainHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MainItemViewModel model) {
            binding.setVariable(BR.viewModel, model);

            if (getItemViewType() == TYPE_POST) {
                bindPost(model);
            } else {
                bindEvent(model);
            }

            binding.executePendingBindings();
        }

        private void bindEvent(MainItemViewModel viewModel) {
            Deal deal = viewModel.getDeal();
            ItemEventBinding eventBinding = (ItemEventBinding) binding;

            eventBinding.itemEventContainer
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            eventBinding.actionPanel.panelItemComments
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            eventBinding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = TextUtils.buildShareText(deal);
                handler.share(text);
            });

            eventBinding.itemEventMenu.setOnClickListener(v -> MainItemMenu.show(v).subscribe(id -> {
                switch (id) {
                    case R.id.action_report:
                        handler.report(deal.getId());
                }
            }));

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
                                    response ->
                                            viewModel.participates.set(response.isParticipates()),
                                    Throwable::printStackTrace));

            eventBinding.itemEventAvatar.setOnClickListener(v ->
                    handler.openProfile(deal.getAuthor().getId()));
        }


        private void bindPost(MainItemViewModel viewModel) {
            Deal deal = viewModel.getDeal();
            ItemNewsBinding postBinding = (ItemNewsBinding) binding;

            postBinding.newsItemContainer
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            postBinding.actionPanel.panelItemComments
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            postBinding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = TextUtils.buildShareText(deal);
                handler.share(text);
            });

            postBinding.newItemMenu.setOnClickListener(v -> MainItemMenu.show(v).subscribe(id -> {
                switch (id) {
                    case R.id.action_report:
                        handler.report(deal.getId());
                }
            }));

            postBinding.newsItemLocation
                    .setOnClickListener(v -> handler.openMap(deal.getLocation()));

            postBinding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like(deal.getId()).subscribe(response -> {
                        viewModel.panelViewModel.isLiked.set(response.isLiked());
                        viewModel.panelViewModel.likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            postBinding.newsItemUserAvatar.setOnClickListener(v ->
                    handler.openProfile(deal.getAuthor().getId()));
        }
    }
}
