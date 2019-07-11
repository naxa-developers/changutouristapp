package com.naxa.np.changunarayantouristapp.touristinformationguide;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.naxa.np.changunarayantouristapp.R;

public class TouristGuideListViewHolder extends RecyclerView.ViewHolder  {

    private ExpandableTextView tvTouristGuideDesc;
    private TextView tvTouristGuideCount;


    public TouristGuideListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTouristGuideDesc = itemView.findViewById(R.id.tv_tourist_guide_expand_text_view);
        tvTouristGuideCount = itemView.findViewById(R.id.tv_expandable_text_count);

    }

    public void bindView(TouristInformationGuideDetails touristInformationGuideDetails, int pos) {
        tvTouristGuideCount.setText(pos+ ".");
        tvTouristGuideDesc.setText(touristInformationGuideDetails.getDescription());
    }
}