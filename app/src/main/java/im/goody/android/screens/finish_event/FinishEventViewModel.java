package im.goody.android.screens.finish_event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import im.goody.android.BR;
import im.goody.android.data.dto.Participant;
import im.goody.android.data.network.req.AchievementsReq;

public class FinishEventViewModel extends BaseObservable {

    @Bindable
    private List<Participant> participants = null;

    public final ObservableField<String> thanksContent = new ObservableField<>("");

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
        notifyPropertyChanged(BR.participants);
    }

    public void removeParticipant(int position) {
        participants.remove(position);
        notifyPropertyChanged(BR.participants);
    }

    public AchievementsReq body() {
        List<Long> ids = new ArrayList<>();

        for (Participant participant : participants) {
            ids.add(participant.getId());
        }

        return new AchievementsReq(thanksContent.get(), ids);
    }
}
