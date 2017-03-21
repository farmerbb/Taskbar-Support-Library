package com.farmerbb.taskbar.support;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class BlissEnableHomeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Send broadcast to main Taskbar app to enable HomeActivity
        Intent enableHomeIntent = new Intent("com.farmerbb.taskbar.ENABLE_HOME");
        enableHomeIntent.putExtra("enable_freeform_hack", true);
        context.sendBroadcast(enableHomeIntent);

        // This receiver should only run once, so disable it
        ComponentName component = new ComponentName(context, getClass());
        context.getPackageManager().setComponentEnabledSetting(component,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
