package im.goody.android.data.network.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventStateRes {
    @JsonProperty("aasm_state")
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
