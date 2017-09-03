package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("good_deal")
public class NewPostReq {
    private String description;
    private String placeId;

    @JsonProperty("category_id")
    private int category = 0;

    @JsonProperty("private")
    private boolean subscribersOnly;

    // ======= region getters =======

    public String getDescription() {
        return description;
    }


    public String getPlaceId() {
        return placeId;
    }

    public int getCategory() {
        return category;
    }

    // endregion

    // ======= region setters =======

    public NewPostReq setDescription(String description) {
        this.description = description;
        return this;
    }

    public NewPostReq setPlaceId(String placeId) {
        this.placeId = placeId;
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
