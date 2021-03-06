package familytracker.snm.com.familytracker.listener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import familytracker.snm.com.familytracker.SessionManager;
import familytracker.snm.com.familytracker.config.AppConfig;
import familytracker.snm.com.familytracker.helper.SQLiteHandler;
import familytracker.snm.com.familytracker.model.AssociationModel;
import familytracker.snm.com.familytracker.model.CheckinDataModel;
import familytracker.snm.com.familytracker.model.CoordinateModel;
import familytracker.snm.com.familytracker.model.DeviceInfoModel;
import familytracker.snm.com.familytracker.model.LocationModel;
import familytracker.snm.com.familytracker.utils.BatteryUtil;
import familytracker.snm.com.familytracker.utils.DeviceInfoUtil;
import familytracker.snm.com.familytracker.utils.LocationUtil;
import familytracker.snm.com.familytracker.utils.TimestampUtils;
import familytracker.snm.com.familytracker.utils.UserInfoUtil;
import io.realm.Realm;


/**
 * Created by kumanjit on 9/29/2017.
 */

public class MyLocationListener implements LocationListener {
    Context mcontext;
    static HashMap<String,String > user;
    static final String TAG = "MylocationListioner";
    private Realm myRealm;
   public MyLocationListener(){

    }
   public MyLocationListener(Context mcontext){
        this.mcontext = mcontext;
        this.user = new SQLiteHandler(mcontext).getUserDetails();
    }
    final static String Tag = "Location Monitoring";


    @Override
    public void onLocationChanged(Location location) {

        Log.i(Tag,location.getProvider());
        Log.i(Tag,"Locatin Data: Lat = "+location.getLatitude()+" lng = " +location.getLongitude());
        sendLocationDataToServer(location,user);
        SessionManager sessionManager = new SessionManager(mcontext);
        sessionManager.setLatLng(location.getLatitude()+"",location.getLongitude()+"");
        String msg = location.getProvider() + " " +location.getAccuracy();
        Log.i(Tag,msg);
        Log.i(Tag,user.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        sendProviderDetailsToServer("status is changed",provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        String threadId = Thread.currentThread().getId()+"";
        sendProviderDetailsToServer("enabled",provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        String threadId = Thread.currentThread().getId()+"";
        sendProviderDetailsToServer("disabled",provider);
    }

    public void sendLocationDataToServer(final Location location, HashMap<String,String> user){


        /*
           start Saving in Local DB Location Info
         */
        String primaryKey = UUID.randomUUID().toString();
        Realm.init(mcontext);
        myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        try {
            DeviceInfoModel deviceInfoModel = myRealm.createObject(DeviceInfoModel.class);
            AssociationModel associationModel = myRealm.createObject(AssociationModel.class);
            LocationModel locationModel = myRealm.createObject(LocationModel.class);
            CoordinateModel coordinateModel = myRealm.createObject(CoordinateModel.class);
            CheckinDataModel checkinDataModel = myRealm.createObject(CheckinDataModel.class, primaryKey);
            deviceInfoModel.setBattery(BatteryUtil.getBatteryPercentage(mcontext) + "");
            deviceInfoModel.setAndroidVersion(Build.VERSION.RELEASE);
            deviceInfoModel.setDeviceName(Build.MODEL);
            associationModel.setName(user.get("name"));
            locationModel.setType(location.getProvider());
            locationModel.setAccuracy(location.getAccuracy() + "");
            locationModel.setAddress(LocationUtil.getTextAddress(mcontext, location.getLatitude(), location.getLongitude(), 4));
            locationModel.setAltitude(location.getAltitude() + "");
            coordinateModel.setLat(location.getLatitude() + "");
            coordinateModel.setLng(location.getLongitude() + "");
            locationModel.setCoordinate(coordinateModel);
            checkinDataModel.setTime(TimestampUtils.getISO8601StringForCurrentDate());
            checkinDataModel.setLocation(locationModel);
            checkinDataModel.setDeviceInfo(deviceInfoModel);
            checkinDataModel.setAssociation(associationModel);
            myRealm.commitTransaction();
        }catch (Exception e){
            Log.i(Tag,"error occur");
        }
//        boolean isInterNateAvailable = DeviceInfoUtil.isNetworkAvailable(this);





        /*
           end Saving in Local DB Location Info
         */

        JSONObject jsonDataToSend = new JSONObject();
        String threadId = Thread.currentThread().getId()+"";
        try {
            JSONObject coordinate = getJsonObject();
            coordinate.put("lat",location.getLatitude()+"");
            coordinate.put("lng",location.getLongitude()+"");
            JSONObject locationObj = getJsonObject();
            locationObj.put("type",location.getProvider().equals("gps")?"gps":"network");
            locationObj.put("altitude",location.getAltitude()+"");
            locationObj.put("accuracy",location.getAccuracy()+"");
            locationObj.put("fullAddress",LocationUtil.getTextAddress(mcontext,location.getLatitude(),location.getLongitude(),5));
            locationObj.put("coordinate",coordinate);
            JSONObject assosiationObj = getJsonObject();
            assosiationObj.put("name",user.get("name"));
            JSONObject deviceObj = new JSONObject();
            deviceObj.put("deviceName",android.os.Build.MODEL);
            deviceObj.put("battery", BatteryUtil.getBatteryPercentage(mcontext)+"");
            deviceObj.put("androidVersion", Build.VERSION.RELEASE);
            JSONObject dataObj = getJsonObject();
            dataObj.put("time",TimestampUtils.getISO8601StringForDate(new Date(location.getTime())));
            dataObj.put("location",locationObj);
            dataObj.put("deviceInfo",deviceObj);
            dataObj.put("assosiation",assosiationObj);
            jsonDataToSend.put("data",dataObj);
//            jsonDataToSend.put("checkinType","Location");
//            JSONObject checkinDataObject = getJsonObject();
//            checkinDataObject.put("submittedOn", location.getTime());
//            checkinDataObject.put("","");
//            JSONObject locationObject = getJsonObject();
//            locationObject.put("type",(location.getProvider().equals("gps"))?"gps":"network");
//            locationObject.put("altitude",location.getAltitude()+"");
//            locationObject.put("accuracy",location.getAccuracy()+"");
//            JSONObject coordinate =getJsonObject();
//            coordinate.put("lat",location.getLatitude()+"");
//            coordinate.put("lng",location.getLongitude()+"");
//            locationObject.put("coordinate",coordinate);
//            checkinDataObject.put("location",locationObject);
//            jsonDataToSend.put("checkinData",checkinDataObject);
//            JSONObject assosiationObjectId =getJsonObject();
//            assosiationObjectId.put("userId",user.get("uid"));
//            assosiationObjectId.put("organizationId","test");
//            jsonDataToSend.put("assosiationIds",assosiationObjectId);
//            newJsontoSend = getJsonObject();
//            newJsontoSend.put("data",jsonDataToSend);
            Log.i(Tag,jsonDataToSend.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_MAP_DATA_SEND, jsonDataToSend, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(Tag,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(Tag,error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", location.getLatitude()+"");
                params.put("long", location.getLatitude()+"");
                params.put("speed",location.getSpeed()+"");

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(mcontext);
        queue.add(request);
    }

    public void sendProviderDetailsToServer(String status,String locationProvideName){
        JSONObject data = new JSONObject();
        JSONObject dataBody = new JSONObject();
        String threadId = Thread.currentThread().getId()+"";
        try {
            dataBody.put("time",TimestampUtils.getISO8601StringForCurrentDate());
            dataBody.put("type",locationProvideName+" is "+status );
            dataBody.put("name", UserInfoUtil.getUserName(mcontext));
            dataBody.put("deviceInfo", DeviceInfoUtil.getDeviceInfo(mcontext));
            data.put("data",dataBody);
            Log.d(Tag,data.toString());
            Log.i(Tag,data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

        RequestQueue queue =Volley.newRequestQueue(mcontext);
        queue.add(historyRequest);
    }


    public JSONObject getJsonObject(){
        return  new JSONObject();
    }
}
