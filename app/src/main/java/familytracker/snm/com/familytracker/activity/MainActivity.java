package familytracker.snm.com.familytracker.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import familytracker.snm.com.familytracker.R;
import familytracker.snm.com.familytracker.SessionManager;
import familytracker.snm.com.familytracker.config.AppConfig;
import familytracker.snm.com.familytracker.helper.SQLiteHandler;
import familytracker.snm.com.familytracker.service.TrackingService;
import familytracker.snm.com.familytracker.utils.BatteryUtil;
import familytracker.snm.com.familytracker.utils.TimestampUtils;

public class MainActivity extends Activity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnMap;
    private Button btnrealmCheck;
    private Intent trackingServiceIntent;
    private SQLiteHandler db;
    private static final String TAG = "MAINACTIVITY";
    private SessionManager session;
    private String userNameGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnrealmCheck =(Button)findViewById(R.id.realmCheck);
        //disabling logout button temporary to avoid logout
        btnLogout.setEnabled(true);
//        if(!runtime_permissions())
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.trackingServiceIntent = new Intent(getApplicationContext(), TrackingService.class);
            this.trackingServiceIntent.setAction(TrackingService.ACTION_START_MONITORING);
            startService(this.trackingServiceIntent);

        }
        else{
            Toast.makeText(this,"Please Open GPS for Better Tracking",Toast.LENGTH_LONG).show();
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
//        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            Intent i = new Intent(this, TrackingService.class);
//            i.setAction(TrackingService.ACTION_START_MONITORING);
//            startService(i);
//        }

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        //enable_buttons();
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUserFromDevice();
        }
        sendBroadcast(new Intent("UPDATE_WITH_DB"));

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        this.userNameGlobal =name;

        // Displaying the user details on the screen
        txtName.setText(name);
//        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUserFromDevice();
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(i);
            }
        });

        btnrealmCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MapActivity.class);
                startActivity(i);
            }
        });
    }


    //overRiding onResumeMethod to fix issue when user prompted to start gps service not starting


    @Override
    protected void onResume() {
        super.onResume();
        this.trackingServiceIntent = new Intent(getApplicationContext(), TrackingService.class);
        this.trackingServiceIntent.setAction(TrackingService.ACTION_START_MONITORING);
        startService(this.trackingServiceIntent);

    }

    //
    private void logoutUserFromDevice() {
        session.setLogin(false);

        // Deleting User Info From LocalDb
        db.deleteUsers();

        //Stopping The running Location tracking Service

        this.trackingServiceIntent.setAction(TrackingService.ACTION_STOP_MONITORING);
        this.trackingServiceIntent.putExtra("logout","logout");
        startService(trackingServiceIntent);



        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

        //sending Logout Data To Server

        try {
            JSONObject deviceObj = new JSONObject();
            deviceObj.put("deviceName",android.os.Build.MODEL);
            deviceObj.put("battery", BatteryUtil.getBatteryPercentage(this)+"");
            deviceObj.put("androidVersion", Build.VERSION.RELEASE);
            JSONObject loginInfoObj = new JSONObject();
            loginInfoObj.put("time", TimestampUtils.getISO8601StringForCurrentDate());
            loginInfoObj.put("type","logout");
            loginInfoObj.put("name",this.userNameGlobal);
            loginInfoObj.put("deviceInfo",deviceObj);
            String testData = new JSONObject().put("data",loginInfoObj).toString();
            Log.i(TAG,testData);
            Log.d(TAG,new JSONObject().put("data",loginInfoObj).toString());
            sendLogoutHistoryToServer(getApplicationContext(),new JSONObject().put("data",loginInfoObj));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},200);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }else {
                runtime_permissions();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.trackingServiceIntent = new Intent(this,TrackingService.class);
        stopService(this.trackingServiceIntent);
    }

    public void sendLogoutHistoryToServer(Context context,JSONObject data){
        JsonObjectRequest historyRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.HISTORY_URL,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,error.toString());
            }
        }
        )
        {

        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(historyRequest);
    }


}