package shabana.gateoverflow_web;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TabHost;


public class startActivity extends TabActivity {


   // String url = "http://postimage.org/";
    String url=new Details().getMain_Page();

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // creating and filling tabs 1-website main page   2-rss feedback

        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("first")
                .setIndicator("Website").setContent(new Intent(this, website_Tab.class)));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator("RSS Feed").
                setContent(new Intent(this, rssFeed_Tab.class)));   // automatically calls activity

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(startActivity.this);
        mTabHost.setCurrentTab(Integer.parseInt(prefs.getString("home", "0")));

    }
    public void setURL(String link) {
        // TODO Auto-generated method stub
        url = link;
    }

    public String getURL() {
        // TODO Auto-generated method stub
        return url;
    }
 }
