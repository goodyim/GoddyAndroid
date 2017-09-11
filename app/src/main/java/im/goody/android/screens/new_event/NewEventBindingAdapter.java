package im.goody.android.screens.new_event;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.util.Calendar;
import java.util.Locale;

import im.goody.android.R;

import static im.goody.android.Constants.DATE_FORMAT;

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

            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            date = String.format(Locale.getDefault(), DATE_FORMAT,
                    day, month, year, hours, minutes);
        }

        view.setText(date);
    }

    @BindingAdapter("event_location")
    public static void bindLocation(TextView view, Place place) {
        String address;

        if (place == null) {
            address = view.getContext().getString(R.string.choose_location);
        } else {
            address = place.getAddress().toString();
        }

        view.setText(address);
    }

    @BindingAdapter("event_photo")
    public static void bindPhotoSelector(TextView view, Bitmap bmp) {
        int visibility;

        visibility = bmp == null ? View.VISIBLE : View.GONE;

        view.setVisibility(visibility);
    }

    @BindingAdapter("event_photo")
    public static void bindPhotoSelector(ImageView view, Bitmap bmp) {
        int visibility;

        visibility = bmp == null ? View.GONE : View.VISIBLE;

        ((View)view.getParent()).setVisibility(visibility);
        view.setImageBitmap(bmp);
    }
}
