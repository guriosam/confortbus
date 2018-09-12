package sam.gurio.confortbus.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gurio on 26/03/2017.
 */

public class AppPreferences {

    //####### Class Internal Data #########
    private static AppPreferences instance;
    private static Context context;
    private SharedPreferences sharedPreferences;

    //######## UserData ##########
    private String username;
    private boolean userLogged;

    private boolean persistent;
    private String tempUser;
    private String tempEmail;
    private String tempNome;
    private boolean admin;


    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("CONFORTBUS", Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance(Context mContext) {
        context = mContext;
        if (instance == null) {
            return new AppPreferences(context);
        }
        return instance;
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername() {
        username = sharedPreferences.getString("username", "");
        return username;
    }

    public void setUserLogged(boolean userLogged) {
        this.userLogged = userLogged;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("userLogged", userLogged);
        editor.commit();
    }

    public boolean isUserLogged() {
        userLogged = sharedPreferences.getBoolean("userLogged", false);
        return userLogged;
    }

    public void clearPreferences() {
        setUserLogged(false);
        setUsername("");
        setUserLogged(false);
        setAdmin(false);
    }


    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("persistent", persistent);
        editor.commit();
    }


    public boolean isPersistent() {
        this.persistent = sharedPreferences.getBoolean("persistent", false);
        return persistent;
    }


    public void setTempUser(String tempUser) {
        this.tempUser = tempUser;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tempUser", tempUser);
        editor.commit();
    }


    public String getTempUser() {
        tempUser = sharedPreferences.getString("tempUser", "");
        return tempUser;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tempEmail", tempEmail);
        editor.commit();
    }


    public String getTempEmail() {
        tempEmail = sharedPreferences.getString("tempEmail", "");
        return tempEmail;
    }

    public void setTempNome(String tempNome) {
        this.tempNome = tempNome;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tempNome", tempNome);
        editor.commit();
    }


    public String getTempNome() {
        tempNome = sharedPreferences.getString("tempNome", "");
        return tempNome;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("admin", admin);
        editor.commit();
    }


    public boolean isAdmin() {
        this.admin = sharedPreferences.getBoolean("admin", false);
        return admin;
    }


}
