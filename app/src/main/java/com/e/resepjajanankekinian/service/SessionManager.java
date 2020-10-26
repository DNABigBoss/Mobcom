package com.e.resepjajanankekinian.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.e.resepjajanankekinian.MainActivity;
import com.e.resepjajanankekinian.login;
import com.e.resepjajanankekinian.profil;

import java.util.HashMap;

/**
 * Created by Resep Jajanan Kekinian on 20/10/2020.
 */
public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String id) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if(!this.isLogin()){
            Intent intent = new Intent(context, login.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logut() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, login.class);
        context.startActivity(intent);
        ((profil) context).finish();
    }
}
