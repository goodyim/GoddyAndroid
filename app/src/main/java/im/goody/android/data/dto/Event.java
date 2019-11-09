package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {
    public static final String ACTIVE = "active";
    public static final String IN_PROGRESS = "in_process";
    public static final String CLOSED = "finished";

    private String resources;
    private String date;

    private boolean immediately = false;

    @JsonProperty("aasm_state")
    private String state;

    @JsonProperty("participants")
    private int participantsCount;

    @JsonProperty("phone")
    private PhoneInfo phoneInfo;

    @JsonProperty("last_participants")
    private List<String> lastParticipantsAvatars;

    public List<String> getLastParticipantsAvatars() {
        return lastParticipantsAvatars;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setImmediately(boolean immediately) {
        this.immediately = immediately;
    }

    public String getResources() {
        return resources;
    }

    public String getDate() {
        return date;
    }

    public void setLastParticipantsAvatars(List<String> lastParticipantsAvatars) {
        this.lastParticipantsAvatars = lastParticipantsAvatars;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    public void setPhoneInfo(PhoneInfo phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isImmediately() {
        return immediately;
    }

    public boolean isOpen() {
        return !state.equals(CLOSED);
    }

    public PhoneInfo getPhoneInfo() {
        return phoneInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhoneInfo {
        public static final int STATE_ALLOWED = 0;
        public static final int STATE_REQUEST_NEEDED = 1;
        public static final int STATE_REQUESTED = 2;
        public static final int STATE_FORBIDDEN = 3;

        public static final int STATE_UNDEFINED = -1;

        public static final int VISIBILITY_ALL = 0;
        public static final int VISIBILITY_NOBODY = 1;
//        public static final int VISIBILITY_PARTICIPANTS = 2;
//        public static final int VISIBILITY_REQUEST = 3;

        private int state;

        private String content;

        private Integer visibility;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getVisibility() {
            return visibility;
        }

        public void setVisibility(Integer visibility) {
            this.visibility = visibility;
        }
    }
}
