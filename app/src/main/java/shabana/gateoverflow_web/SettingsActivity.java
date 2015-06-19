package shabana.gateoverflow_web;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by shabana on 6/17/15.
 */
public class SettingsActivity extends PreferenceActivity {
    private static final String DEBUG_TAG = "gmf-debug";


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }


    protected void onPause(){
        super.onPause();
        Intent intent = new Intent();
        intent.setAction("de.arjun.servicestarten");
        sendBroadcast(intent);
    }
}
