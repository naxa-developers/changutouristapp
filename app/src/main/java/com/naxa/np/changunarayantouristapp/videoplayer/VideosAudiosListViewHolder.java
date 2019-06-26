package com.naxa.np.changunarayantouristapp.videoplayer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.placedetailsview.FileNameAndUrlPojo;

public class VideosAudiosListViewHolder extends RecyclerView.ViewHolder  {

    private TextView tvFileName;


    public VideosAudiosListViewHolder(@NonNull View itemView) {
        super(itemView);
        tvFileName = itemView.findViewById(R.id.tv);

    }

    public void bindView(FileNameAndUrlPojo fileNameAndUrlPojo) {
        tvFileName.setText(fileNameAndUrlPojo.getName());

    }
}