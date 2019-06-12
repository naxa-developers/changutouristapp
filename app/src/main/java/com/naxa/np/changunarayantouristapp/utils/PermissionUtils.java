package com.naxa.np.changunarayantouristapp.utils;

import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;

import com.naxa.np.changunarayantouristapp.R;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtils {

    public abstract static class CameraPermission extends AppCompatActivity{
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
            } else {
                EasyPermissions.requestPermissions(activity, activity.getResources().getString(R.string.camera_rationale),
                        Constant.Permission.RC_CAMERA, perms);
                cameraPermisionDenied();
            }
        }
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            // Forward results to EasyPermissions
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, appCompatActivity);
        }

        protected abstract void cameraPermisionGranted ();
        protected abstract void cameraPermisionDenied ();

    }



}
