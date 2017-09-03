package im.goody.android.screens.new_post;

import android.content.ContentResolver;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.location.places.Place;

import java.io.IOException;

import im.goody.android.App;
import im.goody.android.BR;
import im.goody.android.data.network.req.NewPostReq;

public class NewPostViewModel extends BaseObservable {
    private String description;
    private Uri imageUri;

    @Bindable
    private Bitmap image;

    @Bindable
    private Place location;

    private boolean subscribersOnly;

    NewPostReq body() {
        return new NewPostReq()
                .setPlaceId(location != null ? location.getId() : null)
                .setDescription(description)
                .setSubscribersOnly(subscribersOnly);
    }

    void setImage(Uri imageUri, boolean isNewFile) {
        ContentResolver resolver = App.getAppContext().getContentResolver();

        if (isNewFile) resolver.notifyChange(imageUri, null);

        try {
            image = MediaStore.Images.Media.getBitmap(resolver, imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }

        notifyPropertyChanged(BR.image);
    }

    // ======= region getters =======

    public Place getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isSubscribersOnly() {
        return subscribersOnly;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    // endregion

    // ======= region setters =======

    public void setLocation(Place location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public NewPostViewModel setSubscribersOnly(boolean subscribersOnly) {
        this.subscribersOnly = subscribersOnly;
        return this;
    }

    // endregion
}
