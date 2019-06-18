package com.naxa.np.changunarayantouristapp.map.markerdetailspage;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.naxa.np.changunarayantouristapp.R;

import java.util.List;


public class MarkerDetailedDisplayAdapter extends BaseQuickAdapter<MarkerDetailsKeyValue, BaseViewHolder> {

    public MarkerDetailedDisplayAdapter(int layoutResId, @Nullable List<MarkerDetailsKeyValue> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MarkerDetailsKeyValue item) {
        helper.setText(R.id.tv_key_data,item.getKey())
                .setText(R.id.tv_value_data, ""+item.getValue());
    }
}
