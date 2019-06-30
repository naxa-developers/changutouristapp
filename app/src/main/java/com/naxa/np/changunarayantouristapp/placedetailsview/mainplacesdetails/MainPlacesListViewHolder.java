package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

public class MainPlacesListViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvPlaceName;
    private ImageView ivPlaceImage;


    public MainPlacesListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPlaceName = itemView.findViewById(R.id.tv_main_place_name);
        ivPlaceImage = itemView.findViewById(R.id.iv_main_place_image);

    }

    public void bindView(MainPlaceListDetails mainPlaceListDetails) {
        tvPlaceName.setText(mainPlaceListDetails.getTitle());
        if(mainPlaceListDetails.getImage() != null){
            LoadImageUtils.loadImageToViewFromSrc(ivPlaceImage, mainPlaceListDetails.getImage());
        }
    }
}