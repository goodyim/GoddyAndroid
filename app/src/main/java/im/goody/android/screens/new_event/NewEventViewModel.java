package im.goody.android.screens.new_event;

import android.databinding.ObservableField;

import java.util.Calendar;

import im.goody.android.utils.DateUtils;

public class NewEventViewModel {
    public final ObservableField<Calendar> calendar = new ObservableField<>();

    public String getStringDate() {
        if (calendar.get() != null)
            return DateUtils.dateToString(calendar.get().getTime());
        else return null;
    }
}
