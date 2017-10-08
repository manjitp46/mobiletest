package familytracker.snm.com.familytracker.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class LocationUtil {
    public static List<Address> listOfAddress = null;
    private static final String TAG = "LocationUtil";

    public static String getTextAddress(Context context,double lat,double lng,int max) {
        int start = -1;
        int maxLine = -1;
        int currentIndex = 0;
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
        if (addressReturned>0) {
            Iterator<Address> itr= listOfAddress.iterator();
            while (itr.hasNext()){
                Address addr = itr.next();
                currentLine = addr.getMaxAddressLineIndex();
                if (currentLine>maxLine){
                    maxLine = currentLine;
                    start = currentIndex;
                }
                currentIndex++;
            }
        //  end of dummy logic to get most accurate address info not working surety need to tested
            result =  getHumanReadableAddress(listOfAddress.get(start));
        }
        return  (result!=null) ? result :"";
    }

    // Util
    private static String getHumanReadableAddress(Address address){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append((address.getSubThoroughfare())!=null?address.getSubThoroughfare():"");
        strBuilder.append(putSpaceChar());
        strBuilder.append((address.getThoroughfare())!=null?address.getThoroughfare():"");
        strBuilder.append(putSpaceChar());
        strBuilder.append((address.getSubLocality())!=null?address.getSubLocality():"");
        strBuilder.append(putSpaceChar());
        if(address.getSubLocality()!=null){
            strBuilder.append((address.getLocality()) != null && !(address.getSubLocality().equals(address.getLocality())) ? address.getLocality() : "");
        }
        strBuilder.append(putSpaceChar());
        strBuilder.append((address.getAdminArea())!=null?address.getAdminArea():"");

//        int lastIndex  = address.getMaxAddressLineIndex();
//        for (int index =0 ;index <= lastIndex;index++){
//            strBuilder.append(address.getAddressLine(index));
//            strBuilder.append(" ");
//        }
        Log.i(TAG,strBuilder.toString());
        return strBuilder.toString();
    }
    private static String putSpaceChar(){return " ";}
}
