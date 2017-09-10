package im.goody.android.ui.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

import static android.app.DatePickerDialog.OnDateSetListener;

public class DatePickDialog {
    private Context context;

    private DatePickDialog(Context context){
        this.context = context;
    }

    public static DatePickDialog with(Context context) {
        return new DatePickDialog(context);
    }

    public Observable<Calendar> show(@Nullable Calendar start) {
        return Observable.create(source -> {
            Calendar result = start != null ? (Calendar) start.clone() : Calendar.getInstance();

            int year = result.get(Calendar.YEAR);
            int month = result.get(Calendar.MONTH);
            int day = result.get(Calendar.DAY_OF_MONTH);

            OnDateSetListener listener = (datePicker, y, m, d) -> {
                result.set(Calendar.YEAR, y);
                result.set(Calendar.MONTH, m);
                result.set(Calendar.DAY_OF_MONTH, d);

                source.onNext(result);
            };

            new DatePickerDialog(context, listener, year, month, day).show();
        });
    }
}
