package im.goody.android.screens.common;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import im.goody.android.data.dto.Event;
import im.goody.android.data.dto.Event.PhoneInfo;

public class PhoneInfoViewModel {
    public final ObservableInt state = new ObservableInt(PhoneInfo.STATE_UNDEFINED);
    public final ObservableField<String> content = new ObservableField<>((String) null);

    public PhoneInfoViewModel(PhoneInfo phoneInfo) {
        update(phoneInfo);
    }

    public boolean isPhoneNotFilled() {
        return state.get() == PhoneInfo.STATE_UNDEFINED;
    }

    public void update(PhoneInfo info) {
        if (info != null) {
            state.set(info.getState());
            content.set(info.getContent());
        }
    }
}
