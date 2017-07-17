package com.qrcode.zxing;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.qrcode.core.QRCodeView;
import com.qrcode.core.QrcodeResult;


public class ZXingView extends QRCodeView {
    private MultiFormatReader mMultiFormatReader;

    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMultiFormatReader();
    }

    private void initMultiFormatReader() {
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(QRCodeDecoder.HINTS);
    }

    @Override
    public QrcodeResult processData(byte[] data, int width, int height, boolean isRetry) {

        QrcodeResult qrcodeResult = null;

        try {
            PlanarYUVLuminanceSource source = null;
            Rect rect = mScanBoxView.getScanBoxAreaRect(height);
            if (rect != null) {
                source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
            } else {
                source = new PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
            }
            Result  rawResult = mMultiFormatReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
            qrcodeResult = new QrcodeResult();
            qrcodeResult.setCodeFormat(rawResult.getBarcodeFormat().ordinal());
            qrcodeResult.setCode(rawResult.getText());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMultiFormatReader.reset();
        }
        return qrcodeResult;
    }
}