package shabana.gateoverflow_web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by shabana on 6/17/15.
 */
public class webChromeClient extends WebChromeClient {

    // Variables declarations
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;
    ProgressBar progressbar=null;
    Activity MyActivity = null;


    public webChromeClient(ProgressBar progressBar,Activity ParentActivity)
    {
        progressbar=progressBar;
        MyActivity=ParentActivity;
    }
    public void onProgressChanged(WebView view, int progress)   //for showing progress bar
    {
        if (progress < 100) {
            // still loading
            progressbar.setVisibility(View.VISIBLE);
        } else {
            // completely loaded page
            progressbar.setVisibility(View.GONE);
        }

        progressbar.setProgress(progress);
    }
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        selectImage(uploadMsg);

    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        selectImage(uploadMsg);
    }
    //For Android 4.1 + 5 emulator
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        selectImage(uploadMsg);

    }
    public void selectImage(ValueCallback<Uri> uploadMsg)
    {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        MyActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

    }
}

