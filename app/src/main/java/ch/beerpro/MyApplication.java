package ch.beerpro;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The MyApplication class is instantiated once for the whole application and can (but should not) be used as a
 * singleton to store application wide data. The manifest contains a anreference to this class, apart from that it is
 * not referenced in this application.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * This will log whenever we reveice data from firestore. This is useful for debugging and to get a feeling
         * of how much and when new data is received from the database.
         * */
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public boolean getDarkTheme() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean("darkTheme", false);
        return darkTheme;
    }

    public void setDarkTheme(boolean value) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("darkTheme", value);
        editor.commit();
    }

}