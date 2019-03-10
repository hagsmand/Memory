package ch.zhaw.facerecognition.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.locationlibary.*;
import com.example.locationlibary.MyService;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,ch.zhaw.facerecognition.Activities.MyService.class);
        context.startService(intent1);
        Log.i("done","itReciever");
    }
}
