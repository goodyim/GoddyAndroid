package im.goody.android.data.network.req;

import android.graphics.Bitmap;

public class NewPostReq {
    private String title;
    private String description;
    private Bitmap image;
    private String placeId;

    // ======= region getters =======

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPlaceId() {
        return placeId;
    }

    // endregion

    // ======= region setters =======

    public NewPostReq setTitle(String title) {
        this.title = title;
        return this;
    }

    public NewPostReq setDescription(String description) {
        this.description = description;
        return this;
    }

    public NewPostReq setImage(Bitmap image) {
        this.image = image;
        return this;
    }

    public NewPostReq setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    //endregion
}
