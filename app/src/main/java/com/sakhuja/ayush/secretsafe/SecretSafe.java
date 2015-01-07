package com.sakhuja.ayush.secretsafe;

/**
 * Created by Ayush on 12/30/2014.
 */

import android.app.Application;
import android.content.Context;

public class SecretSafe extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
