package shabana.gateoverflow_web;

import android.graphics.Bitmap;
import android.view.InputEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by shabana on 6/17/15.
 */
public class webClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
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
        super.onPageFinished(view, url);

       // progressBar.setVisibility(View.GONE);
    }
}
