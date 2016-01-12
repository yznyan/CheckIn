package com.bear.chickinview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 签到View
 */
public class CheckInView extends LinearLayout {
    /**
     * 金色皇冠
     */
    public static final int GOLD_CROWN = 0;
    /**
     * 银色皇冠
     */
    public static final int SLIVER_CROWN = 1;

    private ImageView mImgCrown;                //皇冠
    private TextView mTvPoint;                  //积分
    private ImageView mImgLine;                 //线
    private TextView mTvDay;                    //天数
    private ImageView mImgGoldPoint;              //金色积分背景
    private ImageView mImgGrayPoint;            //灰色积分背景
    private View mPointFrame;

    private boolean isShowCrown;
    private boolean isShowDay;
    private boolean isShowLine;
    private boolean isChecked;
    private String day;

    public CheckInView(Context context) {
        super(context);
        initView(context, null);
    }

    public CheckInView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CheckInView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startAnimation() {
        mImgGoldPoint.setImageResource(R.drawable.clip_coin);

        AnimatorSet animatorSet = new AnimatorSet();

        ClipDrawable clipDrawable = (ClipDrawable) mImgGoldPoint.getDrawable();
        clipDrawable.setLevel(6000);

        ValueAnimator clipAnimator = new ObjectAnimator().ofInt(clipDrawable, "level", 6000, 10000);
        ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(mPointFrame, "rotationY", 0.0f, 360.f);
        final ObjectAnimator textScaleXAnimator = ObjectAnimator.ofFloat(mTvPoint, "scaleX", 0.4f, 1.0f);
        final ObjectAnimator textScaleYAnimator = ObjectAnimator.ofFloat(mTvPoint, "scaleY", 0.4f, 1.0f);

        rotationXAnimator.setDuration(400);
        clipAnimator.setDuration(400);

        rotationXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTvPoint.setTextColor(getResources().getColor(R.color.check_in_checked));
                mTvPoint.setVisibility(VISIBLE);
                AnimatorSet textScaleAnimatorSet = new AnimatorSet();
                textScaleAnimatorSet.playTogether(textScaleXAnimator, textScaleYAnimator);
                textScaleAnimatorSet.setDuration(300);
                textScaleAnimatorSet.setInterpolator(new OvershootInterpolator());
                textScaleAnimatorSet.start();
            }
        });
        clipAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mImgGoldPoint.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTvPoint.setVisibility(GONE);
                mImgGrayPoint.setVisibility(GONE);
            }
        });
        animatorSet.play(rotationXAnimator).after(clipAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();
    }

    /**
     * 设置是否已签到
     */
    public void setChecked(boolean isChecked) {
        if (isChecked) {
            mTvPoint.setTextColor(getResources()
                    .getColor(R.color.check_in_checked));
            mImgGrayPoint.setVisibility(GONE);
            mImgGoldPoint.setVisibility(VISIBLE);
            mImgLine.setImageResource(R.drawable.bg_checked_line);
        } else {
            mTvPoint.setTextColor(getResources().getColor(R.color.un_check));
            mImgLine.setImageResource(R.drawable.bg_un_check_line);
            mImgGrayPoint.setVisibility(VISIBLE);
            mImgGoldPoint.setVisibility(GONE);
        }
    }

    public void setShowCrown(boolean isShowSliver) {
        mImgCrown.setVisibility(isShowSliver ? VISIBLE : INVISIBLE);
    }

    public void setShowDay(boolean isShowDay) {
        mTvDay.setVisibility(isShowDay ? VISIBLE : INVISIBLE);
    }

    public void setShowLine(boolean isShowLine) {
        mImgLine.setVisibility(isShowLine ? VISIBLE : INVISIBLE);
    }

    /**
     * 设置积分
     */
    public void setPoint(int point) {
        mTvPoint.setText(String.valueOf(point));
    }

    public void setDay(String day) {
        mTvDay.setText(day);
    }

    public void setCrown(int crown) {
        switch (crown) {
            case GOLD_CROWN: {
                mImgCrown.setBackgroundResource(R.mipmap.ic_checkin_gold_crown);
                break;
            }
            case SLIVER_CROWN: {
                mImgCrown.setBackgroundResource(R.mipmap.ic_checkin_silver_crown);
                break;
            }
        }
    }


    private void initView(Context context, AttributeSet attributeSet) {
        setOrientation(HORIZONTAL);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_check_in_view, this, true);

        mImgLine = (ImageView) findViewById(R.id.check_in_line);
        mImgCrown = (ImageView) findViewById(R.id.check_in_img_sliver);
        mTvDay = (TextView) findViewById(R.id.check_in_tv_day);
        mImgGrayPoint = (ImageView) findViewById(R.id.check_in_img_point_gray);
        mImgGoldPoint = (ImageView) findViewById(R.id.check_in_img_point_gold);
        mPointFrame = findViewById(R.id.check_in_point_frame);
        mTvPoint = (TextView) findViewById(R.id.check_in_tv_point);

        getAttribute(context, attributeSet);

        setShowDay(isShowDay);
        setShowCrown(isShowCrown);
        setShowLine(isShowLine);
        setChecked(isChecked);
        setDay(day);
    }

    /*
    * 获取属性值
    * */
    private void getAttribute(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CheckInView);
            if (typedArray != null) {
                isShowDay = typedArray.getBoolean(R.styleable.CheckInView_show_day, false);
                isShowLine = typedArray.getBoolean(R.styleable.CheckInView_show_line, true);
                isShowCrown = typedArray.getBoolean(R.styleable.CheckInView_show_crown, false);
                isChecked = typedArray.getBoolean(R.styleable.CheckInView_checked, false);
                day = typedArray.getString(R.styleable.CheckInView_day);

                typedArray.recycle();
            }
        }
    }
}
