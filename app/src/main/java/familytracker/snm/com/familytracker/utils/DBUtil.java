package familytracker.snm.com.familytracker.utils;

import android.content.Context;
import android.location.Location;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import familytracker.snm.com.familytracker.model.AssociationModel;
import familytracker.snm.com.familytracker.model.CheckinDataModel;
import familytracker.snm.com.familytracker.model.CoordinateModel;
import familytracker.snm.com.familytracker.model.DeviceInfoModel;
import familytracker.snm.com.familytracker.model.LocationModel;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by kumanjit on 10/7/2017.
 */

public class DBUtil {
    private Context context;
    private Realm myRealm;
    private static final String TAG = "DBUtil";

    public DBUtil(Context context){
        this.context = context;
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }
    public JSONArray prepareJsonFromDb(){
        JSONObject _chekinData =  new JSONObject();
        JSONArray  jsonArrayToReturn = new JSONArray();
        RealmQuery<CheckinDataModel> realmQuery = myRealm.where(CheckinDataModel.class);
        RealmResults<CheckinDataModel> checkinDatas = realmQuery.findAll();
        Log.i(TAG,"DB-Length: "+checkinDatas.size()+"");
        for (CheckinDataModel checkinData: checkinDatas){
            try {
                _chekinData.put("time",checkinData.getTime());
                _chekinData.put("location",prepareLocationJsonFromDb(checkinData.getLocation()));
                _chekinData.put("deviceInfo",prepareDeviceInfoFromDb(checkinData.getDeviceInfo()));
                _chekinData.put("assosiation",prepareAssosiationFromDb(checkinData.getAssociation()));
                jsonArrayToReturn.put(_chekinData);
//                Log.i(TAG,_chekinData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArrayToReturn;
    }
     private JSONObject prepareLocationJsonFromDb(LocationModel locationModel){
         JSONObject locationToReturn =  new JSONObject();
         try {
             locationToReturn.put("type",locationModel.getType());
             locationToReturn.put("altitude",locationModel.getAltitude());
             locationToReturn.put("accuracy",locationModel.getAccuracy());
             locationToReturn.put("address",locationModel.getAddress());
             locationToReturn.put("coordinate",prepareCoordinateJsonFromDb(locationModel.getCoordinate()));
         } catch (JSONException e) {
             e.printStackTrace();
         }
         return locationToReturn;
     }
    private JSONObject prepareCoordinateJsonFromDb(CoordinateModel locationModel){
        JSONObject coordinateToReturn =  new JSONObject();
        try {
            coordinateToReturn.put("lat",locationModel.getLat());
            coordinateToReturn.put("lng",locationModel.getLng());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  coordinateToReturn;
    }
    private JSONObject prepareAssosiationFromDb(AssociationModel associationModel){
        JSONObject associationToReturn = new JSONObject();
        try {
            associationToReturn.put("name",associationModel.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return associationToReturn;
    }

    private JSONObject prepareDeviceInfoFromDb(DeviceInfoModel deviceInfoModel){
        JSONObject deviceInfoToReturn = new JSONObject();
        try {
            deviceInfoToReturn.put("deviceName",deviceInfoModel.getDeviceName());
            deviceInfoToReturn.put("battery",deviceInfoModel.getBattery());
            deviceInfoToReturn.put("androidVersion",deviceInfoModel.getAndroidVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceInfoToReturn;
    }
    public JSONArray getLatLngJsonArray(){
        JSONArray latlngArrayToReturn = new JSONArray();
        RealmQuery<CheckinDataModel> realmQuery = myRealm.where(CheckinDataModel.class);
        RealmResults<CheckinDataModel> checkinDatas = realmQuery.findAll();
        for (CheckinDataModel checkinData: checkinDatas) {
            latlngArrayToReturn.put(prepareCoordinateJsonFromDb(checkinData.getLocation().getCoordinate()));
        }
        return latlngArrayToReturn;
    }

}
