package com.qrcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qrcode.core.R;

import org.jetbrains.annotations.Nullable;


/**
 * Created by dinglei on 16/5/27.
 */
public class QrtButtonControl extends LinearLayout {

    ImageView img;
    TextView tvText;
    int unSelelectColor;
    int selectColor;
    private Context context;

    public QrtButtonControl(Context context) {
        this(context, null);
    }

    public QrtButtonControl(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public QrtButtonControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QrButtonControl);
        setupAttr(a);
        setupView();
    }

    public void setupAttr(TypedArray a) {


        Drawable d = a.getDrawable(R.styleable.QrButtonControl_android_src);
        if (d != null) {
            setImageDrawable(d);
        }

        CharSequence text = a.getText(R.styleable.QrButtonControl_android_text);
        setText(text);

        unSelelectColor = a.getColor(R.styleable.QrButtonControl_unselectColor, Color.WHITE);
        selectColor = a.getColor(R.styleable.QrButtonControl_selectColor, Color.YELLOW);


        a.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.qr_button_control, this);
        img = (ImageView) findViewById(R.id.img);
        tvText = (TextView) findViewById(R.id.tv_text);
    }


    private void setupView() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setUnSelelectColor();
    }

    private void setUnSelelectColor() {
        tvText.setTextColor(unSelelectColor);
        img.setColorFilter(unSelelectColor);
    }

    private void setSelectColor() {
        tvText.setTextColor(selectColor);
        img.setColorFilter(selectColor);
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        img.setImageDrawable(drawable);
    }

    public void setImageResource(@DrawableRes int resId) {
        img.setImageResource(resId);
    }

    public void setText(CharSequence text) {
        if (text == null) {
            text = "";
        }
        setText(text.toString());
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        tvText.setText(text);

    }

    public String getText() {
        return tvText.getText().toString();
    }

    public void setText(int resid) {
        tvText.setText(context.getString(resid));
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (selected) {
            setSelectColor();
        } else {
            setUnSelelectColor();
        }
    }
}
