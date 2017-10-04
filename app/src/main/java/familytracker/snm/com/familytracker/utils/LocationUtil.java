package familytracker.snm.com.familytracker.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class LocationUtil {
    public static List<Address> listOfAddress = null;
    private static final String TAG = "LocationUtil";

    public static String getTextAddress(Context context,double lat,double lng,int max) {
        int start = -1;
        int maxLine = 0;
        int currentLine = 0;
        String result = null;
        Geocoder geocoder = new Geocoder(context);
        try {
            listOfAddress = geocoder.getFromLocation(lat,lng,max);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int addressReturned = 0;
        if (listOfAddress!=null)
            addressReturned = listOfAddress.size();
        // start of dummy logic to get most accurate address info not working surety need to tested throughly
        if (addressReturned>1) {
            for (Address address : listOfAddress) {
                currentLine = address.getMaxAddressLineIndex();
                if (currentLine>maxLine){
                    maxLine = currentLine;
                    start++;
                }
            }
        //  end of dummy logic to get most accurate address info not working surety need to tested
            result =  getHumanReadableAddress(listOfAddress.get(start));
        }
        return  (result!=null) ? result : "readAble Address Not found";
    }
    private static String getHumanReadableAddress(Address address){
        StringBuilder strBuilder = new StringBuilder();
        int lastIndex  = address.getMaxAddressLineIndex();
        for (int index =0 ;index <= lastIndex;index++){
            strBuilder.append(address.getAddressLine(index));
            strBuilder.append(" , ");
        }
        Log.i(TAG,strBuilder.toString());
        return strBuilder.toString();
    }
}
