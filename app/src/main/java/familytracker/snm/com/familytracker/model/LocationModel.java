package familytracker.snm.com.familytracker.model;

import io.realm.RealmObject;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class LocationModel extends RealmObject {
  private String type;
  private String altitude;
  private String accuracy;
  private String address;
  private CoordinateModel coordinate;

    public CoordinateModel getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateModel coordinate) {
        this.coordinate = coordinate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
