package familytracker.snm.com.familytracker;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import familytracker.snm.com.familytracker.config.AppConfig;
import familytracker.snm.com.familytracker.helper.SQLiteHandler;
import familytracker.snm.com.familytracker.listener.MyLocationListener;
import familytracker.snm.com.familytracker.service.MyStartedService;
import familytracker.snm.com.familytracker.service.TrackingService;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Button btnStart;
    private Button btnStop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runtime_permissions();
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String userName = inputEmail.getText().toString().trim();
//                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!userName.isEmpty()) {
                    // login user
                    checkLogin(userName, "test");
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
//        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        RegisterActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });


    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String userName, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        db.addUser(userName, userName+"@gmail.com", "123", "jwt_token");
        session.setLogin(true);

        // Launch main activity
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);

//        JSONObject jsonRequest = new JSONObject();
//        try {
//            JSONObject userDetails = new JSONObject();
//            userDetails.put("userName",userName);
//            userDetails.put("password", password);
//            jsonRequest.put("signIn",userDetails);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        pDialog.setMessage("Logging in ...");
//        showDialog();

//        JsonObjectRequest strReq = new JsonObjectRequest(Method.POST,
//                AppConfig.URL_LOGIN, jsonRequest, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    // Check for error node in json
//                    if (response!=null) {
//                        // user successfully logged in
//                        // Create login session
//
//
//                        // Now store the user in SQLite
//
//
//                        JSONObject data = response.getJSONObject("data");
//                        JSONObject account = data.getJSONObject("account");
//                        String uid      = account.getString("_id");
//                        String name     = account.getString("firstName") + " " + account.getString("lastName");
//                        String email    = account.getString("email");
//                        String jwt_token = data.getString("jwt");
//
//                        // Inserting row in users table
//                        db.addUser(name, email, uid, jwt_token);
//                        session.setLogin(true);
//
//                        // Launch main activity
//                        Intent intent = new Intent(LoginActivity.this,
//                                MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        // Error in login. Get the error message
//                       // String errorMsg = jObj.getString("error_msg");
////                        Toast.makeText(getApplicationContext(),
////                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("userName", userName);
//                params.put("password", password);
//
//                return params;
//            }
//
//        };

        // Adding request to request queue
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(strReq);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void startedService(View view) {
//        Intent i = new Intent(LoginActivity.this, MyStartedService.class);
        Log.i(TAG, "StartedService Called");

        Log.i(TAG, String.valueOf(this));
//        startService(i);
    }
    private void stopStartedService(View view){
//        Intent i = new Intent(this, MyStartedService.class);
//        stopService(i);
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
                LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else {
                    Toast.makeText(LoginActivity.this, "Location Permission Granted", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(LoginActivity.this, TrackingService.class);
//                    intent.setAction(TrackingService.ACTION_START_MONITORING);
//                    startService(intent);
                }
            }else {
                Toast.makeText(LoginActivity.this,"Please Allow Location Permission service to use this Service",Toast.LENGTH_SHORT).show();
                runtime_permissions();
            }
        }
    }
}