package im.goody.android.screens.participants;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.data.dto.Participant;
import im.goody.android.databinding.ItemParticipantBinding;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsHolder> {
    private List<Participant> data;
    private Handler handler;

    ParticipantsAdapter(List<Participant> data, Handler handler) {
        this.data = data;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ParticipantsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemParticipantBinding binding = ItemParticipantBinding.inflate(inflater, parent, false);
        return new ParticipantsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface Handler {
        void itemCLicked(long id);
    }

    class ParticipantsHolder extends RecyclerView.ViewHolder {
        private ItemParticipantBinding binding;

        ParticipantsHolder(ItemParticipantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Participant participant) {
            binding.setData(participant);

            binding.getRoot().setOnClickListener(v -> handler.itemCLicked(participant.getId()));
        }
    }
}
