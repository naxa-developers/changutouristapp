package com.naxa.np.changunarayantouristapp.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtils {

    public static abstract class CameraPermission extends BaseActivity {
        
        AppCompatActivity appCompatActivity;
        public CameraPermission(AppCompatActivity appCompatActivity) {
            this.appCompatActivity = appCompatActivity;
            requestCameraPermission(appCompatActivity);

        }


        @AfterPermissionGranted(Constant.Permission.RC_CAMERA)
        private void requestCameraPermission(AppCompatActivity activity) {
            String[] perms = {Manifest.permission.CAMERA};
            if (EasyPermissions.hasPermissions(activity, perms)) {
                cameraPermisionGranted();
//                listener.onPermissionGranted();
            } else {
                EasyPermissions.requestPermissions(activity, activity.getResources().getString(R.string.camera_rationale),
                        Constant.Permission.RC_CAMERA, perms);
                cameraPermisionDenied();
//                listener.onPermissionDenied();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, appCompatActivity);
            requestCameraPermission(appCompatActivity);
        }

        protected abstract void cameraPermisionGranted ();
        protected abstract void cameraPermisionDenied ();

    }




}
