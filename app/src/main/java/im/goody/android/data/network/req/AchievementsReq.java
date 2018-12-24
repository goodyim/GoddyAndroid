package im.goody.android.data.network.req;

import java.util.List;

public class AchievementsReq {
    private String message;
    private List<Long> participantIds;

    public AchievementsReq(String message, List<Long> participantIds) {
        this.message = message;
        this.participantIds = participantIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
}
