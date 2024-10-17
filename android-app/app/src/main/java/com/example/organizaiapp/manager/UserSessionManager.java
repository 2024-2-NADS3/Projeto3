package com.example.organizaiapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userId";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String email, int userId) {
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getUserEmail() != null && getUserId() != -1;
    }
}