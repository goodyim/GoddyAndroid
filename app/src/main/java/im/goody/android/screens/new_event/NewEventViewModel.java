package im.goody.android.screens.new_event;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import java.util.Calendar;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.TextUtils;

public class NewEventViewModel extends BaseObservable {
    public final ObservableField<Calendar> calendar = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> resources = new ObservableField<>();

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

        resources.set(deal.getEvent().getResources());
        calendar.set(DateUtils.calendarFromString(deal.getEvent().getDate()));
    }

    NewEventViewModel() {
    }

    NewEventReq body() {
        return new NewEventReq()
                .setLatitude(location == null ? null : Double.parseDouble(location.getLatitude()))
                .setLongitude(location == null ? null : Double.parseDouble(location.getLongitude()))
                .setDate(getStringDate())
                .setTitle(title.get())
                .setResources(resources.get())
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

    // end

}
