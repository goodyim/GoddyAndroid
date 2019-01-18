package im.goody.android.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.List;

@JsonRootName("zone")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpInfo {
    private List<String> tags = new ArrayList<>();
    private Area area;

    public List<String> getTags() {
        return tags;
    }

    public HelpInfo setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Area getArea() {
        return area;
    }

    public HelpInfo setArea(Area area) {
        this.area = area;
        return this;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Area {
        private double latitude;
        private double longitude;
        private int radius;
        private String address;

        public String getAddress() {
            return address;
        }

        public Area setAddress(String address) {
            this.address = address;
            return this;
        }

        public double getLatitude() {
            return latitude;
        }

        public Area setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public double getLongitude() {
            return longitude;
        }

        public Area setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public int getRadius() {
            return radius;
        }

        public Area setRadius(int radius) {
            this.radius = radius;
            return this;
        }
    }
}
