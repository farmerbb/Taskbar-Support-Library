/* Copyright 2018 Braden Farmer
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Display;

public class OverscanReceiver extends BroadcastReceiver {

    @SuppressLint("PrivateApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!hasWriteSecureSettingsPermission(context))
            return;

        int displayID = intent.getIntExtra("display_id", Display.DEFAULT_DISPLAY);
        int value = intent.getIntExtra("value", 0);

        try {
            wmOverscan(displayID, value);
        } catch (Exception e) { /* Gracefully fail */ }
    }

    public static boolean hasWriteSecureSettingsPermission(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.checkSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("PrivateApi")
    private static Object getWindowManagerService() throws Exception {
        return Class.forName("android.view.WindowManagerGlobal")
                .getMethod("getWindowManagerService")
                .invoke(null);
    }

    @SuppressLint("PrivateApi")
    private static void wmOverscan(int displayID, int value) throws Exception {
        Class.forName("android.view.IWindowManager")
                .getMethod("setOverscan", int.class, int.class, int.class, int.class, int.class)
                .invoke(getWindowManagerService(), displayID, 0, 0, 0, value);
    }
}
