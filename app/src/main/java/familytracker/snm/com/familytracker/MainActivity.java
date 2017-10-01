package familytracker.snm.com.familytracker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import familytracker.snm.com.familytracker.config.AppConfig;
import familytracker.snm.com.familytracker.helper.SQLiteHandler;
import familytracker.snm.com.familytracker.service.TrackingService;

public class MainActivity extends Activity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
//        if(!runtime_permissions())
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent i = new Intent(this, TrackingService.class);
            i.setAction(TrackingService.ACTION_START_MONITORING);
            startService(i);
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

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

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
    }

//
    private void logoutUserFromDevice() {
        session.setLogin(false);
        // Deleting User Info From LocalDb
        db.deleteUsers();

        //Stopping The running Location tracking Service
        Intent i = new Intent(this, TrackingService.class);
        i.setAction(TrackingService.ACTION_STOP_MONITORING);
        startService(i);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
        Intent i = new Intent(this,TrackingService.class);
        stopService(i);
    }
}