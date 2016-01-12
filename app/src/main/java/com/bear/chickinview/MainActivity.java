package com.bear.chickinview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

public class MainActivity extends AppCompatActivity {

    private CheckInView mDayOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        initView();
    }

    private void initView() {
        mDayOne = (CheckInView) findViewById(R.id.aty_main_day_one);

        mDayOne.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mDayOne.getViewTreeObserver().removeOnPreDrawListener(this);
                mDayOne.startAnimation();
                return true;
            }
        });
    }
}
