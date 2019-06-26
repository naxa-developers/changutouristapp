package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;
import com.naxa.np.changunarayantouristapp.videoplayer.VideoPlayerActivity;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;

public class PlaceDetailsActivity extends BaseActivity implements View.OnClickListener, FileDownloadView {

    private static final String TAG = "PlaceDetailsActivity";
    ImageView ivImageMain;
    TextView tvPlaceTitle, tvPlaceDesc;
    ImageButton btnView360Image;
    Button btnViewAllNearByPlaces;
    RatingBar ratingBar;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewnearByPlaces;
    LinearLayout llNearByPlacesLayout;

    FileDownloadPresenter fileDownloadPresenter;

    private BaseRecyclerViewAdapter<NearByPlacesPojo, NearByPlacesViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);


        fileDownloadPresenter = new FileDownloadPresenterImpl(this, PlaceDetailsActivity.this);

        setupToolbar("Place Details", false);
        initUI();

        getnewIntent(getIntent());

    }

    private void getnewIntent(Intent intent) {
        if(intent != null){
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            boolean isFromMainPlaceList = (boolean) hashMap.get(KEY_VALUE);
            PlacesDetailsEntity placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if(isFromMainPlaceList){
                llNearByPlacesLayout.setVisibility(View.VISIBLE);
            }

            if(placesDetailsEntity != null) {
                setValueOnView(placesDetailsEntity);
                setupToolbar(placesDetailsEntity.getName(), false);

            }
        }
    }

    private void setValueOnView(@NotNull PlacesDetailsEntity placesDetailsEntity) {

        tvPlaceTitle.setText(placesDetailsEntity.getName());
        tvPlaceDesc.setText(placesDetailsEntity.getDescription());

        LoadImageUtils.loadImageToViewFromSrc(ivImageMain, placesDetailsEntity.getPrimaryImage());
    }

    private void initUI() {
        tvPlaceTitle = findViewById(R.id.tv_place_title);
        tvPlaceDesc = findViewById(R.id.tv_place_desc);
        ivImageMain = findViewById(R.id.iv_place_details_main);
        ratingBar = findViewById(R.id.rating_bar_place);
        recyclerViewnearByPlaces = findViewById(R.id.rv_nearby_places);
        llNearByPlacesLayout = findViewById(R.id.ll_nearby_places_layout);
        llNearByPlacesLayout.setVisibility(View.GONE);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnView360Image = findViewById(R.id.btn_view_vr_image);
        btnViewAllNearByPlaces = findViewById(R.id.btn_view_all_nearby_places);
        btnViewAllNearByPlaces.setOnClickListener(this);
        btnView360Image.setOnClickListener(this);

        setuprecyclerView(loadDataToList());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_images:
                    toolbar.setTitle("Images");
                    return true;
                case R.id.action_bottom_videos:
                    toolbar.setTitle("Videos");
                    return true;
                case R.id.action_bottom_audios:
                    toolbar.setTitle("Audios");
                    return true;
                case R.id.action_bottom_map:
                    ActivityUtil.openActivity(MapMainActivity.class, PlaceDetailsActivity.this);
                    return true;
            }
            return false;
        }
    };

    private void setuprecyclerView(List<NearByPlacesPojo> nearByPlacesPojoList) {

        if(nearByPlacesPojoList == null){
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewnearByPlaces.setLayoutManager(manager);
        recyclerViewnearByPlaces.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<NearByPlacesPojo, NearByPlacesViewHolder>(nearByPlacesPojoList, R.layout.nearby_places_recycler_item_row_layout){

            @Override
            public void viewBinded(NearByPlacesViewHolder nearByPlacesViewHolder, final NearByPlacesPojo nearByPlacesPojo, int position) {
                nearByPlacesViewHolder.bindView(nearByPlacesPojo);
                nearByPlacesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PlaceDetailsActivity.this, nearByPlacesPojo.getCategoryName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public NearByPlacesViewHolder attachViewHolder(View view) {
                return new NearByPlacesViewHolder(view);
            }
        };
        recyclerViewnearByPlaces.setAdapter(adapter);
    }

    private List<NearByPlacesPojo> loadDataToList() {

        ArrayList<NearByPlacesPojo> mapCategoryModelArrayList = new ArrayList<>();

        mapCategoryModelArrayList.add(new NearByPlacesPojo("Hospital", getResources().getDrawable(R.drawable.ic_bottom_menu_image_24dp)));
        mapCategoryModelArrayList.add(new NearByPlacesPojo("College", getResources().getDrawable(R.drawable.ic_bottom_menu_video_24dp)));
        mapCategoryModelArrayList.add(new NearByPlacesPojo("Gas Station", getResources().getDrawable(R.drawable.ic_bottom_menu_audio_24dp)));
        mapCategoryModelArrayList.add(new NearByPlacesPojo("Bus Station", getResources().getDrawable(R.drawable.ic_bottom_menu_map_24dp)));
        mapCategoryModelArrayList.add(new NearByPlacesPojo("Restaurant", getResources().getDrawable(R.drawable.ic_bottom_menu_image_24dp)));
        mapCategoryModelArrayList.add(new NearByPlacesPojo("Airport", getResources().getDrawable(R.drawable.ic_bottom_menu_video_24dp)));

        return mapCategoryModelArrayList;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_view_vr_image:
                ActivityUtil.openActivity(VRImageViewActivity.class, PlaceDetailsActivity.this);
                break;

            case R.id.btn_view_all_nearby_places:
                dialog = DialogFactory.createProgressDialog(PlaceDetailsActivity.this , "Please wait!!! \nDownloading file");
                dialog.show();
//                fileDownloadPresenter.handleFileDownload("http://kmc.naxa.com.np/uploads/publication/file/75.mp3", "Audio file test");
                fileDownloadPresenter.handleFileDownload("http://changu.naxa.com.np//assets//admin/SampleVideo_1280x720_1mb_(3).mp4", "Sample video file test");
                break;
        }
    }



Dialog dialog;
    @Override
    public void fileDownloadProgress(int progress, int total, String successMsg) {

    }

    @Override
    public void fileDownloadSuccess(String fileName, String successMsg, boolean isAlreadyExists) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadSuccess: "+fileName + " , "+ successMsg + " , "+isAlreadyExists);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_OBJECT, fileName);
        ActivityUtil.openActivity(VideoPlayerActivity.class, PlaceDetailsActivity.this, hashMap, false);
    }

    @Override
    public void fileDownloadFailed(String failedMsg) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadFailed: "+failedMsg);

    }

}
