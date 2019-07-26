package com.naxa.np.changunarayantouristapp.map.mapboxutils.mapdialogs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;

public class SelectMaincategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView tvSelectMainCategory;


    public SelectMaincategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSelectMainCategory = itemView.findViewById(R.id.tv_select_map_layer_main_category);


    }

    public void bindView(String maincategoryName) {
        tvSelectMainCategory.setText(maincategoryName);
    }
}