package com.naxa.np.changunarayantouristapp.map.mapboxutils.mapdialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapDataLayerDialogCloseListen;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.MultiItemSectionModel;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItem;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItemAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;

public class MapDialogs {
    private static final String TAG = "MapDialogs";




    public MapDialogs() {
    }


    public  Dialog createMapDataLayerDialog(@NonNull Context context, GeoJsonCategoryViewModel geoJsonCategoryViewModel,
                                                  List<GeoJsonCategoryListEntity> mainCategoryList, boolean isFirsttime, @NonNull MapDataLayerDialogCloseListen listner) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.map_data_filter_custom_dialog_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        RecyclerView recyclerView =  dialog.findViewById(R.id.recyclerViewDialogMapDataCategory);
        RecyclerView recyclerViewMainCategory =  dialog.findViewById(R.id.recyclerViewDialogMapDataMainCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.btn_close_dialog);
        dialogButton.bringToFront();
        ViewCompat.setTranslationZ(dialogButton, 0);


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onDialogClose();
                dialog.dismiss();
            }
        });

        if(mainCategoryList.size() >0) {
            setUpMainCategoryRecycler(context, recyclerViewMainCategory, geoJsonCategoryViewModel, mainCategoryList, recyclerView, 0, mainCategoryList.get(0).getSlug());
        }else {
            Toast.makeText(context, R.string.currently_there_is_no_data_available, Toast.LENGTH_LONG).show();
            return new Dialog(context);
        }

        if (isFirsttime) {
            listner.isFirstTime();
        }
                dialog.getWindow().setAttributes(lp);
        return dialog;
    }


    private void setUpMainCategoryRecycler(Context context, RecyclerView recyclerViewMainCategory, GeoJsonCategoryViewModel geoJsonCategoryViewModel, List<GeoJsonCategoryListEntity> mainCategoryList, RecyclerView recyclerViewMapLayer,
                                           int selectedPosition, String selectedMainCategory) {

        if (mainCategoryList == null) {
            return;
        }
        BaseRecyclerViewAdapter<GeoJsonCategoryListEntity, SelectMaincategoryViewHolder> adapter;
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false );
        recyclerViewMainCategory.setLayoutManager(manager);
        recyclerViewMainCategory.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<GeoJsonCategoryListEntity, SelectMaincategoryViewHolder>(mainCategoryList, R.layout.select_main_category_map_layer_dialog) {

            @Override
            public void viewBinded(SelectMaincategoryViewHolder selectMaincategoryViewHolder, final GeoJsonCategoryListEntity maincategory, int position) {
                selectMaincategoryViewHolder.bindView(maincategory, position, selectedPosition );
                selectMaincategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpMainCategoryRecycler(context, recyclerViewMainCategory, geoJsonCategoryViewModel, mainCategoryList, recyclerViewMapLayer, position, mainCategoryList.get(position).getSlug());
                        recyclerViewMainCategory.scrollToPosition(position);
                    }
                });

            }

            @Override
            public SelectMaincategoryViewHolder attachViewHolder(View view) {
                return new SelectMaincategoryViewHolder(view);
            }
        };
        recyclerViewMainCategory.setAdapter(adapter);

        fetchCategoryWiseLayerDataFromDatabase(context, selectedMainCategory, geoJsonCategoryViewModel, recyclerViewMapLayer);
    }

    private void fetchCategoryWiseLayerDataFromDatabase(Context context, String maincategory, @NotNull GeoJsonCategoryViewModel geoJsonCategoryViewModel, RecyclerView recyclerView) {
        // 1. create entityList which item data extend SectionMultiEntity
         List<SectionMultipleItem> mapDataLayerList = new ArrayList<>();

        geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByLanguage(SharedPreferenceUtils.getInstance(context).getStringValue(KEY_SELECTED_APP_LANGUAGE, null),
                maincategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<GeoJsonCategoryListEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryListEntity> geoJsonCategoryListEntities) {

                        if (geoJsonCategoryListEntities != null && geoJsonCategoryListEntities.size() > 0) {
//                            Log.d(TAG, "onNext: "+geoJsonCategoryListEntities.size());
                            for (int index = 0; index < geoJsonCategoryListEntities.size(); index++) {
                                GeoJsonCategoryListEntity geoJsonCategoryListEntity = geoJsonCategoryListEntities.get(index);
                                if (index == 0) {
                                    mapDataLayerList.add(new SectionMultipleItem(true, geoJsonCategoryListEntity.getSubCategories(), false, false, geoJsonCategoryListEntities.size()+""));
                                }
                                mapDataLayerList.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel(
                                        geoJsonCategoryListEntity.getCategoryMarker(), geoJsonCategoryListEntity.getCategoryTable(), geoJsonCategoryListEntity.getCategoryName())));
                            }
                            setUpFilterlayerRecyclerView(recyclerView, mapDataLayerList);
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


    private static void setUpFilterlayerRecyclerView(@NotNull RecyclerView recyclerView, List<SectionMultipleItem> mapDataCategoryArrayList) {

        SectionMultipleItemAdapter sectionAdapter = new SectionMultipleItemAdapter(R.layout.map_data_layer_list_section_head_custom_layout, mapDataCategoryArrayList);
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SectionMultipleItem item = (SectionMultipleItem) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.card_view:
                        if (item.getMultiItemSectionModel() != null) {
                            if (item.getMultiItemSectionModel().getData_value().equals("") || item.getMultiItemSectionModel().getData_value() == null) {
                                return;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(sectionAdapter);
    }


}
