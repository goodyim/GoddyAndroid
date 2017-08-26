package im.goody.android.screens.new_post;

import android.content.Context;
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
    private String title;
    private String description;

    @Bindable
    private Bitmap image;

    @Bindable
    private Place location;

    NewPostReq body() {
        return new NewPostReq()
                .setPlaceId(location != null ? location.getId() : null)
                .setImage(image)
                .setDescription(description)
                .setTitle(title);
    }

    void setImage(Uri imageUri) {
        Context context = App.getAppContext();
        try {
            image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    // endregion

    // ======= region setters =======

    public void setLocation(Place location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }

    // endregion
}
