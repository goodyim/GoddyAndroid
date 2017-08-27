package im.goody.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import im.goody.android.App;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class DateUtils {
    private static final String INPUT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static String getReadableDate(String date) {
        try {
            return format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    private static String format(String time) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_PATTERN, Locale.getDefault());

        Date date = inputFormat.parse(time);

        return getRelativeTimeSpanString(App.getAppContext(), date.getTime(), true)
                .toString();
    }
}
