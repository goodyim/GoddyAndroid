package im.goody.android.screens.participants;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.data.dto.Participant;
import im.goody.android.databinding.ScreenParticipantsBinding;

public class ParticipantsView extends BaseView<ParticipantsController, ScreenParticipantsBinding> implements ParticipantsAdapter.Handler {

    public ParticipantsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.participantList.setHasFixedSize(true);
    }

    @Override
    protected void onDetached() {

    }

    public void showData(List<Participant> data) {
        ParticipantsAdapter adapter = new ParticipantsAdapter(data, this);
        binding.participantList.setAdapter(adapter);
    }

    @Override
    public void itemCLicked(long id) {
        controller.itemClicked(id);
    }
}
