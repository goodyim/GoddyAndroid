package im.goody.android.screens.profile.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.databinding.ItemProfileEventBinding;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;

public class ProfileEventsAdapter extends RecyclerView.Adapter<ProfileEventsAdapter.ProfileEventsHolder> {
    private List<ProfileEventItemViewModel> data;
    private ProfileEventItemHandler handler;

    public ProfileEventsAdapter(List<ProfileEventItemViewModel> data, ProfileEventItemHandler handler) {
        this.data = data;
        this.handler = handler;
    }

    @Override
    public ProfileEventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProfileEventBinding binding = ItemProfileEventBinding.inflate(inflater, parent, false);
        return new ProfileEventsHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProfileEventsHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    interface ProfileEventItemHandler {
        void showDetail(long id);

        void share(String text);

        Observable<Deal> like(long id);

        void openPhoto(String imageUrl);
    }

    class ProfileEventsHolder extends RecyclerView.ViewHolder {
        private ItemProfileEventBinding binding;

        ProfileEventsHolder(ItemProfileEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProfileEventItemViewModel viewModel) {
            binding.setViewModel(viewModel);

            Deal deal = viewModel.getDeal();

            binding.eventImage.setOnClickListener(v ->
                    handler.openPhoto(NetUtils.buildDealImageUrl(deal)));

            binding.actionPanel.panelItemComments
                    .setOnClickListener(v -> handler.showDetail(deal.getId()));

            binding.actionPanel.panelItemShare.setOnClickListener(v -> {
                String text = TextUtils.buildShareText(deal);
                handler.share(text);
            });

            binding.actionPanel.panelLikeContainer.setOnClickListener(v ->
                    handler.like(deal.getId()).subscribe(response -> {
                        viewModel.getPanelViewModel().isLiked.set(response.isLiked());
                        viewModel.getPanelViewModel().likedCount.set(response.getLikesCount());
                    }, Throwable::printStackTrace));

            binding.getRoot().setOnClickListener(v -> handler.showDetail(deal.getId()));
        }
    }
}
