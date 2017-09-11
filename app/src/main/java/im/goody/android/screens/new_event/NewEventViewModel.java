package im.goody.android.screens.new_event;

import android.content.ContentResolver;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import java.util.Calendar;

import im.goody.android.App;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.utils.DateUtils;

public class NewEventViewModel {
    public final ObservableField<Calendar> calendar = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> resources = new ObservableField<>();

    public final ObservableField<Bitmap> image = new ObservableField<>();
    public final ObservableField<Place> location = new ObservableField<>();

    private Uri imageUri;

    NewEventReq body() {
        return new NewEventReq()
                .setLatitude(location.get() == null ? null : location.get().getLatLng().latitude)
                .setLongitude(location.get() == null ? null : location.get().getLatLng().longitude)
                .setDate(getStringDate())
                .setTitle(title.get())
                .setResources(resources.get())
                .setDescription(description.get());
    }

    Uri getImageUri() {
        return imageUri;
    }

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

    private String getStringDate() {
        if (calendar.get() != null)
            return DateUtils.dateToString(calendar.get().getTime());
        else return null;
    }
}
