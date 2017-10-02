package familytracker.snm.com.familytracker.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import familytracker.snm.com.familytracker.service.TrackingService;

/**
 * Created by kumanjit on 10/2/2017.
 */

public class RestartTrackingServiceRReciever extends BroadcastReceiver {
    static boolean isLogout = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(RestartTrackingServiceRReciever.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        if (intent.hasExtra("logout")){
            isLogout = intent.getExtras().getBoolean("logout");
        }
        if (isLogout){
//            context.stopService(new Intent(context,TrackingService.class));
        }else {
            Intent i = new Intent(context, TrackingService.class);
            i.setAction(TrackingService.ACTION_START_MONITORING);
            context.startService(i);
        }

    }
}
