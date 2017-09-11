package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import im.goody.android.data.network.core.NameSpace;

public class NewEventReq {
    private String description;
    @JsonProperty("name")
    private String title;
    @JsonProperty("category_id")
    private int category = 0; // required param but not used

    @NameSpace("event") private Double latitude;
    @NameSpace("event") private String date;
    @NameSpace("event") private String resources;
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

    public NewEventReq setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public NewEventReq setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    // end
}
