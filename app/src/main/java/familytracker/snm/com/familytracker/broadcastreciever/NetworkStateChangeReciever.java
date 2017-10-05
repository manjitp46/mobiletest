package familytracker.snm.com.familytracker.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by kumanjit on 10/6/2017.
 */

public class NetworkStateChangeReciever extends BroadcastReceiver{
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(context,"Connected To InterNate",Toast.LENGTH_LONG).show();
            }
        }
    }
}
