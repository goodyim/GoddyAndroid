package im.goody.android.screens.new_post;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import im.goody.android.App;
import im.goody.android.data.network.req.NewPostReq;

public class NewPostViewModel extends BaseObservable {
    private Uri imageUri;

    public final ObservableField<Bitmap> image = new ObservableField<>();
    public final ObservableField<Place> location = new ObservableField<>();

    public final ObservableBoolean subscribersOnly = new ObservableBoolean(false);
    public final ObservableField<String> description = new ObservableField<>();

    NewPostReq body() {
        return new NewPostReq()
                .setPlaceId(location.get() != null ? location.get().getId() : null)
                .setDescription(description.get())
                .setSubscribersOnly(subscribersOnly.get());
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

    // ======= region getters =======

    Uri getImageUri() {
        return imageUri;
    }

    // endregion

    // ======= region setters =======

    void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    // endregion
}
