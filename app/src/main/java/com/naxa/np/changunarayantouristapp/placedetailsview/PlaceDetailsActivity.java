package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.audioplayer.AudioListActivity;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.imageviewer.ImageListGridViewActivity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlaceListDetails;
import com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces.NearByPlacesListActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;
import com.naxa.np.changunarayantouristapp.videoplayer.VideoListActivity;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;

public class PlaceDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PlaceDetailsActivity";
    ImageView ivImageMain;
    TextView tvPlaceTitle, tvPlaceDesc;
    ImageButton btnView360Image;
    Button btnViewAllNearByPlaces;
    RatingBar ratingBar;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewnearByPlaces;
    LinearLayout llNearByPlacesLayout;

    PlacesDetailsEntity placesDetailsEntity;
    boolean isFromMainPlaceList;
    String mainPlaceType = "";

    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;

    private BaseRecyclerViewAdapter<PlacesDetailsEntity, NearByPlacesViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        setupToolbar("Place Details", false);
        initUI();

        try {
            getnewIntent(getIntent());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void getnewIntent(Intent intent) {
        if (intent != null) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            isFromMainPlaceList = (boolean) hashMap.get(KEY_VALUE);

            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if (isFromMainPlaceList) {
                llNearByPlacesLayout.setVisibility(View.VISIBLE);
                mainPlaceType = SharedPreferenceUtils.getInstance(PlaceDetailsActivity.this).getStringValue(KEY_MAIN_PLACE_TYPE, null);
                initRecyclerView();
            }

            if (placesDetailsEntity != null) {
                setValueOnView(placesDetailsEntity);
                setupToolbar(placesDetailsEntity.getName(), false);
            }

        }
    }

    private void initRecyclerView() {
        Constant constant = new Constant();
        placeDetailsEntityViewModel.getNearByPlacesListByPlaceTypeAndNearByTypeList(mainPlaceType,
                constant.getNearByPlacesTypeList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<PlacesDetailsEntity>>() {
                    @Override
                    public void onNext(List<PlacesDetailsEntity> placesDetailsEntities) {
                        if (placesDetailsEntities != null && placesDetailsEntities.size() > 0) {
                            setuprecyclerView(placesDetailsEntities);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setValueOnView(@NotNull PlacesDetailsEntity placesDetailsEntity) {

        tvPlaceTitle.setText(placesDetailsEntity.getName());
        tvPlaceDesc.setText(placesDetailsEntity.getDescription());

        if (!TextUtils.isEmpty(fetchPromaryImageFromList(placesDetailsEntity))) {
            LoadImageUtils.loadImageToViewFromSrc(ivImageMain, fetchPromaryImageFromList(placesDetailsEntity));
        }
    }

    private synchronized String fetchPromaryImageFromList(@NotNull PlacesDetailsEntity placesDetailsEntity) {
        String primaryImage = null;

        if (!TextUtils.isEmpty(placesDetailsEntity.getImages())) {
            try {
                JSONArray jsonArray = new JSONArray(placesDetailsEntity.getImages());
                if (jsonArray.length() > 0) {
                    primaryImage = jsonArray.optString(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return primaryImage;
    }

    private void setValueOnViewFromMainPlaces(@NotNull MainPlaceListDetails mainPlaceListDetails) {

        tvPlaceTitle.setText(mainPlaceListDetails.getTitle());
        tvPlaceDesc.setText(mainPlaceListDetails.getDescription());

        LoadImageUtils.loadImageToViewFromSrc(ivImageMain, mainPlaceListDetails.getImage());
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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_images:
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(ImageListGridViewActivity.class, PlaceDetailsActivity.this, hashMap, false);
                    return true;

                case R.id.action_bottom_videos:
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(VideoListActivity.class, PlaceDetailsActivity.this, hashMap1, false);
                    return true;

                case R.id.action_bottom_audios:
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(AudioListActivity.class, PlaceDetailsActivity.this, hashMap2, false);
                    return true;

                case R.id.action_bottom_map:
                    HashMap<String, Object> hashMap3 = new HashMap<>();
                    hashMap3.put(KEY_VALUE, isFromMainPlaceList);
                    hashMap3.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(MapMainActivity.class, PlaceDetailsActivity.this, hashMap3, false);
                    return true;
            }
            return false;
        }
    };


    private void setuprecyclerView(List<PlacesDetailsEntity> nearByPlacesPojoList) {

        if (nearByPlacesPojoList == null) {
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewnearByPlaces.setLayoutManager(manager);
        recyclerViewnearByPlaces.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<PlacesDetailsEntity, NearByPlacesViewHolder>(nearByPlacesPojoList, R.layout.nearby_places_recycler_item_row_layout) {

            @Override
            public void viewBinded(NearByPlacesViewHolder nearByPlacesViewHolder, final PlacesDetailsEntity nearByPlacesPojo, int position) {
                nearByPlacesViewHolder.bindView(nearByPlacesPojo);
                nearByPlacesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> hashMap3 = new HashMap<>();
                        hashMap3.put(KEY_VALUE, false);
                        hashMap3.put(KEY_OBJECT, nearByPlacesPojo);
                        ActivityUtil.openActivity(PlaceDetailsActivity.class, PlaceDetailsActivity.this, hashMap3, false);
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_view_vr_image:
                ActivityUtil.openActivity(VRImageViewActivity.class, PlaceDetailsActivity.this);
                break;

            case R.id.btn_view_all_nearby_places:
                ActivityUtil.openActivity(NearByPlacesListActivity.class, PlaceDetailsActivity.this);
                break;
        }
    }


}
