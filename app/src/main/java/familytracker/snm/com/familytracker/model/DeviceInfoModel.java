package familytracker.snm.com.familytracker.model;

import io.realm.RealmObject;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class DeviceInfoModel extends RealmObject{
    private String deviceName;
    private String battery;
    private String androidVersion;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }
}
