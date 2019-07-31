package com.naxa.np.changunarayantouristapp.map.mapboxutils.mapdialogs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;

public class SelectMaincategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView tvSelectMainCategory;
    private CardView cardViewCategoryHead;


    public SelectMaincategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSelectMainCategory = itemView.findViewById(R.id.tv_select_map_layer_main_category);
        cardViewCategoryHead = itemView.findViewById(R.id.card_view_map_category_title);

    }

    public void bindView(String maincategoryName, int position, int selectedosition) {

        if(position == selectedosition){
            cardViewCategoryHead.setCardBackgroundColor(cardViewCategoryHead.getContext().getResources().getColor(R.color.colorPrimaryLight));
            tvSelectMainCategory.setBackgroundColor(tvSelectMainCategory.getContext().getResources().getColor(R.color.colorPrimaryLight));
        }

        tvSelectMainCategory.setText(maincategoryName);
    }

}