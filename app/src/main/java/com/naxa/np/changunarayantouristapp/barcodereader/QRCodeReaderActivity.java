package com.naxa.np.changunarayantouristapp.barcodereader;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.Result;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces.NearByPlacesListActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.FieldValidatorUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.CAMERA;
import static com.naxa.np.changunarayantouristapp.utils.Constant.PermissionID.RC_CAMERA;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;

public class QRCodeReaderActivity extends BaseActivity {
    
    private static final String TAG = "QRCodeReaderActivity";

    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;

    EditText tvQRCode;
    MaterialButton btnScanQrCode, btnSubmitQRCode;
    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        setupToolbar("QR Code Scan", false);
        initUI();
    }

    private void initUI() {
        tvQRCode = findViewById(R.id.tv_qr_code);
        btnScanQrCode = findViewById(R.id.btn_scan_qr_code);
        btnSubmitQRCode = findViewById(R.id.btn_submit_qr_code);
        btnScanQrCode.setEnabled(false);
        btnSubmitQRCode.setEnabled(false);

        tvQRCodeTextChangeListner();

        btnSubmitQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaceDetails();
            }
        });



        checkPermission(RC_CAMERA, new String[]{CAMERA},
                getString(R.string.camera_rationale),new PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        setupQRCodeReader();
                    }

                    @Override
                    public void onPermissionDenied() {
                        return;
                    }
                });

    }

    private void tvQRCodeTextChangeListner() {
        tvQRCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    btnSubmitQRCode.setEnabled(true);
                }
            }
        });
    }

    private void getPlaceDetails() {
        if(FieldValidatorUtils.validateEditText(tvQRCode)){
            placeDetailsEntityViewModel.getPlacesDetailsEntityBYQRCode(tvQRCode.getText().toString(),
                    SharedPreferenceUtils.getInstance(QRCodeReaderActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<PlacesDetailsEntity>() {
                        @Override
                        public void onSuccess(PlacesDetailsEntity placesDetailsEntity) {
                            HashMap<String, Object> hashMap3 = new HashMap<>();
                            hashMap3.put(KEY_VALUE, false);
                            hashMap3.put(KEY_OBJECT, placesDetailsEntity);
                            ActivityUtil.openActivity(PlaceDetailsActivity.class, QRCodeReaderActivity.this, hashMap3, false);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                        }
                    });
        }

    }




    private void setupQRCodeReader() {
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        btnScanQrCode.setEnabled(true);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeReaderActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvQRCode.setText(result.getText());
                        btnSubmitQRCode.setEnabled(true);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
//                LoadImageUtils.downloadAndSaveImageToStorage(QRCodeReaderActivity.this, "butterfly", "https://helpx.adobe.com/content/dam/help/en/stock/how-to/visual-reverse-image-search/jcr_content/main-pars/image/visual-reverse-image-search-v2_intro.jpg");
//                LoadImageUtils.downloadAndSaveImageToStorage(QRCodeReaderActivity.this, "nature", "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832_960_720.jpg");
            }
        });
    }

    @Override
    protected void onResume() {
        if(mCodeScanner != null){
            mCodeScanner.startPreview();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mCodeScanner != null){
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

}
