package net.kehui.www.t_907_origin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.kehui.www.t_907_origin.view.SplashActivity;

/**
 * Created by jwj on 2019/11/16.
 */

public class RestartAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentSplash = new Intent(context, SplashActivity.class);
        intentSplash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentSplash);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
