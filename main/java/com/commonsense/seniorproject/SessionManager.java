package com.commonsense.seniorproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaCas;
import android.util.Log;

/**
 * Provides a list of getters and setters for stored variables
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "CommonSenseLogin";

    private static final String KEY_USERID = "userID";
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    ////////////////////////////////////////

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified");
    }

    public boolean isLoggedIn() { return pref.getBoolean(KEY_IS_LOGGED_IN, false);}

    ////////////////////////////////////////

    public void setUserID(String userID) {
        editor.putString(KEY_USERID, userID);

        // commit changes
        editor.commit();

        Log.d(TAG, "userID session modified");
    }

    public String getUserID() {return pref.getString(KEY_USERID, null);}

    public void resetUserID() {
        editor.remove(KEY_USERID);
        editor.apply();
    }

    ////////////////////////////////////////

    public void setFirstName(String firstName) {
        editor.putString(KEY_FIRSTNAME, firstName);

        // commit changes
        editor.commit();

        Log.d(TAG, "firstName session modified");
    }

    public String getFirstName() {return pref.getString(KEY_FIRSTNAME, null);}

    public void resetFirstName() {
        editor.remove(KEY_FIRSTNAME);
        editor.apply();
    }

    ////////////////////////////////////////

    public void setLastName(String lastName) {
        editor.putString(KEY_LASTNAME, lastName);

        // commit changes
        editor.commit();

        Log.d(TAG, "lastName session modified");
    }

    public String getLastName() {return pref.getString(KEY_LASTNAME, null);}

    public void resetLastName() {
        editor.remove(KEY_LASTNAME);
        editor.apply();
    }

    ////////////////////////////////////////

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();

        Log.d(TAG, "email session modified");
    }

    public String getEmail() {return pref.getString(KEY_EMAIL, null);}

    public void resetEmail() {
        editor.remove(KEY_EMAIL);
        editor.apply();
    }

    ////////////////////////////////////////

    public void resetAllOnLogout() {
        setLogin(false);
        editor.remove(KEY_USERID);
        editor.remove(KEY_FIRSTNAME);
        editor.remove(KEY_LASTNAME);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }

    public void resetAllExecptID() {
        editor.remove(KEY_FIRSTNAME);
        editor.remove(KEY_LASTNAME);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }

    public void setAllExceptID(String firstName, String lastName, String email) {
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.commit();

        Log.d(TAG, "email session modified");
    }
}
