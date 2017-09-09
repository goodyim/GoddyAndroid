package im.goody.android.ui.dialogs;

import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

import static android.app.TimePickerDialog.OnTimeSetListener;

public class TimePickDialog {
    private static final boolean IS_24_STYLE = true;

    private Context context;

    private TimePickDialog(Context context){
        this.context = context;
    }

    public static TimePickDialog with(Context context) {
        return new TimePickDialog(context);
    }

    public Observable<Calendar> show(@Nullable Calendar start) {
        return Observable.create(source -> {
            Calendar result = start != null ? start : Calendar.getInstance();

            int hour = result.get(Calendar.HOUR_OF_DAY);
            int minutes = result.get(Calendar.MINUTE);

            OnTimeSetListener listener = (timePicker, h, m) -> {
                result.set(Calendar.HOUR_OF_DAY, h);
                result.set(Calendar.MINUTE, m);

                source.onNext(result);
            };

            new TimePickerDialog(context, listener, hour, minutes, IS_24_STYLE).show();
        });
    }
}
