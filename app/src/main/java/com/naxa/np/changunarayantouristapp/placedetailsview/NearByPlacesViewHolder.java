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

public class NearByPlacesViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvNearByPlaceName;
    private ImageView ivNearByPlaceImage;


    public NearByPlacesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNearByPlaceName = itemView.findViewById(R.id.tv_nearby_place_name);
        ivNearByPlaceImage = itemView.findViewById(R.id.iv_nearby_place_image);


    }

    void bindView(PlacesDetailsEntity placesDetailsEntity) {
        tvNearByPlaceName.setText(placesDetailsEntity.getName());

        if(!TextUtils.isEmpty(placesDetailsEntity.getPrimaryImage())){
            LoadImageUtils.loadImageToViewFromSrc(ivNearByPlaceImage, placesDetailsEntity.getPrimaryImage());
        }

    }
}
