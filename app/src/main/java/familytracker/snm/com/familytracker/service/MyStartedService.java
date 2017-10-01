package familytracker.snm.com.familytracker.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by kumanjit on 9/27/2017.
 */

public class MyStartedService extends Service {
    private static final String Tag = MyStartedService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(Tag, "onBinds Command Thread Name " + Thread.currentThread().getName());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Tag, "onStart Command Thread Name " + Thread.currentThread().getName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();

        Log.i(Tag,"onCreate Command Thread Name "+Thread.currentThread().getName());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(Tag,"onDestroy Command Thread Name "+Thread.currentThread().getName());
    }
}
