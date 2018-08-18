package im.goody.android.screens.new_event;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.TextUtils;

public class NewEventViewModel extends BaseObservable {
    public final ObservableField<Calendar> calendar = new ObservableField<>();
    public final ObservableBoolean isDateImmediate = new ObservableBoolean(false);

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();

    final ArrayList<String> tags;

    public final ObservableField<Bitmap> image = new ObservableField<>();

    @Bindable
    private Location location;

    private Uri imageUri;

    NewEventViewModel(Deal deal) {
        Location location = deal.getLocation();
        if (location != null && !TextUtils.isEmpty(location.getAddress())) {
            this.location = deal.getLocation();
        }

        description.set(deal.getDescription());
        title.set(deal.getTitle());

        Deal.Event event = deal.getEvent();

        String[] temp = event.getResources().split(",");
        tags = new ArrayList<>(Arrays.asList(temp));

        isDateImmediate.set(event.isImmediately());

        if (!event.isImmediately())
            calendar.set(DateUtils.calendarFromString(event.getDate()));
    }

    NewEventViewModel() {
        tags = new ArrayList<>();
    }

    NewEventReq body() {
        return new NewEventReq()
                .setLatitude(location == null ? null : Double.parseDouble(location.getLatitude()))
                .setLongitude(location == null ? null : Double.parseDouble(location.getLongitude()))
                .setDate(getStringDate())
                .setTitle(title.get())
                .setResources(joinTags())
                .setImmediately(isDateImmediate.get())
                .setDescription(description.get());
    }

    // ======= region getters =======

    Uri getImageUri() {
        return imageUri;
    }

    private String getStringDate() {
        if (calendar.get() != null)
            return DateUtils.dateToString(calendar.get().getTime());
        else return null;
    }

    public Location getLocation() {
        return location;
    }

    // end

    // ======= region setters =======

    void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    void setImageFromUri(Uri imageUri) {
        ContentResolver resolver = App.getAppContext().getContentResolver();

        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(resolver, imageUri);
            image.set(bmp);
        } catch (Exception e) {
            image.set(null);
        }
    }

    public void setLocation(Place place) {
        if (place == null) {
            location = null;
        } else {
            location = new Location(place.getLatLng().latitude,
                    place.getLatLng().longitude, place.getAddress().toString());
        }
        notifyPropertyChanged(BR.location);
    }

    public void addTags(String rawTags) {
        rawTags = rawTags.replace(", ", ",");
        String[] newTags = rawTags.split(",|\\s");
        tags.addAll(Arrays.asList(newTags));

        Set<String> set = new LinkedHashSet<>(tags);
        tags.clear();
        tags.addAll(set);
    }

    private String joinTags() {
        StringBuilder sb = new StringBuilder("");

        for (String tag : tags) {
            sb.append(tag).append(",");
        }

        return sb.toString();
    }

    // end

}
