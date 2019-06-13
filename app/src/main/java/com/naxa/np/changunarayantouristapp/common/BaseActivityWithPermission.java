package com.naxa.np.changunarayantouristapp.common;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.network.NetworkApiClient;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.utils.Constant;

import java.util.ArrayList;

public class BaseActivityWithPermission  extends AppCompatActivity {

    protected Toolbar toolbar;
    ProgressDialog progressDialog;

    protected NetworkApiInterface apiInterface = NetworkApiClient.getAPIClient().create(NetworkApiInterface.class);

    protected void setupToolbar(boolean isCenterTitle) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (isCenterTitle) {
            centerToolbarTitle(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    protected void setupToolbar(String title, boolean isCenterTitle) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (isCenterTitle) {
            centerToolbarTitle(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }


    /**
     * https://code.i-harness.com/en/q/194de86
     *
     * @param toolbar
     */
    private void centerToolbarTitle(@NonNull final Toolbar toolbar) {
        final CharSequence title = toolbar.getTitle();
        final ArrayList<View> outViews = new ArrayList<>(1);
        toolbar.findViewsWithText(outViews, title, View.FIND_VIEWS_WITH_TEXT);
        if (!outViews.isEmpty()) {
            final TextView titleView = (TextView) outViews.get(0);
            titleView.setGravity(Gravity.CENTER_HORIZONTAL);
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) titleView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            toolbar.requestLayout();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void createProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    PermissionRequestListener listener;
    int REQUEST_CODE;
    protected void checkPermission(int requestCode, String[] permissions, String rationaleMsg, @NonNull PermissionRequestListener listener){


        REQUEST_CODE = requestCode;
        this.listener = listener;
        ActivityCompat.requestPermissions(this,
                permissions,
                REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                listener.onPermissionGranted();
            } else {

                // permission denied, boo! Disable the
                listener.onPermissionDenied();
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public interface PermissionRequestListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }


}
