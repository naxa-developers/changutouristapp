package com.naxa.np.changunarayantouristapp.barcodereader;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.button.MaterialButton;
import com.google.zxing.Result;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivityWithPermission;
import com.naxa.np.changunarayantouristapp.utils.Constant;

public class QRCodeReaderActivity extends BaseActivityWithPermission {
    
    private static final String TAG = "QRCodeReaderActivity";

    TextView tvQRCode;
    MaterialButton btnScanQrCode;
    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        setupToolbar("QR Code Scan", false);
        initUI();
    }

    private void initUI() {
        tvQRCode = findViewById(R.id.tv_qr_code);
        btnScanQrCode = findViewById(R.id.btn_scan_qr_code);
        btnScanQrCode.setEnabled(false);

        checkPermission(Constant.Permission.RC_CAMERA, new String[]{Manifest.permission.CAMERA},
                getString(R.string.camera_rationale), new PermissionRequestListener() {
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
                        Toast.makeText(QRCodeReaderActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        tvQRCode.setText(result.getText());
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
