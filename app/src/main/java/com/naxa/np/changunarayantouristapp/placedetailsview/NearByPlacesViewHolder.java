package com.naxa.np.changunarayantouristapp.placedetailsview;

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

public class NearByPlacesViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvNearByPlaceName;
    private ImageView ivNearByPlaceImage;


    public NearByPlacesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNearByPlaceName = itemView.findViewById(R.id.tv_nearby_place_name);
        ivNearByPlaceImage = itemView.findViewById(R.id.iv_nearby_place_image);

    }

    public void bindView(PlacesDetailsEntity placesDetailsEntity) {
        tvNearByPlaceName.setText(placesDetailsEntity.getName());

        if(!TextUtils.isEmpty(fetchPromaryImageFromList(placesDetailsEntity))){
            LoadImageUtils.loadImageToViewFromSrc(ivNearByPlaceImage, fetchPromaryImageFromList(placesDetailsEntity));
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
