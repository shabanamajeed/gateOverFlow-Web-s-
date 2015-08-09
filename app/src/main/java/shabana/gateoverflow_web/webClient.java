package shabana.gateoverflow_web;

import android.graphics.Bitmap;
import android.view.InputEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by shabana on 6/17/15.
 */
public class webClient extends WebViewClient {
    ProgressBar progressbar=null;

    public webClient(ProgressBar progressBar)
    {
        progressbar=progressBar;
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
        progressbar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub

        view.loadUrl(url);
        return true;

    }
    //shabana

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        // issue fix by shabana progress bar multiple times removal
        progressbar.setVisibility(View.GONE);
        super.onPageFinished(view, url);

    }
}
