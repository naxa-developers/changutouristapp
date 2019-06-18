package com.naxa.np.changunarayantouristapp.map.markerdetailspage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.QueryBuildWithSplitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MarkerDetailsDisplayActivity extends BaseActivity {
    private static final String TAG = "MarkerDetailsDisplay";

    String jsonString;
    List<MarkerDetailsKeyValue> markerDetailsKeyValueListCommn = new ArrayList<MarkerDetailsKeyValue>();
    List<MarkerDetailsKeyValue> markerDetailsKeyValueListSpecf = new ArrayList<MarkerDetailsKeyValue>();

    List<MarkerDetailsKeyValue> markerDetailsKeyValueWholeList = new ArrayList<MarkerDetailsKeyValue>();
    RecyclerView recylcerViewItemDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details_display);

        setupToolbar("Details", false);
        setupListRecycler();

        getIntentData();
        parseReceivedJson(jsonString);

        getFullListToDisplay();

    }


    private void getIntentData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra("data")) {
                Bundle extras = intent.getExtras();
                jsonString = extras.getString("data");
                Log.d(TAG, "onCreate: after Clicked data received" + jsonString);
                parseCommonJson(new JSONObject(jsonString));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListRecycler() {
        MarkerDetailedDisplayAdapter markerDetailedDisplayAdapter = new MarkerDetailedDisplayAdapter(R.layout.marker_detailed_info_display_layout, null);
        recylcerViewItemDetail.setLayoutManager(new LinearLayoutManager(this));
        recylcerViewItemDetail.setAdapter(markerDetailedDisplayAdapter);

    }


    private void parseReceivedJson(String JSON) {
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            if (jsonObject.has("commonPlacesAttrb")) {
                JSONObject jsonCommonObject = new JSONObject(jsonObject.getString("commonPlacesAttrb"));
                parseCommonJson(jsonCommonObject);
            }

            if (jsonObject.has("hospitalFacilities")) {
                JSONObject jsonHospitalObject = new JSONObject(jsonObject.getString("hospitalFacilities"));
                parseHospitalJson(jsonHospitalObject);
            }

            if (jsonObject.has("openSpace")) {
                JSONObject jsonOpenObject = new JSONObject(jsonObject.getString("openSpace"));
                parseOpenJson(jsonOpenObject);
            }

            if (jsonObject.has("educationalInstitutes")) {
                JSONObject jsonEducationObject = new JSONObject(jsonObject.getString("educationalInstitutes"));
                parseEducationJson(jsonEducationObject);
            }

            if (jsonObject.has("wardDetailsModel")) {
                JSONObject jsonEducationObject = new JSONObject(jsonObject.getString("wardDetailsModel"));
                parseWardDetailsJson(jsonEducationObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCommonJson(JSONObject jsonObject) {
        Log.d(TAG, "parseCommonJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListCommn = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseHospitalJson(JSONObject jsonObject) {
        Log.d(TAG, "parseHospitalJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));

    }

    private void parseOpenJson(JSONObject jsonObject) {
        Log.d(TAG, "parseOpenJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseEducationJson(JSONObject jsonObject) {
        Log.d(TAG, "parseEducationJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseWardDetailsJson(JSONObject jsonObject) {
        Log.d(TAG, "parseEducationJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void getFullListToDisplay() {
        markerDetailsKeyValueWholeList.clear();
        markerDetailsKeyValueWholeList.addAll(markerDetailsKeyValueListCommn);
        markerDetailsKeyValueWholeList.addAll(markerDetailsKeyValueListSpecf);

        ((MarkerDetailedDisplayAdapter) recylcerViewItemDetail.getAdapter()).replaceData(markerDetailsKeyValueWholeList);
    }


}
