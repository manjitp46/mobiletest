package familytracker.snm.com.familytracker.utils;

import android.content.Context;

import java.util.HashMap;

import familytracker.snm.com.familytracker.helper.SQLiteHandler;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class UserInfoUtil {
//    private static SQLiteHandler db;
    private static HashMap<String, String> user;
    //disabling from instatntiation: object not to be created to avoid memory leaks
    private  UserInfoUtil(){

    }
    public static String getUserName(Context context){
        user = new SQLiteHandler(context).getUserDetails();
        return user.get("name");
    }
}
