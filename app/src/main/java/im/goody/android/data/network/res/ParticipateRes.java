package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticipateRes {
    private boolean participates;

    @JsonProperty("aasm_state")
    private String status;

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
