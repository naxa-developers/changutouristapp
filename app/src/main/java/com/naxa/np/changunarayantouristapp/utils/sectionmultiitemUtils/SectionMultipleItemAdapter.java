package com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.events.MapDataLayerListCheckEvent;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class SectionMultipleItemAdapter extends BaseSectionMultiItemQuickAdapter<SectionMultipleItem, BaseViewHolder> {
    /**
     * init SectionMultipleItemAdapter
     * 1. add your header resource layout
     * 2. add some kind of items
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */

    int sectionItemCount = 0;

    SharedPreferenceUtils sharedPreferenceUtils;

    public SectionMultipleItemAdapter(int sectionHeadResId, List data) {
        super(sectionHeadResId, data);

        addItemType(SectionMultipleItem.TEXT, R.layout.item_text_view);
        addItemType(SectionMultipleItem.IMG, R.layout.item_image_view);
        addItemType(SectionMultipleItem.IMG_TEXT, R.layout.item_img_text_view);
        addItemType(SectionMultipleItem.MAP_DATA_LIST, R.layout.map_data_layer_list_row_custom_dialog_layout);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final SectionMultipleItem item) {
        // deal with header viewHolder
        TextView view = helper.getView(R.id.header_no);
        view.setVisibility(item.isHeadListNo() ? View.VISIBLE : View.GONE);

        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionMultipleItem item) {

        Log.d(TAG, "convertHead: setupRecyclerView "+item.getMultiItemSectionModel().getData_key());
        if(item.isHeadListNo()){
            sectionItemCount++;
            Log.d(TAG, "convertHead: setupRecyclerView "+item.getMultiItemSectionModel().getData_key());
            Log.d(TAG, "convertHead: setupRecyclerView "+item.getMultiItemSectionModel().getData_value());
            Log.d(TAG, "convertHead: setupRecyclerView "+item.getMultiItemSectionModel().getImage());
            helper.setText(R.id.header_no, sectionItemCount );
        }

        // deal with multiple type items viewHolder
        helper.addOnClickListener(R.id.card_view);
        switch (helper.getItemViewType()) {
            case SectionMultipleItem.TEXT:
                helper.setText(R.id.tv, item.getMultiItemSectionModel().getData_key());
                helper.setText(R.id.tv1, item.getMultiItemSectionModel().getData_value());
                break;

            case SectionMultipleItem.IMG:

                ImageView imageView = helper.getView(R.id.iv);
                Glide
                        .with(imageView.getContext())
                        .load(item.getMultiItemSectionModel().getImage())
                        .fitCenter()
                        .into(imageView);

                break;

            case SectionMultipleItem.IMG_TEXT:
                helper.setText(R.id.tv, item.getMultiItemSectionModel().getData_key());

                ImageView imageView1 = helper.getView(R.id.iv);
                Glide
                        .with(imageView1.getContext())
                        .load(item.getMultiItemSectionModel().getImage())
                        .fitCenter()
                        .into(imageView1);
                break;

            case SectionMultipleItem.MAP_DATA_LIST:
                Log.d(TAG, "convertHead: setupRecyclerView "+item.getMultiItemSectionModel().getData_key());

                Switch switchButton = helper.getView(R.id.switchMapCategoryData);
                sharedPreferenceUtils = new SharedPreferenceUtils(switchButton.getContext());


                helper.setText(R.id.switchMapCategoryData, item.getMultiItemSectionModel().getData_key());
                ImageView imageViewData = helper.getView(R.id.ivCategoryIndicator);
                Glide
                        .with(imageViewData.getContext())
                        .load(item.getMultiItemSectionModel().getImage())
                        .fitCenter()
                        .into(imageViewData);

                if(sharedPreferenceUtils.getBoolanValue(item.getMultiItemSectionModel().getData_key(), false)){
                    switchButton.setChecked(true);
                    Log.d(TAG, "convert: Checked");
                    EventBus.getDefault().post(new MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent(item.getMultiItemSectionModel(), true));
                }else {
                    switchButton.setChecked(false);
                    Log.d(TAG, "convert: Unchecked");
                    EventBus.getDefault().post(new MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent(item.getMultiItemSectionModel(), false));
                }

                switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, "onCheckedChanged: "+item.getMultiItemSectionModel().getData_key() + " is checked "+isChecked);
                        if(isChecked){
                            EventBus.getDefault().post(new MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent(item.getMultiItemSectionModel(), true));
                            sharedPreferenceUtils.setValue(item.getMultiItemSectionModel().data_key, true);
//                            Toast.makeText(mContext, item.getMultiItemSectionModel().data_key +" Checked", Toast.LENGTH_SHORT).show();
                        }else {
                            EventBus.getDefault().post(new MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent(item.getMultiItemSectionModel(), false));
                            sharedPreferenceUtils.setValue(item.getMultiItemSectionModel().data_key, false);
//                            Toast.makeText(mContext, item.getMultiItemSectionModel().data_key +" Unchecked", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        }
    }
}
