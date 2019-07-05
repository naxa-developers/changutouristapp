package com.naxa.np.changunarayantouristapp.imageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.placedetailsview.FileNameAndUrlPojo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;

public class ImageListGridViewActivity extends BaseActivity {

    PlacesDetailsEntity placesDetailsEntity ;
    RecyclerView recyclerView;
    private List<FileNameAndUrlPojo> images;
    private GalleryAdapter mAdapter;
    TextView tvNoDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_grid_view);

        setupToolbar("Images", false);

        initUI(getIntent());
    }

    private void initUI(Intent intent) {
        recyclerView = findViewById(R.id.recycler_view_Images);
        tvNoDataFound = findViewById(R.id.tv_no_data_found);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        if(intent != null){
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if(placesDetailsEntity != null) {
                setupToolbar(placesDetailsEntity.getName(), false);
                setupRecyclerView();
            }
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if(!TextUtils.isEmpty(placesDetailsEntity.getImages())) {
            fetchImagesList();
        }else {
            tvNoDataFound.setVisibility(View.VISIBLE);
        }

           recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void   fetchImagesList() {

        try {
            JSONArray jsonArray = new JSONArray(placesDetailsEntity.getImages());
            for (int i = 0 ; i<jsonArray.length(); i++){
                int imageCount = i+1;
                images.add(new FileNameAndUrlPojo(placesDetailsEntity.getName()+" Image "+imageCount, jsonArray.getString(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();


    }
}
