package familytracker.snm.com.familytracker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import familytracker.snm.com.familytracker.R;
import familytracker.snm.com.familytracker.model.CheckinDataModel;
import familytracker.snm.com.familytracker.model.DeviceInfoModel;
import familytracker.snm.com.familytracker.model.LocationModel;
import familytracker.snm.com.familytracker.utils.DBUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MapActivity extends AppCompatActivity {
    private Realm myRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        showRealmData();
    }
    public void showRealmData(){
        RealmQuery<CheckinDataModel> realmQuery = myRealm.where(CheckinDataModel.class);
        RealmResults<CheckinDataModel> checkinData = realmQuery.findAll();
        Log.i("DB LENGTH",checkinData.size()+"");
        Log.i("MapActivity",checkinData.toString());
//        showData(checkinData);
        DBUtil dbUtil = new DBUtil(this);
        JSONArray arr= dbUtil.prepareJsonFromDb();
        Log.i(MapActivity.class.getSimpleName(),arr.toString());
        Toast.makeText(this,"Fetched from Realm Db - Length:  " +checkinData.size() ,Toast.LENGTH_LONG).show();
    }


    private void showData(RealmResults<CheckinDataModel> checkinDataModel) {
        StringBuilder builder = new StringBuilder();

        for (CheckinDataModel checkinData: checkinDataModel){
            builder.append("ID: "+checkinData.getId()+"\n");
            builder.append("Time: "+checkinData.getTime()+"\n");
            DeviceInfoModel deviceInfo = checkinData.getDeviceInfo();
            LocationModel locationModel =  checkinData.getLocation();
            builder.append("Location: {type:"+deviceInfo.getAndroidVersion()+":"+deviceInfo.getBattery()+", }");
            builder.append("Location: {type:"+locationModel.getType()+":"+locationModel.getAddress()+", }");
        }
        Log.i("DB DATA",builder.toString());


    }

}
