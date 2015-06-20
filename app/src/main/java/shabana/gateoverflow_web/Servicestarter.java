package shabana.gateoverflow_web;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

/**
 * Created by shabana on 6/17/15.
 */
public class Servicestarter extends BroadcastReceiver {
    private static final String DEBUG_TAG = "gmf-debug";
/* Hier werden die beiden Hintergrundservice gestartet.
*/

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int time = Integer.parseInt(prefs.getString("peridodendauer", "10"));
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent antwortintent = new Intent(context,
                ServiceAnswer.class);
        PendingIntent antwortenpendingintent = PendingIntent.getService(
                context, 0, antwortintent, 0);
        long intervalantworten = DateUtils.MINUTE_IN_MILLIS * time;

        Intent fragenintent = new Intent(context,
                ServiceQuestion.class);
        PendingIntent fragenpendingintent = PendingIntent.getService(
                context, 0, fragenintent, 0);
        AlarmManager am2 = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        long intervalfragen = DateUtils.MINUTE_IN_MILLIS * time;


        if ((!prefs.getBoolean("running", false)) || (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))) {
            Log.d(DEBUG_TAG, " Bradcast Receiver gestartet.");

            SharedPreferences.Editor ed = prefs.edit();
            ed.putBoolean("running", true);
            ed.commit();
            if (prefs.getBoolean("antwortcheck", false)) {
                long firstStart = System.currentTimeMillis() + 2000;

                am.setInexactRepeating(AlarmManager.RTC, firstStart,
                        intervalantworten, antwortenpendingintent);
                Log.d(DEBUG_TAG, "Antwortservice wird gestartet");
            }

            if (prefs.getBoolean("fragencheck", false)) {

                long firstStart2 = System.currentTimeMillis() + 5000;
                am2.setInexactRepeating(AlarmManager.RTC,
                        firstStart2, intervalfragen, fragenpendingintent);
                Log.d(DEBUG_TAG, "Fragenservice wird gestartet");
            }
        }else{
            am.cancel(antwortenpendingintent);
            am2.cancel(fragenpendingintent);

            if (prefs.getBoolean("antwortcheck", false)) {
                long firstStart = System.currentTimeMillis() + 2000;

                am.setInexactRepeating(AlarmManager.RTC, firstStart,
                        intervalantworten, antwortenpendingintent);
                Log.d(DEBUG_TAG, "Antwortservice wird gestartet");
            }

            if (prefs.getBoolean("fragencheck", false)) {

                long firstStart2 = System.currentTimeMillis() + 5000;
                am2.setInexactRepeating(AlarmManager.RTC,
                        firstStart2, intervalfragen, fragenpendingintent);
                Log.d(DEBUG_TAG, "Fragenservice wird gestartet");
            }


        }
    }
}
