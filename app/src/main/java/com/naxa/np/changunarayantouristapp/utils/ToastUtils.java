package com.naxa.np.changunarayantouristapp.utils;


import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;

import static android.widget.Toast.LENGTH_SHORT;

public final class ToastUtils {



    public static void showToast(@NonNull String msg) {
        Toast.makeText(ChangunarayanTouristApp.getInstance(), msg, LENGTH_SHORT).show();
    }
    private ToastUtils() {

    }

    public static void showShortToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int messageResource) {
        showToast(messageResource, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    public static void showLongToast(int messageResource) {
        showToast(messageResource, Toast.LENGTH_LONG);
    }

    private static void showToast(String message, int duration) {
        Toast.makeText(ChangunarayanTouristApp.getInstance(), message, duration).show();
    }

    private static void showToast(int messageResource, int duration) {
        Toast.makeText(ChangunarayanTouristApp.getInstance(), ChangunarayanTouristApp.getInstance().getString(messageResource), duration).show();
    }

    public static void showShortToastInMiddle(int messageResource) {
        showToastInMiddle(ChangunarayanTouristApp.getInstance().getString(messageResource), Toast.LENGTH_SHORT);
    }

    public static void showShortToastInMiddle(String message) {
        showToastInMiddle(message, Toast.LENGTH_SHORT);
    }

    public static void showLongToastInMiddle(int messageResource) {
        showToastInMiddle(ChangunarayanTouristApp.getInstance().getString(messageResource), Toast.LENGTH_LONG);
    }

    private static void showToastInMiddle(String message, int duration) {
        Toast toast = Toast.makeText(ChangunarayanTouristApp.getInstance(), message, duration);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(21);
        messageTextView.setGravity(Gravity.CENTER);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

