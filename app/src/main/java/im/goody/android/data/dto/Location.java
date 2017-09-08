package im.goody.android.data.dto;

public class Location {
    private String latitude;
    private String longitude;
    private String address;

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
