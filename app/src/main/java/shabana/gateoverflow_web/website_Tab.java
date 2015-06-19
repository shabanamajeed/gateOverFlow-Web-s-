package shabana.gateoverflow_web;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by shabana on 6/17/15.
 */
public class website_Tab extends Activity {

    //variables declations
    private static final String DEBUG_TAG = "gmf-debug";
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;
    private Details url_Details = new Details();


    //when activity gets started oncreate gets call

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_TAG, "WebView onCreate()");
        setContentView(R.layout.activity_web_viewseite);


        WebView wv1 = (WebView)findViewById(R.id.webView1);
        wv1.getSettings().setJavaScriptEnabled(true);
       // wv1.loadUrl(url_Details.getMain_Page());

        // for overriding urlloading and filechooser
        wv1.setWebViewClient(new WebViewClient());
        final Activity MyActivity = this;
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar1);
        wv1.setWebChromeClient(new webChromeClient(progress, MyActivity));}

    //this will be called when user has selected the image

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
       if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }


    }

    // currently unused optionmenu is not in app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_web_viewseite, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        WebView wv1 = (WebView)findViewById(R.id.webView1);
        switch (item.getItemId()) {
            case R.id.aktualisieren:
                wv1.reload();
                Log.d(DEBUG_TAG, "WebView Reload");
                return true;
            case R.id.impressum:
                wv1.loadUrl(url_Details.getQuestions());
                Log.d(DEBUG_TAG, "WebView Lade: http://gateoverflow.in/questions");
                return true;
            case R.id.home:
                wv1.loadUrl(url_Details.getQa());
                Log.d(DEBUG_TAG, "WebView Lade: http://gateoverflow.in/qa");
                return true;
            case R.id.service:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(website_Tab.this);
                SharedPreferences.Editor ed = prefs.edit();
                if (prefs.getBoolean("antwortcheck", false)) {//Service l�uft
                    ed.putBoolean("antwortcheck", false);
                    Toast.makeText(website_Tab.this, "Service wurde gestoppt", Toast.LENGTH_LONG).show();
                }else{//Service l�uft nicht
                    ed.putBoolean("antwortcheck", true);
                    Toast.makeText(website_Tab.this, "Service wurde gestartet", Toast.LENGTH_LONG).show();
                }
                ed.commit();
                Intent intent = new Intent();
                intent.setAction("de.arjun.servicestarten");
                sendBroadcast(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    final Activity MyActivity = this;
    private class MyWebViewClient extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebView wv1 = (WebView)findViewById(R.id.webView1);
            wv1.loadUrl(Uri.parse(url).getHost());

            return true;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        WebView myWebView = (WebView)findViewById(R.id.webView1);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume(){
        super.onResume();
        WebView wv1 = (WebView)findViewById(R.id.webView1);
        startActivity tabAct = (startActivity)getParent();
        String url =  tabAct.getURL();
        wv1.loadUrl(url);
    }

    protected void onPause(){
        super.onPause();
        WebView wv1 = (WebView)findViewById(R.id.webView1);
        startActivity tabAct = (startActivity)getParent();
        tabAct.setURL(wv1.getOriginalUrl());
    }
}