package familytracker.snm.com.familytracker.utils;

import android.app.ActivityManager;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by kumanjit on 10/2/2017.
 */

public class ServiceUtil {

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager;
//        manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.i ("isMyServiceRunning?", true+"");
//                return true;
//            }
//        }
//        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}


