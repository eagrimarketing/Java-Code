package my.project.agrim;

/**
 * Created by Vivek on 08-09-2018.
 */
public class Latlng {
    Double Latitude;
    Double Longitude;

    public Latlng(Double latitude, Double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }


    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
