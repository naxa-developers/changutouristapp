package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;

public class MainPlacesListViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvPlaceName;
    private TextView tvPlaceDistance;
    private ImageView ivPlaceImage;
    CardView cardView;


    public MainPlacesListViewHolder(@NonNull View itemView, Display display, boolean isFromMainList) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_view);
        tvPlaceName = itemView.findViewById(R.id.tv_main_place_name);
        tvPlaceDistance = itemView.findViewById(R.id.tv_Places_distance);
        ivPlaceImage = itemView.findViewById(R.id.iv_main_place_image);

        if(isFromMainList){
            setCardHeight(cardView, display);
        }

    }

    public void bindView(PlacesDetailsEntity placesDetailsEntity, Float distance) {
        tvPlaceName.setText(placesDetailsEntity.getName());
        if(!TextUtils.isEmpty(fetchPrimaryImageFromList(placesDetailsEntity))){
            LoadImageUtils.loadImageToViewFromSrc(ivPlaceImage, fetchPrimaryImageFromList(placesDetailsEntity));
        }

        if(distance != null){
            tvPlaceDistance.setText(meterToKMConverter(distance));
        }else {
            tvPlaceDistance.setVisibility(View.INVISIBLE);
        }

    }

    private synchronized String fetchPrimaryImageFromList(@NotNull PlacesDetailsEntity placesDetailsEntity) {
        String primaryImage = null;

        if(!TextUtils.isEmpty(placesDetailsEntity.getImages())) {
            try {
                JSONArray jsonArray = new JSONArray(placesDetailsEntity.getImages());
                if (jsonArray.length() > 0) {
                    primaryImage = jsonArray.optString(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return primaryImage;
    }

    public String meterToKMConverter (Float distance){

        String convertedDistance = "distance in K.m";
        Float distanceInKm ;
        if(distance > 1000){
            distanceInKm = distance/1000;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            float twoDigitsDistance = Float.valueOf(decimalFormat.format(distanceInKm));
            convertedDistance = twoDigitsDistance + " kms away";

        }else {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            float twoDigitsDistance = Float.valueOf(decimalFormat.format(distance));
            convertedDistance = twoDigitsDistance + " meters away";
        }

        return convertedDistance;
    }

    private void setCardHeight(@NotNull CardView cardView, @NotNull Display display){
        int width = display.getWidth(); // ((display.getWidth()*20)/100)
        int height = display.getHeight();// ((display.getHeight()*30)/100)
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,(height*3)/8);
        cardView.setLayoutParams(parms);

    }

}