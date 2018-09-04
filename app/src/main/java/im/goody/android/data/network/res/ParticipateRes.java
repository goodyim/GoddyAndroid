package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipateRes {
    private boolean participates;

    @JsonProperty("aasm_state")
    private String status;

    @JsonProperty("last_participants")
    private List<String> lastParticipants;

    @JsonProperty("participants_count")
    private int participantsCount;

    public List<String> getLastParticipants() {
        return lastParticipants;
    }

    public void setLastParticipants(List<String> lastParticipants) {
        this.lastParticipants = lastParticipants;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
    }

    public String getStatus() {
        return status;
    }

    public boolean isParticipates() {
        return participates;
    }

    public ParticipateRes setParticipates(boolean participates) {
        this.participates = participates;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
