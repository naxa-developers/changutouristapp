package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

public class MainPlacesListViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvPlaceName;
    private ImageView ivPlaceImage;


    public MainPlacesListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPlaceName = itemView.findViewById(R.id.tv_main_place_name);
        ivPlaceImage = itemView.findViewById(R.id.iv_main_place_image);

    }

    public void bindView(PlacesDetailsEntity placesDetailsEntity) {
        tvPlaceName.setText(placesDetailsEntity.getName());
        if(!TextUtils.isEmpty(placesDetailsEntity.getPrimaryImage())){
            LoadImageUtils.loadImageToViewFromSrc(ivPlaceImage, placesDetailsEntity.getPrimaryImage());
        }
    }

    private synchronized String  fetchPromaryImageFromList(@NotNull PlacesDetailsEntity placesDetailsEntity) {
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

}