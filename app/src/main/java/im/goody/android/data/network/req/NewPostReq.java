package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import im.goody.android.data.network.core.NameSpace;

@JsonRootName("good_deal")
public class NewPostReq {
    private String description;

    @JsonProperty("category_id")
    private int category = 0;

    @JsonProperty("private")
    private boolean subscribersOnly;

    @NameSpace("event") private Double latitude;
    @NameSpace("event") private Double longitude;

    // ======= region getters =======

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    // endregion

    // ======= region setters =======

    public NewPostReq setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public NewPostReq setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public NewPostReq setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isSubscribersOnly() {
        return subscribersOnly;
    }

    public NewPostReq setSubscribersOnly(boolean subscribersOnly) {
        this.subscribersOnly = subscribersOnly;
        return this;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    //endregion
}
