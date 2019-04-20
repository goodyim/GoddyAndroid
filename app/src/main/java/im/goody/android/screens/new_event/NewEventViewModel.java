package im.goody.android.screens.new_event;

import android.content.ContentResolver;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Calendar;

import im.goody.android.App;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Event;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.screens.common.TagViewModel;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.TextUtils;

import static im.goody.android.data.dto.Event.PhoneInfo.VISIBILITY_ALL;

public class NewEventViewModel extends TagViewModel {
    public final ObservableField<Calendar> calendar = new ObservableField<>();
    public final ObservableBoolean isDateImmediate = new ObservableBoolean(false);

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();

    public final ObservableInt phoneVisibility = new ObservableInt(VISIBILITY_ALL);

    public final ObservableField<Bitmap> image = new ObservableField<>();

    public final ObservableField<Location> location = new ObservableField<>();

    private Uri currentImageUri;

    NewEventViewModel(Deal deal) {
        Location location = deal.getLocation();
        if (location != null && !TextUtils.isEmpty(location.getAddress())) {
            this.location.set(deal.getLocation());
        }

        description.set(deal.getDescription());
        title.set(deal.getTitle());

        Event event = deal.getEvent();

        addTags(event.getResources());

        isDateImmediate.set(event.isImmediately());

        if (!event.isImmediately())
            calendar.set(DateUtils.calendarFromString(event.getDate()));

        phoneVisibility.set(event.getPhoneInfo().getVisibility());
    }

    NewEventViewModel() {
        tags = new ArrayList<>();
    }

    NewEventReq body() {
        return new NewEventReq()
                .setLatitude(location.get() == null ? null : Double.parseDouble(location.get().getLatitude()))
                .setLongitude(location.get() == null ? null : Double.parseDouble(location.get().getLongitude()))
                .setDate(getStringDate())
                .setTitle(title.get())
                .setResources(joinTags())
                .setImmediately(isDateImmediate.get())
                .setVisibility(phoneVisibility.get())
                .setDescription(description.get());
    }

    // ======= region getters =======

    Uri getCurrentImageUri() {
        return currentImageUri;
    }

    private String getStringDate() {
        if (calendar.get() != null)
            return DateUtils.dateToString(calendar.get().getTime());
        else return null;
    }

    // end

    // ======= region setters =======

    void setImageFromUri(Uri imageUri) {
        ContentResolver resolver = App.getAppContext().getContentResolver();

        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(resolver, imageUri);
            image.set(bmp);
            currentImageUri = imageUri;
        } catch (Exception e) {
            image.set(null);
        }
    }

    public void setLocation(Place place) {
        if (place == null) {
            location.set(null);
        } else {
            location.set(new Location(
                    place.getLatLng().latitude,
                    place.getLatLng().longitude,
                    place.getAddress().toString()
            ));
        }
    }

    private String joinTags() {
        StringBuilder sb = new StringBuilder();

        for (String tag : buildTags()) {
            sb.append(tag).append(",");
        }

        return sb.toString();
    }

    // end

}
