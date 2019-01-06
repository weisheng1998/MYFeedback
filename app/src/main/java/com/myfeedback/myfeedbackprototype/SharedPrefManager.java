package com.myfeedback.myfeedbackprototype;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_F_NAME = "userfname",
                                KEY_L_NAME = "userlname",
                                KEY_USER_EMAIL = "useremail",
                                KEY_AGE = "age",
                                KEY_IC = "ic",
                                KEY_ADDRESS = "address",
                                KEY_USER_ID = "userid",
                                KEY_IMAGE_INFO = "imageinfo";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(String... params) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, params[0]);
        editor.putString(KEY_F_NAME, params[1]);
        editor.putString(KEY_L_NAME, params[2]);
        editor.putString(KEY_AGE, params[3]);
        editor.putString(KEY_USER_EMAIL, params[4]);
        editor.putString(KEY_ADDRESS, params[5]);
        editor.putString(KEY_IC, params[6]);
        editor.putString(KEY_IMAGE_INFO, params[7]);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null) != null;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getKeyUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, "MYFeedback");
    }

    public String getKeyFName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_F_NAME, "");
    }

    public String getKeyLName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_L_NAME, "");
    }

    public String getKeyAge(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AGE, "");
    }

    public String getKeyIc(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IC, "");
    }

    public String getKeyImageInfo(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IMAGE_INFO, "");
    }

    public String getKeyUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public String getKeyAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }
}
