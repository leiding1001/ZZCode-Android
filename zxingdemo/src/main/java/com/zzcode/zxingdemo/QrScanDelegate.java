package com.zzcode.zxingdemo;

import static android.content.Context.VIBRATOR_SERVICE;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zzcode.core.QRCodeView;
import com.zzcode.zxing.ZXingView;


public class QrScanDelegate  {
    private static final String TAG = QrScanDelegate.class.getSimpleName();

    private QRCodeView mQRCodeView;

    private boolean isOpenLight = false;
    private TextView flashStatusTextView;

    private AppCompatActivity mActivity;
    private Vibrator vibrator;

    public QrScanDelegate(AppCompatActivity activity) {
        mActivity = activity;
        vibrator = (Vibrator) mActivity.getSystemService(VIBRATOR_SERVICE);
        initView();
    }

    /**
     * 必须包含
     * <include layout="@layout/zxing_qrcode_view"/>
     */
    public void initView() {

        mActivity.setSupportActionBar((Toolbar) mActivity.findViewById(R.id.toolbar));
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActivity.getSupportActionBar().setTitle("");

        mQRCodeView = (ZXingView) mActivity.findViewById(R.id.zxingview);


        flashStatusTextView = (TextView) mActivity.findViewById(R.id.flash_status);
        mActivity.findViewById(R.id.flashView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleFlash();
            }
        });
    }


    public QRCodeView getQRCodeView() {
        return mQRCodeView;
    }

    protected void onStart() {
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
        mQRCodeView.showScanRect();
    }

    public void setDelegate(QRCodeView.Delegate delegate){
        mQRCodeView.setDelegate(delegate);
    }

    protected void onStop() {
        mQRCodeView.stopCamera();
    }


    protected void onDestroy() {
        mQRCodeView.onDestroy();
    }

    public void vibrate() {
        vibrator.vibrate(200);
    }


    public void onToggleFlash() {
        if (isOpenLight) {
            mQRCodeView.closeFlashlight();
            isOpenLight = false;
            flashStatusTextView.setText(R.string.qrcode_on_light);
        } else {
            mQRCodeView.openFlashlight();
            isOpenLight = true;
            flashStatusTextView.setText(R.string.qrcode_off_light);
        }
    }
}