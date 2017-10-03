package familytracker.snm.com.familytracker.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.compat.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

import familytracker.snm.com.familytracker.AppController;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class DeviceInfo {
    //disabling instatiation
    private DeviceInfo(){}

    //function to return Device Specific Info
    public static JSONObject getDeviceInfo(Context context){
        JSONObject deviceInfoObj = new JSONObject();

        try {
            deviceInfoObj.put("deviceName",android.os.Build.MODEL);
            deviceInfoObj.put("battery", BatteryUtil.getBatteryPercentage(context)+"");

            deviceInfoObj.put("appVersion", getAppVersionName(context));
            deviceInfoObj.put("androidVersion", Build.VERSION.RELEASE+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfoObj;
    }
    private static String getAppVersionName(Context context){
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }
}
