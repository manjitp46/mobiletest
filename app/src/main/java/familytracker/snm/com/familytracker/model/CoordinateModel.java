package familytracker.snm.com.familytracker.model;

import io.realm.RealmObject;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class CoordinateModel extends RealmObject {
    String lat;
    String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
