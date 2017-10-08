package familytracker.snm.com.familytracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class CheckinDataModel extends RealmObject {

    //@primary key works Same as a primary key in relational DB
    @PrimaryKey
    private String id;
    private String time;
    private LocationModel location;
    private DeviceInfoModel deviceInfo;
    private AssociationModel association;

    /*
     *  If we want some field but not want to insert in
     *  Db we can use
     * @Ignore Annotation
     * means that field is not inserted in Db
     */

   /* If we want some field must be Inserted to Db
    * we can use
    * @Required Annotation
    * means that field cannot be null
    */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public DeviceInfoModel getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfoModel deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public AssociationModel getAssociation() {
        return association;
    }

    public void setAssociation(AssociationModel association) {
        this.association = association;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
