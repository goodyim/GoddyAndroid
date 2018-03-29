package im.goody.android.data.dto;

import java.util.List;

public class HelpInfo {
    private List<String> tags;
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
