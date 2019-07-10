package com.naxa.np.changunarayantouristapp.touristinformationguide;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.utils.textviewutils.ExpandableTextView;

public class TouristGuideListViewHolder extends RecyclerView.ViewHolder  {

    private ExpandableTextView tvTouristGuideDesc;
    private TextView tvTouristGuideTitle;


    public TouristGuideListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTouristGuideDesc = itemView.findViewById(R.id.tv_tourist_guide_expand_text_view);

    }

    public void bindView(TouristInformationGuideDetails touristInformationGuideDetails) {
        tvTouristGuideDesc.setText(touristInformationGuideDetails.getDescription());
    }
}