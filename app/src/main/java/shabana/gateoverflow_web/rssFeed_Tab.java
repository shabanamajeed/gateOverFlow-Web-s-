/*
	App Name: Gute-Mathe-Fragen
	App URI: https://play.google.com/store/apps/details?id=de.gute_mathe_fragen
	App Description: This app connects to our math forum and shows the website (mobile interface) within a webview, it also reads the RSS feeds to display all questions and meta data.
	App Version: 1.0
	App Date: 2013-03-08
	App Authors: blackfire185, echteinfachtv
	App Author URI: http://www.echteinfach.tv/
	App License: GPLv3

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version. If you use the source code, you 
	have to keep the copyright notice in your files plus the author name. 

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU General Public License for more details:
	http://www.gnu.org/licenses/gpl.html
*/
package shabana.gateoverflow_web;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class rssFeed_Tab extends Activity {

    ArrayList<String> links = new ArrayList();
    private static final int DIALOG_ALERT = 10;
    private static final String SPEED_TAG = "gmf-speed";

    private static final String DEBUG_TAG = "gmf-debug";

    private static final String COMPARE = "gmf-vergleich";
    ListView msgList;
    ArrayList details;
    AdapterView.AdapterContextMenuInfo info;
    boolean running = false;

    //Accessing url from Details
    Details url_details=new Details();



    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent = new Intent();
        intent.setAction("de.arjun.servicestarten");
        sendBroadcast(intent);

        async();

        final ListView lv = (ListView)findViewById(R.id.listView1);
        lv.setOnItemClickListener(new OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {

                String link = links.get(position).toString();
                Log.d(DEBUG_TAG, "Fragen.java: OnItemClick(), wechsle zu Tab 0 mit url: "+ link);
                startActivity tabAct = (startActivity)getParent();
                tabAct.setURL(link);
                tabAct.getTabHost().setCurrentTab(0);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_rss, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection  for layout see activity_questionon.xml
        switch (item.getItemId()) {
            case R.id.update:
                async();
                return true;
            case R.id.rssactivityfeed:
                startActivity tabAct = (startActivity)getParent();
                tabAct.setURL(url_details.getRss_activity());
                tabAct.getTabHost().setCurrentTab(0);
                return true;
            case R.id.rssquestionfeed:
                startActivity tabActa = (startActivity)getParent();
                tabActa.setURL(url_details.getRss());
                tabActa.getTabHost().setCurrentTab(0);
                return true;
            case R.id.rssanswerfeed:
                startActivity tabAct1 = (startActivity)getParent();
                tabAct1.setURL(url_details.getRss_answers());
                tabAct1.getTabHost().setCurrentTab(0);
                return true;
            case R.id.setting:
                Intent i = new Intent(rssFeed_Tab.this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void async (){		//Internet pr�fen und Async starten
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) { //Internet verf�gbar
            Log.d(DEBUG_TAG, "Fragen.java: Internetverbindung vorhanden. Code: " + networkInfo);
            new rsstask().execute();  	//AsyncTask starten

        } else {		//keine Verbindung
            showDialog(DIALOG_ALERT);
            Log.d(DEBUG_TAG, "Fragen.java: Keine Internetverbindung. Zeige Dialog");
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT:
                // Create out AlterDialog
                Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.networkerror);
                builder.setCancelable(true);
                builder.setPositiveButton("Neu versuchen", new RetryOnClickListener());
                builder.setNegativeButton("Abbrechen", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Log.d(DEBUG_TAG, "Fragen.java: Beenden im Dialog");
            finish();

        }
    }

    private final class RetryOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            Log.d(DEBUG_TAG, "Fragen.java: Retry Internetverbindung");
            async();
        }
    }

    protected void onResume(){
        super.onResume();
        if (!running){
            async();
        }
    }

    private class rsstask extends AsyncTask<String, Integer, String>{
        ArrayList<Details> details =  new ArrayList();
        AdapterView.AdapterContextMenuInfo info;
        long start =0;
        long end = 0;
        long differenz = 0;
        long startges, endges, differenzges, addition = 0;
        String link ="";

        @Override
        protected String doInBackground(String... arg0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rssFeed_Tab.this);
            Editor ed = prefs.edit();
            String s = "";
            String szeit = null; Integer izeit = 0;
            String zeittemp1, zeittemp2;
            long startges = System.nanoTime();	//Speed Messung
            try {
                URL url = new URL(url_details.getRss());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");
                Details Detail;
                boolean insideItem = false;
                String stringname = "";
                String stringdescription = "";
                String stringtime = "";
                String autor = "";
                String antworten = "";
                int fortschritt = 0;
                String[] temp;
                int id=0;
                String seite ="";
                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {


                    if (eventType == XmlPullParser.START_TAG) {
                        Detail = new Details();


                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;

                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem){
                                stringname = xpp.nextText();


                            }
                        }else if (xpp.getName().equalsIgnoreCase("description")) {


                            if (insideItem){
                                //start = System.nanoTime();
                                Document doc = Jsoup.parse(xpp.nextText());
                                s = doc.body().text();


                                if (s.length()<161){
                                    stringdescription =s;

                                }else{
                                    stringdescription = s.substring(0, 160) + " ..."; //extract the headline
                                }

                               }

                        }else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem){
                                stringtime = xpp.nextText().replace(" +0000", "");
                                //stringtime = stringtime.replace("Sun", "So").replace("Sat", "Sa").replace("Fri", "Fr").replace("Thu", "Do").replace("Wed", "Mi").replace("Tue", "Di").replace("Mon", "Mo");
                                //Uhrzeit korrigieren

                                szeit = stringtime.substring(stringtime.length()-8, stringtime.length()-6);

                                izeit = Integer.parseInt(szeit.trim());
                                if (izeit == 23){
                                    izeit = 00;
                                }else{
                                    izeit =  izeit+ 1;
                                }



                                zeittemp1 = stringtime.substring(0, stringtime.length()-8);
                                zeittemp2 = stringtime.substring(stringtime.length()-6, stringtime.length());


                                stringtime = zeittemp1+String.valueOf(izeit)+zeittemp2;
                                stringtime = stringtime.substring(0, stringtime.length()-3);


                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem){
                                seite = xpp.nextText();
                                links.add(seite); //extract the link of article
                                if (fortschritt == 0){
                                    temp = seite.split("/");

                                    try {
                                        id = Integer.parseInt(temp[3]);
                                    } catch (NumberFormatException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    ed.putInt("lastFragenId", id);
                                    ed.commit();
                                    Log.d(DEBUG_TAG ,String.valueOf(id));
                                }
                            }
                        }else if (xpp.getName().equalsIgnoreCase("author")) {
                            if (insideItem)
                                autor = "Author: " + xpp.nextText();
                            Detail.setName(stringname);
                            Detail.setDesc(stringdescription);
                            Detail.setTime(stringtime);
                            Detail.setAutor(autor);
                            //Detail.setAnzahlAntworten(antworten);

                            details.add(Detail);
                            fortschritt++;publishProgress(fortschritt);

                        }

                    }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem=false;
                    }

                    eventType = xpp.next(); //move to next element
                }

            } catch (MalformedURLException e) {
                Log.d(DEBUG_TAG, "Fragen.java: MalformedURLException e ");
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.d(DEBUG_TAG, "Fragen.java: XmlPullParserException e ");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Fragen.java: IOException e ");
                e.printStackTrace();
            }
            endges = System.nanoTime();
            differenzges = (endges-startges)/1000/1000;
            return null;
        }

        protected void onPostExecute(String result){
            ProgressBar pbar = (ProgressBar)findViewById(R.id.progressBar1);
            pbar.setVisibility(View.GONE);

            ListView lv1 = (ListView)findViewById(R.id.listView1);
            lv1.setAdapter(new CustomAdapter(details, rssFeed_Tab.this));
            running = false;


        }
        public InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress){
            ProgressBar pbar = (ProgressBar)findViewById(R.id.progressBar1);
            pbar.setProgress(progress[0]);

        }
        protected void onPreExecute(){
            ProgressBar pbar = (ProgressBar)findViewById(R.id.progressBar1);
            pbar.setProgress(0);
            pbar.setVisibility(View.VISIBLE);
            running = true;
        }



    }

}
