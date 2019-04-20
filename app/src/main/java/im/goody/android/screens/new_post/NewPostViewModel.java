package im.goody.android.screens.new_post;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.utils.TextUtils;

public class NewPostViewModel extends BaseObservable {
    public final ObservableField<Bitmap> image = new ObservableField<>();
    public final ObservableBoolean subscribersOnly = new ObservableBoolean(false);
    public final ObservableField<String> description = new ObservableField<>();

    private Uri currentImageUri;

    @Bindable
    private Location location;

    NewPostViewModel() {}

    NewPostViewModel(Deal deal) {
        Location location = deal.getLocation();
        if (location != null && !TextUtils.isEmpty(location.getAddress())) {
            this.location = deal.getLocation();
        }

        description.set(deal.getDescription());
    }

    NewPostReq body() {
        return new NewPostReq()
                .setLatitude(location != null ? Double.parseDouble(location.getLatitude()) : null)
                .setLongitude(location != null ? Double.parseDouble(location.getLongitude()) : null)
                .setAddress(location != null ? location.getAddress() : null)
                .setDescription(description.get())
                .setSubscribersOnly(subscribersOnly.get());
    }

    // ======= region getters =======

    Uri getCurrentImageUri() {
        return currentImageUri;
    }

    public Location getLocation() {
        return location;
    }

    // endregion

    // ======= region setters =======

    void setImageFromUri(Uri imageUri) {
        ContentResolver resolver = App.getAppContext().getContentResolver();

        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(resolver, imageUri);
            image.set(bmp);
            this.currentImageUri = imageUri;
        } catch (Exception e) {
            image.set(null);
        }
    }

    void setCurrentImageUri(Uri currentImageUri) {
        this.currentImageUri = currentImageUri;
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

    // endregion
}
