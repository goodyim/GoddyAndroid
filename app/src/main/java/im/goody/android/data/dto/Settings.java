package im.goody.android.data.dto;

public class Settings {
    private boolean alertFromSubscriber;
    private boolean alertFromNearby;
    private boolean notifyMentions;
    private boolean notifyMessages;

    public Settings(boolean alertFromSubscriber, boolean alertFromNearby, boolean notifyMentions, boolean notifyMessages) {
        this.alertFromSubscriber = alertFromSubscriber;
        this.alertFromNearby = alertFromNearby;
        this.notifyMentions = notifyMentions;
        this.notifyMessages = notifyMessages;
    }

    public boolean isAlertFromSubscriber() {
        return alertFromSubscriber;
    }

    public boolean isAlertFromNearby() {
        return alertFromNearby;
    }

    public boolean isNotifyMentions() {
        return notifyMentions;
    }

    public boolean isNotifyMessages() {
        return notifyMessages;
    }
}
