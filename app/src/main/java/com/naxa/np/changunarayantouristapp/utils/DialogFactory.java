package com.naxa.np.changunarayantouristapp.utils;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawGeoJsonOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapDataLayerDialogCloseListen;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItem;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_OPENSTREET;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_SATELLITE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_STREET;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_NAGARKOT_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_BASE_LAYER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;


public final class DialogFactory {

    private static final String TAG = "DialogFactory";

    private ProgressDialog progressDialog;

    public DialogFactory() {
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkWithTitleDialog(Context context, String title, String message, onClickListner listner ) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listner.onClick();
                    }
                });
        return alertDialog.create();
    }



    public interface onClickListner {
        public void onClick();
    }


    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createDataSyncErrorDialog(Context context, String message, String code) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(context.getString(R.string.dialog_error_title_sync_failed, code))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }


    public static Dialog createMessageDialogWithRetry(final Context context, String title, String message, onClickListner listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_action_retry,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClick();
                    }
                });
        return alertDialog.create();
    }


    public static AlertDialog.Builder createActionDialog(final Context context, String title, String message) {
        return new AlertDialog.Builder(context, R.style.RiseUpDialog)
                .setTitle(title).setCancelable(false)
                .setMessage(message);
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static Dialog createDataSyncErrorDialog(Context context, String responseCode, @StringRes int messageResource) {
        return createDataSyncErrorDialog(context, context.getString(messageResource), responseCode);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.RiseUpDialog);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressBarDialog(Context context, String title, String message) {

        final ProgressDialog progress = new ProgressDialog(context, R.style.RiseUpDialog);

        DialogInterface.OnClickListener buttonListerns =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                progress.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };


        progress.setMessage(message);
        progress.setTitle(title);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.dialog_action_hide), buttonListerns);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);


        return progress;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static Dialog createBaseLayerDialog(@NonNull Context context, @NonNull CustomBaseLayerDialogListner listner) {

        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.select_base_layer_custom_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_closeDialog);

        Switch street = (Switch) dialog.findViewById(R.id.switchStreetView);
        Switch satellite = (Switch) dialog.findViewById(R.id.switchSatelliteView);
        Switch openStreet = (Switch) dialog.findViewById(R.id.switchOpenStreetView);
        Switch municipality = (Switch) dialog.findViewById(R.id.switchMetropolitianBoundary);
        Switch ward = (Switch) dialog.findViewById(R.id.switchWardBoundary);


        if (sharedPreferenceUtils.getIntValue(MAP_BASE_LAYER, -1) == KEY_STREET) {
            street.setChecked(true);
            satellite.setChecked(false);
            openStreet.setChecked(false);
            listner.onStreetClick();

        } else if (sharedPreferenceUtils.getIntValue(MAP_BASE_LAYER, -1) == KEY_SATELLITE) {
            satellite.setChecked(true);
            street.setChecked(false);
            openStreet.setChecked(false);
            listner.onSatelliteClick();
        } else if (sharedPreferenceUtils.getIntValue(MAP_BASE_LAYER, -1) == KEY_OPENSTREET) {
            openStreet.setChecked(true);
            openStreet.setChecked(true);
            satellite.setChecked(false);
            listner.onOpenStreetClick();
        }


        if (sharedPreferenceUtils.getIntValue(MAP_OVERLAY_LAYER, -1) == KEY_CHANGUNARAYAN_BOARDER) {
            municipality.setChecked(true);
            ward.setChecked(false);
            listner.onChangunarayanBoarderClick();
        } else if (sharedPreferenceUtils.getIntValue(MAP_OVERLAY_LAYER, -1) == KEY_NAGARKOT_BOARDER) {
            ward.setChecked(true);
            municipality.setChecked(false);
            listner.onNagarkotBoarderClick();
        }


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        street.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    satellite.setChecked(false);
                    openStreet.setChecked(false);
                    listner.onStreetClick();
                    sharedPreferenceUtils.setValue(MAP_BASE_LAYER, KEY_STREET);
                }
            }
        });

        satellite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    street.setChecked(false);
                    openStreet.setChecked(false);
                    listner.onSatelliteClick();
                    sharedPreferenceUtils.setValue(MAP_BASE_LAYER, KEY_SATELLITE);
                }
            }
        });

        openStreet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    street.setChecked(false);
                    satellite.setChecked(false);
                    listner.onOpenStreetClick();
                    sharedPreferenceUtils.setValue(MAP_BASE_LAYER, KEY_OPENSTREET);
                }
            }
        });

        municipality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    ward.setChecked(false);
                    listner.onChangunarayanBoarderClick();
                    sharedPreferenceUtils.setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);

                }
            }
        });

        ward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                if (isChecked == true) {
                    municipality.setChecked(false);
                    listner.onNagarkotBoarderClick();
                    sharedPreferenceUtils.setValue(MAP_OVERLAY_LAYER, KEY_NAGARKOT_BOARDER);
                }
            }
        });


        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


    public interface CustomBaseLayerDialogListner {
        void onStreetClick();

        void onSatelliteClick();

        void onOpenStreetClick();

        void onChangunarayanBoarderClick();

        void onNagarkotBoarderClick();
    }


    public Dialog createAudioPlayerDialog(@NonNull Context context, @NonNull String audioName, @NonNull AudioPlayerDialogListner listner) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.audio_player_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        TextView textView = dialog.findViewById(R.id.tv_audio_title);
        textView.setText(audioName);

        ImageButton dialogButton =  dialog.findViewById(R.id.btn_close_dialog);
        ToggleButton btnAudioPlay = dialog.findViewById(R.id.btn_audio_play);
        ToggleButton btnAudioPause = dialog.findViewById(R.id.btn_audio_pause);
        ToggleButton btnAudioStop = dialog.findViewById(R.id.btn_audio_stop);

        setAudioButtonList();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onDialogClose();
                dialog.dismiss();
            }
        });

        btnAudioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonChecked(R.id.btn_audio_play, dialog);
                listner.onAudioPlay();
            }
        });

        btnAudioPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonChecked(R.id.btn_audio_pause, dialog);
                listner.onAudioPause();
            }
        });


        btnAudioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonChecked(R.id.btn_audio_stop, dialog);
                listner.onAudioStop();
            }
        });



        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    List<Integer> audioButtonList = new ArrayList<Integer>();
    private  void setAudioButtonList(){
        audioButtonList.add(R.id.btn_audio_play);
        audioButtonList.add(R.id.btn_audio_pause);
        audioButtonList.add(R.id.btn_audio_stop);
    }


    private void setButtonChecked(int buttonId, Dialog dialog){
        for (int i = 0; i< audioButtonList.size(); i++){
            ToggleButton toggleButton = (ToggleButton) dialog.findViewById(audioButtonList.get(i));
            toggleButton.setChecked(false);
        }
        ToggleButton toggleButton = (ToggleButton) dialog.findViewById(buttonId);
        toggleButton.setChecked(true);

    }

    public interface AudioPlayerDialogListner {
        void onAudioPlay();

        void onAudioPause();

        void onAudioStop();

        void onDialogClose();
    }


    public static Dialog createPlaceRatingDialog(Context context, PlaceRatingDialogListner listner){


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.place_rating_custom_dialog_layout);

        ImageButton btnClose =  dialog.findViewById(R.id.btn_close_dialog);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit_rating);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingbar_dialog);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onRatingClose();
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onSubmitTotalRatingStar(dialog, ratingBar.getRating());
            }
        });


        return dialog;
    }

    public interface PlaceRatingDialogListner{
        void onSubmitTotalRatingStar(Dialog dialog, float starRating);

        void onRatingClose();
    }

}
