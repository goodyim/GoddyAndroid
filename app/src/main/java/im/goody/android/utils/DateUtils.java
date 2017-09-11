package im.goody.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import im.goody.android.App;

import static android.text.format.DateUtils.getRelativeTimeSpanString;
import static im.goody.android.Constants.DATE_FORMAT;

public class DateUtils {
    private static final String INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static String getReadableDate(String input) {
        try {
            Date date = StringToDate(input);
            return getRelativeTimeSpanString(App.getAppContext(), date.getTime(), true)
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(INPUT_PATTERN, Locale.getDefault());
        return format.format(date);
    }

    private static Date StringToDate(String time) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_PATTERN, Locale.getDefault());

        return inputFormat.parse(time);
    }

    public static String getAbsoluteDate(String input) {
        try {
            Date date = StringToDate(input);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return getAbsoluteDate(calendar);
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    private static String getAbsoluteDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), DATE_FORMAT,
                day, month, year, hours, minutes);
    }
}
