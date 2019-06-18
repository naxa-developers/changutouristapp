package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;

public class NearByPlacesViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvNearByPlaceName;
    private ImageView ivNearByPlaceImage;


    public NearByPlacesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNearByPlaceName = itemView.findViewById(R.id.tv_nearby_place_name);
        ivNearByPlaceImage = itemView.findViewById(R.id.iv_nearby_place_image);


    }

    void bindView(NearByPlacesPojo nearByPlacesPojo) {
        tvNearByPlaceName.setText(nearByPlacesPojo.getCategoryName());
        ivNearByPlaceImage.setImageDrawable(nearByPlacesPojo.drawablecategoryIcon);

//        Glide.with(ChangunarayanTouristApp.getInstance())
//                .load(nearByPlacesPojo.drawablecategoryIcon)
//                .into(ivNearByPlaceImage);
    }
}
