package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.mapdialogs.SelectMaincategoryViewHolder;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetails;
import com.naxa.np.changunarayantouristapp.selectlanguage.SelectLanguageViewHolder;
import com.naxa.np.changunarayantouristapp.selectlanguage.SelectlanguageActivity;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItem;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItemAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MapDialogs {
    private static final String TAG = "MapDialogs";



    public static Dialog createMapDataLayerDialog(@NonNull Context context, PlaceDetailsEntityViewModel placeDetailsEntityViewModel,
                                                  List<String> mainCategoryList, boolean isFirsttime, @NonNull MapDataLayerDialogCloseListen listner) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.map_data_filter_custom_dialog_layout);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

//        List<SectionMultipleItem> mapDataCategoryArrayList

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

        setUpMainCategoryRecycler(context, recyclerViewMainCategory, placeDetailsEntityViewModel, mainCategoryList);

//        setUpFilterlayerRecyclerView(recyclerView, );


        if (isFirsttime) {
            listner.isFirstTime();
        }
        //        dialog.getWindow().setAttributes(lp);
        return dialog;
    }


    private static void setUpMainCategoryRecycler(Context context, RecyclerView recyclerViewMainCategory, PlaceDetailsEntityViewModel placeDetailsEntityViewModel, List<String> mainCategoryList) {

        if (mainCategoryList == null) {
            return;
        }
        BaseRecyclerViewAdapter<String, SelectMaincategoryViewHolder> adapter;
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerViewMainCategory.setLayoutManager(manager);
        recyclerViewMainCategory.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<String, SelectMaincategoryViewHolder>(mainCategoryList, R.layout.select_main_category_map_layer_dialog) {

            @Override
            public void viewBinded(SelectMaincategoryViewHolder selectMaincategoryViewHolder, final String maincategory, int position) {
                selectMaincategoryViewHolder.bindView(maincategory);
                selectMaincategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: " + maincategory);

                    }
                });

            }

            @Override
            public SelectMaincategoryViewHolder attachViewHolder(View view) {
                return new SelectMaincategoryViewHolder(view);
            }
        };
        recyclerViewMainCategory.setAdapter(adapter);
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
                                Log.d(TAG, "onItemChildClick: null value ");
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
