package com.naxa.np.changunarayantouristapp.selectlanguage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;

public class SelectLanguageViewHolder extends RecyclerView.ViewHolder {
    private TextView tvSelectLanguage;


    public SelectLanguageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvSelectLanguage = itemView.findViewById(R.id.tv_select_language);


    }

    void bindView(LanguageDetails languageDetails) {
        tvSelectLanguage.setText(languageDetails.getName());
    }
}