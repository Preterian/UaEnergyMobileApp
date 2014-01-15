package com.leoart.uaenergyapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.leoart.uaenergyapp.UaEnergyApp;

/**
 * Created by bogdan on 1/14/14.
 */
public class Rest {


    private static final String TAG = "Rest";

    public static boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) UaEnergyApp.context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            // mobile
            // 1
            NetworkInfo.State mobile = null;
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {

                mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getState();
            }

            // wifi
            // 1
            NetworkInfo.State wifi = null;
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
                wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState();
            }

            if (wifi != null) {
                Log.d(TAG, "WiFi State: " + wifi);
            }

            if (mobile != null) {
                Log.d(TAG, "3G State: " + mobile);
            }

            if (mobile == NetworkInfo.State.CONNECTED
                    || mobile == NetworkInfo.State.CONNECTING) {
                status = true;
            }
            if (wifi == NetworkInfo.State.CONNECTED
                    || wifi == NetworkInfo.State.CONNECTING) {
                status = true;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                // 8
                NetworkInfo.State wimax = null;
                if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX) != null) {
                    wimax = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX)
                            .getState();
                }

                if (wimax != null) {
                    Log.d(TAG, "Wimax State: " + wimax);
                }

                if (wimax == NetworkInfo.State.CONNECTED
                        || wimax == NetworkInfo.State.CONNECTING) {
                    status = true;
                }

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                // 13
                NetworkInfo.State bluetooth = null;
                if (cm.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH) != null) {
                    bluetooth = cm.getNetworkInfo(
                            ConnectivityManager.TYPE_BLUETOOTH).getState();
                }

                // 13
                NetworkInfo.State ethernet = null;
                if (cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) != null) {
                    ethernet = cm.getNetworkInfo(
                            ConnectivityManager.TYPE_ETHERNET).getState();
                }

                if (bluetooth != null) {
                    Log.d(TAG, "Bluetooth State: " + bluetooth);
                }

                if (ethernet != null) {
                    Log.d(TAG, "Ethernet State: " + ethernet);
                }

                if (bluetooth == NetworkInfo.State.CONNECTED
                        || bluetooth == NetworkInfo.State.CONNECTING) {
                    status = true;
                }

                if (ethernet == NetworkInfo.State.CONNECTED
                        || ethernet == NetworkInfo.State.CONNECTING) {
                    status = true;
                }

            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG,
                        "Error while checking network status" + e.getMessage());
            } else {
                Log.e(TAG, "Unknown error while checking network status: "
                        + e.getClass().getName());
            }
            e.printStackTrace();

            //UaEnergyApp.eventBus.post(new OnAppWentOfflineEvent());

            return false;
        }

       /* if (status == true) {
            RevibeFmApp.eventBus.post(new OnAppWentOnlineEvent());
        } else {
            RevibeFmApp.eventBus.post(new OnAppWentOfflineEvent());
        }*/

        return status;

    }

}
