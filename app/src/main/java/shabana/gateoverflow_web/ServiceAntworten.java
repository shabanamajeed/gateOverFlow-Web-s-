package shabana.gateoverflow_web;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shabana on 6/17/15.
 */
public class ServiceAntworten extends Service {
    private static final String DEBUG_TAG = "gmf-debug";
    Details url_details=new Details();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, System.currentTimeMillis()
                + ": Antwortservice gestartet.");

        // Unsere auszufuehrende Methode.
        loadData();


        return START_STICKY;
    }

    private void loadData() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) { //Internet verf�gbar
            Log.d(DEBUG_TAG, "AntwortService.java: Internetverbindung vorhanden. Code: " + networkInfo);
            new rsstask().execute();  	//AsyncTask starten

        }



    }


    private void benachrichtige(String titel, String description) {
        // TODO Auto-generated method stub
        int mId = 123;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.launchericon)
                        .setContentTitle(titel)
                        .setContentText(description)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, startActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(startActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }



    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, System.currentTimeMillis()
                + ": Antwortservice zerstoert.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    protected class rsstask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ServiceAntworten.this);
                String username = prefs.getString("username", "Error loading Username");
                Log.d(DEBUG_TAG, "Username: " + username);
                URL url = new URL(url_details.getAnswers());
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                Log.d(DEBUG_TAG, "Downlaod gestartet");
                xpp.setInput(getInputStream(url), "UTF_8");


                boolean insideItem = false;
                String titel ="";
                String description = "";
                String time ="";
                String link ="";
                String autor = "";
                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {


                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;

                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem){
                                titel = xpp.nextText();

                            }

                        }else if (xpp.getName().equalsIgnoreCase("description")) {
                            if (insideItem){
                                description = xpp.nextText();

                            }

                        }else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem){
                                time = xpp.nextText();

                            }

                        }else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem){
                                link = xpp.nextText();

                            }

                        }else if (xpp.getName().equalsIgnoreCase("qauthor") ) {
                            if (insideItem){
                                autor = xpp.nextText();
                                Log.d(DEBUG_TAG, autor);
                                if (autor.equals(username))
                                {
                                    String temp = link.substring(link.length()-5, link.length()-1);
                                    Log.d(DEBUG_TAG, "ID entspricht " + temp);
                                    Log.d(DEBUG_TAG, "gespeicherter Wert: " + String.valueOf(prefs.getInt("lastAntwortID", 0)));

                                    if ((Integer.parseInt(temp.trim())) > (prefs.getInt("lastAntwortID", 0))){	//neuer als letzte Antwort ID

                                        if (prefs.getBoolean("antwortcheck", false)) {
                                            benachrichtige("New Answer", "Your question has been answered!");
                                        }
                                        SharedPreferences.Editor ed = prefs.edit();
                                        ed.putInt("lastAntwortID", Integer.parseInt(temp));	//neue ID merken
                                        ed.commit();

                                        break;
                                    }else{
                                        Log.d(DEBUG_TAG, "Keine Neuigkeit, beende Verarbeitung");
                                        break;
                                    }

                                }
                            }

                        }
                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element

                }


            } catch (MalformedURLException e) {
                Log.d(DEBUG_TAG, "AntwortService: MalformedURLException e ");
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.d(DEBUG_TAG, "AntwortService: XmlPullParserException e ");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "AntwortService: IOException e ");
                e.printStackTrace();
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                Log.d(DEBUG_TAG, e.toString() + "NumberFormatException of " );
            }

            return null;
        }

        public InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                return null;
            }
        }
        protected void onPostExecute(String result){
            stopSelf();

        }
    }
}
