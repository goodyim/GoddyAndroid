package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewEventReq {
    private String description;
    private String placeId;
    private String date;
    private String resources;
    private String title;

    @JsonProperty("category_id")
    private int category = 0; // required param but not used

    // ======= region getters =======

    public String getDescription() {
        return description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getDate() {
        return date;
    }

    public String getResources() {
        return resources;
    }

    public String getTitle() {
        return title;
    }

    // end

    // ======= region setters =======

    public NewEventReq setDescription(String description) {
        this.description = description;
        return this;
    }

    public NewEventReq setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    public NewEventReq setDate(String date) {
        this.date = date;
        return this;
    }

    public NewEventReq setResources(String resources) {
        this.resources = resources;
        return this;
    }

    public NewEventReq setTitle(String title) {
        this.title = title;
        return this;
    }

    // end
}
