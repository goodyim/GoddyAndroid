package im.goody.android.screens.followers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.data.dto.Follower;
import im.goody.android.databinding.ItemFollowerBinding;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowersHolder> {
    private List<Follower> data;
    private Handler handler;

    FollowersAdapter(List<Follower> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    @NonNull
    @Override
    public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFollowerBinding binding = ItemFollowerBinding.inflate(inflater, parent, false);
        return new FollowersHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface Handler {
        void itemCLicked(long id);
    }

    class FollowersHolder extends RecyclerView.ViewHolder {
        private ItemFollowerBinding binding;

        FollowersHolder(ItemFollowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Follower participant) {
            binding.setData(participant);

            binding.getRoot().setOnClickListener(v -> handler.itemCLicked(participant.getId()));
        }
    }
}

