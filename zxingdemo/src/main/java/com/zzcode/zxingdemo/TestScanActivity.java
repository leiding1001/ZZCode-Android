package com.zzcode.zxingdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.zzcode.QrtButtonControl;
import com.zzcode.core.QRCodeView;
import com.zzcode.core.QrcodeResult;


public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = TestScanActivity.class.getSimpleName();


    private QrtButtonControl qrCodeButtonControl;
    private QrtButtonControl barCodeButtonControl;
    private QrtButtonControl multiCodeButtonControl;

    private QrScanDelegate mQrScanDelegate;
    private Controller currentController;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        mQrScanDelegate = new QrScanDelegate(this);
        mQrScanDelegate.setDelegate(this);
        initButtonControl();

    }

    private void initButtonControl() {
        qrCodeButtonControl = (QrtButtonControl) findViewById(R.id.qrcodeView);
        barCodeButtonControl = (QrtButtonControl) findViewById(R.id.barcodeView);
        multiCodeButtonControl = (QrtButtonControl) findViewById(R.id.mulBarcodeView);

        qrCodeButtonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentController = new QrController();
            }
        });
        barCodeButtonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentController = new BarController();
            }
        });
        multiCodeButtonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentController = new BarcodeMultiController();
            }
        });

        qrCodeButtonControl.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQrScanDelegate.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQrScanDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mQrScanDelegate.onDestroy();
    }


    @Override
    public void onScanQRCodeSuccess(QrcodeResult result) {
        Log.i(TAG, "result:" + result);
        mQrScanDelegate.vibrate();
        mQrScanDelegate.getQRCodeView().startSpotDelay(2500);

        if ((currentController instanceof QrController && BarcodeFormat.QR_CODE.ordinal() != result.getCodeFormat())
                || (!(currentController instanceof QrController) && BarcodeFormat.QR_CODE.ordinal() == result.getCodeFormat())) {
            currentController.scanFailed();
            return;
        }

        if (currentController != null) {
            currentController.handleCode(result.getCode());
        }

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, getString(R.string.msg_camera_framework_bug), Toast.LENGTH_LONG);
    }


    public abstract class Controller {

        String warning;

        public Controller() {
        }

        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public void scanFailed() {
            Toast.makeText(getBaseContext(), warning, Toast.LENGTH_LONG).show();
        }

        abstract void handleCode(String code);

        abstract void onSelected();
    }


    public class QrController extends Controller {

        public QrController() {
            setWarning(getString(R.string.hs_scan_failed));
            onSelected();
        }

        @Override
        void handleCode(String code) {
            //            handleQrCode(code);
            Toast.makeText(getBaseContext(), code, Toast.LENGTH_SHORT).show();
        }

        @Override
        void onSelected() {
            qrCodeButtonControl.setSelected(true);
            barCodeButtonControl.setSelected(false);
            multiCodeButtonControl.setSelected(false);

            mQrScanDelegate.getQRCodeView().changeToScanQRCodeStyle();

            //            onSwitchToQrCode();
        }


    }


    public class BarController extends Controller {

        public BarController() {
            setWarning(getString(R.string.search_scan_failed));
            onSelected();
        }

        @Override
        void handleCode(String code) {
            //            handleBarCode(code);
            Toast.makeText(getBaseContext(), code, Toast.LENGTH_SHORT).show();
        }

        @Override
        void onSelected() {

            qrCodeButtonControl.setSelected(false);
            barCodeButtonControl.setSelected(true);
            multiCodeButtonControl.setSelected(false);
            mQrScanDelegate.getQRCodeView().changeToScanBarcodeStyle();
            //            onSwitchToBarCode();
        }

    }

    public class BarcodeMultiController extends Controller {

        public BarcodeMultiController() {
            setWarning(getString(R.string.multiple_scan_failed));

            onSelected();

        }

        @Override
        void handleCode(String code) {
            //            handleMultiBarcode(code);
            Toast.makeText(getBaseContext(), code, Toast.LENGTH_SHORT).show();
        }

        @Override
        void onSelected() {
            qrCodeButtonControl.setSelected(false);
            barCodeButtonControl.setSelected(false);
            multiCodeButtonControl.setSelected(true);

            mQrScanDelegate.getQRCodeView().changeToScanBarcodeStyle();

            //            onSwitchToMultiBarCode();
        }
    }



}