package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.android.gms.maps.model.LatLng;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    private String latitude;
    private String longitude;
    private String address;

    public Location() {}

    public Location(Double latitude, Double longitude, String address) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        this.address = address;
    }

    public LatLng toLatLng() {
        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);

        return new LatLng(lat, lon);
    }

    // ======= region getters =======

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    // endregion

    // ======= region setters =======

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // endregion
}
