package im.goody.android.screens.new_event;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.data.dto.Location;

public class NewEventBindingAdapter {
    @BindingAdapter("event_location")
    public static void bindLocation(TextView view, Location location) {
        String address;

        if (location == null) {
            address = view.getContext().getString(R.string.choose_location);
        } else {
            address = location.getAddress() != null
                    ? location.getAddress()
                    : String.format(Constants.COORDINATES_FORMAT, location.getLatitude(), location.getLongitude());
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
