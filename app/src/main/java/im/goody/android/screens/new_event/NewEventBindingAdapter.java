package im.goody.android.screens.new_event;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.data.dto.Location;

import static im.goody.android.data.dto.Event.PhoneInfo.VISIBILITY_ALL;
import static im.goody.android.data.dto.Event.PhoneInfo.VISIBILITY_NOBODY;

public class NewEventBindingAdapter {
    @BindingAdapter("event_location")
    public static void bindLocation(TextView view, Location location) {
        String address;

        if (location == null || location.getLatitude() == null || location.getLongitude() == null) {
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

        ((View) view.getParent()).setVisibility(visibility);
        view.setImageBitmap(bmp);
    }

    @BindingAdapter("event_phone")
    public static void bindPhoneVisibility(TextView view, int visibility) {
        int stringRes;

        switch (visibility) {
            case VISIBILITY_ALL:
                stringRes = R.string.phone_visibility_all;
                break;
            /*case VISIBILITY_PARTICIPANTS:
                stringRes = R.string.phone_visibility_participants;
                break;
            case VISIBILITY_REQUEST:
                stringRes = R.string.phone_visibility_request;
                break;*/
            case VISIBILITY_NOBODY:
                stringRes = R.string.phone_visibility_never;
                break;
            default:
                stringRes = R.string.phone_visibility_all;
        }

        view.setText(stringRes);
    }
}
