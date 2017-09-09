package im.goody.android.screens.new_event;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import im.goody.android.R;

public class NewEventBindingAdapter {

    @BindingAdapter("event_date")
    public static void bindDate(TextView view, Calendar calendar) {
        String date;

        if (calendar == null) {
            date = view.getContext().getString(R.string.choose_date);
        } else {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String dateFormat = "%d.%d.%d";
            date = String.format(Locale.getDefault(), dateFormat, year, month, day);
        }

        view.setText(date);
    }

    @BindingAdapter("event_time")
    public static void bindTime(TextView view, Calendar calendar) {
        String time;

        if (calendar == null) {
            time = view.getContext().getString(R.string.choose_time);
        } else {
            int hours = calendar.get(Calendar.YEAR);
            int minutes = calendar.get(Calendar.MONTH);

            String timeFormat = "%d:%d";
            time = String.format(Locale.getDefault(), timeFormat, hours, minutes);
        }

        view.setText(time);
    }
}
