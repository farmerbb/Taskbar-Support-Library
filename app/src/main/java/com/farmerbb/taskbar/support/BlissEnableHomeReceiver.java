/* Copyright 2017 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.support;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class BlissEnableHomeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Send broadcast to main Taskbar app to enable HomeActivity
            if(isSystemApp(context)) {
                Intent enableHomeIntent = new Intent("com.farmerbb.taskbar.ENABLE_HOME");
                enableHomeIntent.setPackage("com.farmerbb.taskbar");
                enableHomeIntent.putExtra("enable_freeform_hack", true);
                enableHomeIntent.putExtra("enable_running_apps_only", true);
                enableHomeIntent.putExtra("enable_navigation_bar_buttons", true);
                context.sendBroadcast(enableHomeIntent);
            }

            // This receiver should only run once, so disable it
            ComponentName component = new ComponentName(context, getClass());
            context.getPackageManager().setComponentEnabledSetting(component,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    private boolean isSystemApp(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0);
            int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
            return (info.flags & mask) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
