package im.goody.android.screens.finish_event;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import im.goody.android.data.dto.Participant;
import im.goody.android.databinding.ItemFinishParticipantBinding;
import im.goody.android.screens.common.recyclerview_binding.BindableAdapter;

public class FinishEventAdapter extends RecyclerView.Adapter<FinishEventAdapter.FinishEventHolder> implements BindableAdapter<Participant> {
    private List<Participant> participants = new ArrayList<>();
    private Handler handler;

    FinishEventAdapter(Handler handler) {
        this.handler = handler;
    }

    @NonNull
    @Override
    public FinishEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFinishParticipantBinding binding = ItemFinishParticipantBinding.inflate(inflater, parent, false);
        return new FinishEventHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishEventHolder holder, int position) {
        holder.bind(participants.get(position));
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    @Override
    public void setData(List<Participant> data) {
        participants = data;
        notifyDataSetChanged();
    }

    interface Handler {
        void removeParticipant(int position);

        void showProfile(long id);
    }

    public class FinishEventHolder extends RecyclerView.ViewHolder {
        private ItemFinishParticipantBinding binding;

        public FinishEventHolder(ItemFinishParticipantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.itemFinishEventClear.setOnClickListener(v -> handler.removeParticipant(getAdapterPosition()));

            binding.itemFinishEventAvatar.setOnClickListener(v -> {
                long id = participants.get(getAdapterPosition()).getId();
                handler.showProfile(id);
            });
        }

        public void bind(Participant participant) {
            binding.setData(participant);
        }
    }
}
