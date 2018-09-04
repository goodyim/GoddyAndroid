package im.goody.android.screens.participants;

import java.util.List;

import im.goody.android.data.dto.Participant;

public class ParticipantsViewModel {
    private List<Participant> data;

    public List<Participant> getData() {
        return data;
    }

    public void setData(List<Participant> data) {
        this.data = data;
    }
}
