package familytracker.snm.com.familytracker.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import familytracker.snm.com.familytracker.config.AppConfig;
import familytracker.snm.com.familytracker.helper.SQLiteHandler;
import familytracker.snm.com.familytracker.listener.MyLocationListener;

/**
 * Created by kumanjit on 9/29/2017.
 * Helps To Track Location on Background Thread
 */

public class TrackingService extends Service implements Handler.Callback{

    public final static String ACTION_START_MONITORING = "com.snm.familytracker.START_MONITORING";

    public final static String ACTION_STOP_MONITORING = "com.snm.familytracker.STOP_MONITORING";

    public final static String HANDLER_THREAD_NAME = "MYLocationTHREAD";

    final static String Tag = "Location Monitoring";

    LocationListener _listener;
    Looper _looper;
    android.os.Handler _handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Tag, "Service Created");
        HandlerThread handlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        handlerThread.start();

        _looper = handlerThread.getLooper();
        _handler = new Handler(_looper,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String threadId = Thread.currentThread().getId() + "";
        _handler.sendMessage(_handler.obtainMessage(0,intent));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doStopTracking();
        if(_looper!=null){
            _looper.quit();
        }
    }

    public void doStartTracking() {
            doStopTracking();
        try {
            _listener = new MyLocationListener();
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                PackageManager pm = getApplicationContext().getPackageManager();
                if(pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK)) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new MyLocationListener(getApplicationContext()), _looper);
                }
//                if(pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, new MyLocationListener(getApplicationContext()), _looper);
//                }
            }else {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }


        } catch (Exception e) {
            stopSelf();
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Intent intent = (Intent) msg.obj;
        String action = intent.getAction();
        String threadId = Thread.currentThread().getId() + "";
        if (action.equals(ACTION_START_MONITORING))
            doStartTracking();
        else if (action.equals(ACTION_STOP_MONITORING))
            doStopTracking();
        return false;
    }

    public void doStopTracking() {
        if (_listener != null) {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                lm.removeUpdates(_listener);
            }
            _listener=null;
            stopSelf();
        }
    }
}
