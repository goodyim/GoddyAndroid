package im.goody.android.screens.setting;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import im.goody.android.BR;
import im.goody.android.data.dto.Settings;

public class SettingViewModel extends BaseObservable {
    private boolean alertFromSubscriber;
    private boolean alertFromNearby;
    private boolean notifyMentions;
    private boolean notifyMessages;

    SettingViewModel(Settings settings) {
        this.alertFromSubscriber = settings.isAlertFromSubscriber();
        this.alertFromNearby = settings.isAlertFromNearby();
        this.notifyMentions = settings.isNotifyMentions();
        this.notifyMessages = settings.isNotifyMessages();
    }

    @Bindable
    public boolean isAlertFromSubscriber() {
        return alertFromSubscriber;
    }

    public void setAlertFromSubscriber(boolean alertFromSubscriber) {
        this.alertFromSubscriber = alertFromSubscriber;
        notifyPropertyChanged(BR.alertFromSubscriber);
    }

    @Bindable
    public boolean isAlertFromNearby() {
        return alertFromNearby;
    }

    public void setAlertFromNearby(boolean alertFromNearby) {
        this.alertFromNearby = alertFromNearby;
        notifyPropertyChanged(BR.alertFromNearby);
    }

    @Bindable
    public boolean isNotifyMentions() {
        return notifyMentions;
    }

    public void setNotifyMentions(boolean notifyMentions) {
        this.notifyMentions = notifyMentions;
        notifyPropertyChanged(BR.notifyMentions);
    }

    @Bindable
    public boolean isNotifyMessages() {
        return notifyMessages;
    }

    public void setNotifyMessages(boolean notifyMessages) {
        this.notifyMessages = notifyMessages;
        notifyPropertyChanged(BR.notifyMessages);
    }
}
