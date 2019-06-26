package com.naxa.np.changunarayantouristapp.imageviewer;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.placedetailsview.FileNameAndUrlPojo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_grid_view);

        setupToolbar("Images", false);

        initUI(getIntent());
    }

    private void initUI(Intent intent) {
        recyclerView = findViewById(R.id.recycler_view_Images);
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

        fetchImagesList();
    }

    private void   fetchImagesList() {

        String imagesString = "https://ichef.bbci.co.uk/news/660/cpsprodpb/10D88/production/_103600096_mediaitem103600095.jpg," +
                "https://cdn.pixabay.com/photo/2017/01/06/19/15/soap-bubble-1958650_960_720.jpg," +
                "https://wallpaper.wiki/wp-content/uploads/2017/05/wallpaper.wiki-Sunset-serpentine-road-wallpapers-3840x2160-PIC-WPE0013986.jpg," +
                "https://cdn.pixabay.com/photo/2018/05/28/22/11/message-in-a-bottle-3437294__340.jpg,"+
        "https://ichef.bbci.co.uk/news/660/cpsprodpb/10D88/production/_103600096_mediaitem103600095.jpg," +
        "https://cdn.pixabay.com/photo/2017/01/06/19/15/soap-bubble-1958650_960_720.jpg," +
                "https://wallpaper.wiki/wp-content/uploads/2017/05/wallpaper.wiki-Sunset-serpentine-road-wallpapers-3840x2160-PIC-WPE0013986.jpg," +
                "https://cdn.pixabay.com/photo/2018/05/28/22/11/message-in-a-bottle-3437294__340.jpg";

//        List<String> imagesListUrl = new ArrayList<String>(Arrays.asList(placesDetailsEntity.getImages().split(",")));
        List<String> imagesListUrl = new ArrayList<String>(Arrays.asList(imagesString.split(",")));
        for (String url : imagesListUrl){
            images.add(new FileNameAndUrlPojo(placesDetailsEntity.getName(), url));
        }
        mAdapter.notifyDataSetChanged();
    }
}
