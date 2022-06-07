package com.i2donate.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;
import com.i2donate.Session.PrefManager;

public class WelcomeActivity extends AppCompatActivity {

    TextView welcome_better_tv,giving_tv;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private TextView btnNext;
    CheckBox checkbox_welcome;
    LinearLayout bypass_linearlayout;
    private PrefManager prefManager;
    private Handler handler = new Handler();
    int index=0;
    int index1=0;
    int indexj=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.e("demochecking","demochecking");
        init();
        listioner();
    }

    private void init() {
        welcome_better_tv=(TextView) findViewById(R.id.welcome_better_tv);
        giving_tv=(TextView)findViewById(R.id.giving_tv);
        btnNext = (TextView) findViewById(R.id.btn_next);
        checkbox_welcome=(CheckBox)findViewById(R.id.checkbox_welcome);
        bypass_linearlayout=(LinearLayout) findViewById(R.id.bypass_linearlayout);
        /*welcome_better_tv.setTypeface(Typeface.createFromAsset(getAssets(), "commercialscriptbt.ttf"));
        giving_tv.setTypeface(Typeface.createFromAsset(getAssets(), "Chunkfive.otf"));*/
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
           /* handler.postDelayed(new Runnable() {
                @Override
                public void run() {*/
                    launchHomeScreen1();
                    finish();
          /*      }
                }, 5000);*/

        }else {
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);



            // layouts of all welcome sliders
            // add few more layouts if you want
            layouts = new int[]{
                    /*R.layout.tutorial_slide1,*/
                    R.layout.tutorial_slide2};

            // adding bottom dots
            addBottomDots(0);

            // making notification bar transparent
            changeStatusBarColor();

            myViewPagerAdapter = new MyViewPagerAdapter();
            viewPager.setAdapter(myViewPagerAdapter);
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // if (new Session(SplashActivity.this, TAG).getIsLogin())
                        //       startActivity(new Intent(SplashActivity.this, ChooseTypeActivity.class));
                        //   else
                        //       startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        if (index==0){
                            if (!prefManager.isFirstTimeLaunch()) {
                                if (index1!=1){
                                    launchHomeScreen1();
                                    finish();
                                }

                            }else {
                                if (index1!=1){
                                    launchHomeScreen();
                                }

                            }

                        }

                    }
                }, 5000);
                //launchHomeScreen();
            }
        }

        // Making notification bar transparent





    }

    private void listioner() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index1=1;
                launchHomeScreen1();
                finishAffinity();
                indexj=1;
                // checking for last page
                // if last page home screen will be launched
              /*
                if (checkbox_welcome.isChecked()){
                    index1=1;
                    launchHomeScreen1();
                }else {
                    launchHomeScreen1();
                }*/
            }
        });
        bypass_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_welcome.isChecked()){
                    prefManager.setFirstTimeLaunch(false);
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finishAffinity();
                    indexj=1;
                }
            }
        });



    }
    private void addBottomDots(int currentPage) {


        dots = new TextView[layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        index=1;
        if (indexj!=1){
            //   prefManager.setFirstTimeLaunch(false);
            if (checkbox_welcome.isChecked()){
                if (checkbox_welcome.isChecked()){
                    prefManager.setFirstTimeLaunch(false);
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }else {
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }
            }else {
                if (checkbox_welcome.isChecked()){
                    prefManager.setFirstTimeLaunch(false);
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }else {
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }
            }
        }

    }
    private void launchHomeScreen1() {
        if (indexj!=1){
            if (index1==1){
                if (checkbox_welcome.isChecked()){
                    prefManager.setFirstTimeLaunch(false);
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }else {
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }

            }else {
                if (checkbox_welcome.isChecked()){
                    prefManager.setFirstTimeLaunch(false);
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }else {
                    ChangeActivity.changeActivity(WelcomeActivity.this, BrowseActivity.class);
                    finish();
                }
            }
        }


    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'

            if (position == layouts.length - 1) {

                btnNext.setText("Got it");
            } else {

                btnNext.setText("Skip");
                // still pages are left

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
