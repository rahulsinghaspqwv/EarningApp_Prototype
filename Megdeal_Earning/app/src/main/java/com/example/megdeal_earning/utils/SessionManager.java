package com.example.megdeal_earning.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    public SessionManager(Context context){
        this.context=context;
        pref=context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public void createLoginSession(String userId, String name, String mobile, String paytmNumber){
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putString(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_NAME, name);
        editor.putString(Constants.KEY_USER_MOBILE, mobile);
        editor.putString(Constants.KEY_USER_PAYTM, paytmNumber);
        editor.commit();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }
    public void logout(){
        editor.clear();
        editor.commit();
    }
    public String getUserId(){
        return pref.getString(Constants.KEY_USER_ID, "");
    }
    public String getUserName(){return pref.getString(Constants.KEY_USER_NAME, "");}
    public String getUserMobile(){return pref.getString(Constants.KEY_USER_MOBILE, "");}
    public String getUserPaytm(){return pref.getString(Constants.KEY_USER_PAYTM, "");}
    public void updatePaytmNumber(String paytmNumber){
        editor.putString(Constants.KEY_USER_PAYTM, paytmNumber);
        editor.commit();
    }
}
