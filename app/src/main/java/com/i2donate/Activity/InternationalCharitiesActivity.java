package com.i2donate.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.i2donate.Adapter.CategorylistAdapter;
import com.i2donate.Adapter.LoadmoreInternationlocationAdapterList;
import com.i2donate.CommonActivity.CommonBackActivity;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.Charitylist;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;
import com.i2donate.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternationalCharitiesActivity extends CommonBackActivity {
    private static String TAG = "UnitedStateActivity";
    Toolbar toolbar;
    TextView title_tv1, advance_search_text, advance_search_text1;
    ImageView close_img, filter_show_img, back_icon_img, back_icon_img1, search_icon, search_icon1, close_img1;
    static ApiInterface apiService;
    RelativeLayout relative_before_toolbar, relative_toolbar, search_relativelayout;
    static RecyclerView united_state_recyclerview;
    static ShimmerFrameLayout shimmer_view_container;
    private static LinearLayoutManager layoutManager;
    static EditText search_et, search_name_et1,search_name__searchet,search_na_et1;
    private CollapsingToolbarLayout collapsingtoolbar_layout;
    LinearLayout name_search_layout;
    LinearLayout type_linear_layout;
    static LinearLayout no_data_linear;
    LinearLayout linear_search1;
    LinearLayout linear_tool_test;
    LinearLayout type_linear_layout1, name_search_layout1;
    static LoadmoreInternationlocationAdapterList internationlocationAdapterList;
    static SessionManager session;
    static int flag=0,backflag=0;
    private AppBarLayout appbar_layout;
    Animation slideUp;
    public static List<HashMap<String, String>> charitylist = new ArrayList<HashMap<String, String>>();
    static ArrayList<Charitylist> charitylist1 = new ArrayList<>();
    static ArrayList<String> listofsubCategory = new ArrayList<>();
    static ArrayList<String> listofchilCategory = new ArrayList<>();
    static ArrayList<String> listOfdate = new ArrayList<>();
    //static ArrayList<Integer> arrayListsize = new ArrayList<Integer>();
    static String data;
    int index = 0;
    static HashMap<String, String> userDetails;
    static Context context;
    static IDonateSharedPreference iDonateSharedPreference;
    static String latlanvalue="";
    NestedScrollView nestedscrollview;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    Boolean loading = false;
    TextView title_location_tv;
    static  JSONArray jsonArray;
    static  JSONArray jsonArray1;
    static  JSONArray jsonArray2;
    static int arrayListsize=0;
    static  String page="1";
    static  int pageno=1;
    LinearLayout search_location_layout,namesearchLayout,location_search_layout,name_search_head_layout,locationtitle_search_layout,locationtitle_search_layout1;
    TextView name_title_tv;
    int name_loc=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_international_charities, TAG);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("United State");
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        toolbar.setVisibility(View.GONE);
        init();
        listioner();

    }

    private void init() {
        session = new SessionManager(getApplicationContext());
        iDonateSharedPreference = new IDonateSharedPreference();
        title_location_tv = (TextView) findViewById(R.id.title_location_tv);
        if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("typesearch")){
            title_location_tv.setText("Search By Type");
        }else  if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("typesearch")){
            title_location_tv.setText("Advanced Search");
        }else {
            title_location_tv.setText("international charites Reg...");
        }
        iDonateSharedPreference.setdailoguepage(getApplicationContext(),"0");
        slideUp = AnimationUtils.loadAnimation(this, R.anim.visiblity_animation);
        back_icon_img = (ImageView) findViewById(R.id.back_icon_name_img);
        back_icon_img1 = (ImageView) findViewById(R.id.back_icon_img1);
        collapsingtoolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar_layout);
        appbar_layout = (AppBarLayout) findViewById(R.id.appbar_layout);
        relative_before_toolbar = (RelativeLayout) findViewById(R.id.relative_before_toolbar);
        relative_toolbar = (RelativeLayout) findViewById(R.id.relative_toolbar);
        filter_show_img = (ImageView) findViewById(R.id.filter_show_img);
        linear_tool_test = (LinearLayout) findViewById(R.id.linear_tool_test);
        search_location_layout = (LinearLayout) findViewById(R.id.search_location_layout);
        location_search_layout = (LinearLayout) findViewById(R.id.location_search_layout);
        name_search_head_layout = (LinearLayout) findViewById(R.id.name_search_head_layout);
        locationtitle_search_layout = (LinearLayout) findViewById(R.id.locationtitle_search_layout);
        locationtitle_search_layout1 = (LinearLayout) findViewById(R.id.locationtitle_search_layout1);
        namesearchLayout = (LinearLayout) findViewById(R.id.namesearchLayout);
        title_tv1 = (TextView) findViewById(R.id.title_tv1);
        name_search_layout1 = (LinearLayout) findViewById(R.id.name_search_layout1);
        search_relativelayout = (RelativeLayout) findViewById(R.id.search_relativelayout);
        linear_search1 = (LinearLayout) findViewById(R.id.linear_search1);
        name_search_layout = (LinearLayout) findViewById(R.id.name_search_layout);
        united_state_recyclerview = (RecyclerView) findViewById(R.id.united_state_recyclerview);
        search_et = (EditText) findViewById(R.id.search_us_et);
        search_et.setFocusable(false);
        search_name_et1 = (EditText) findViewById(R.id.search_us_et1);
        search_name__searchet = (EditText) findViewById(R.id.search_name_et1);
        search_na_et1 = (EditText) findViewById(R.id.search_na_et1);
        //search_name__searchet.setFocusable(false);
        search_name_et1.setFocusable(false);
        search_icon = (ImageView) findViewById(R.id.search_icon);
        search_icon1 = (ImageView) findViewById(R.id.search_icon1);
        advance_search_text = (TextView) findViewById(R.id.advance_search_text);
        advance_search_text1 = (TextView) findViewById(R.id.advance_search_text1);
        close_img1 = (ImageView) findViewById(R.id.close_img1);
        no_data_linear = (LinearLayout) findViewById(R.id.no_data_linear);
        type_linear_layout = (LinearLayout) findViewById(R.id.type_linear_layout);
        type_linear_layout1 = (LinearLayout) findViewById(R.id.type_linear_layout1);
        close_img = (ImageView) findViewById(R.id.close_img);
        nestedscrollview=(NestedScrollView)findViewById(R.id.nestedscrollview);
        shimmer_view_container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();
        context=InternationalCharitiesActivity.this;
        data = getIntent().getStringExtra("data");
        listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());
        listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
        listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
        internationlocationAdapterList = new LoadmoreInternationlocationAdapterList((InternationalCharitiesActivity) context, charitylist1);
        united_state_recyclerview.setAdapter(internationlocationAdapterList);
        if (data.equalsIgnoreCase("3")){
            if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("typesearch")){
                title_location_tv.setText("Search By Type");
            }else  if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("typesearch")){
                title_location_tv.setText("Advanced Search");
            }else {
                title_location_tv.setText("international charites Reg...");
            }
        }
        if(session.isLoggedIn()){
            Log.e("login","login");
            title_tv1.setVisibility(View.VISIBLE);
        }else {
            Log.e("logout","logout");
            title_tv1.setVisibility(View.GONE);
        }

        if (isOnline()) {

            if(flag==0) {
              //  CharityAPI();
            }
        } else {
            Toast.makeText(this, "Please check Internet connection", Toast.LENGTH_SHORT).show();
        }


    }
    private void listioner() {
        nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int i, int i1, int i2, int i3) {
                int x = i1 - i3;
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((i1 >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            i1 > i3) {

                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = true;
                                loadMore();
                                //page = page+1;
                               // getUserList();
//                        Load Your Data
                            }
                        }
                    }
                }
            }
        });
        search_na_et1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                search_na_et1.setFocusableInTouchMode(true);
                Log.e(TAG, "sample = " + "test");
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                Log.e(TAG, "sample = " + "test");

                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.e(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened
                                    Log.e(TAG, "open = " + keypadHeight);
                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    Log.e(TAG, "close = " + keypadHeight);
                                    CommonBackActivity.show();
                                }
                            }
                        });
                return false;
            }
        });


        search_na_et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Log.e(TAG, "sample111 = " + "test");
                CommonBackActivity.hide();
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.e(TAG, "sample = " + "test");

                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.e(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened
                                    Log.e(TAG, "open = " + keypadHeight);
                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    Log.e(TAG, "close = " + keypadHeight);
                                    CommonBackActivity.show();
                                }
                            }
                        });
            }
        });
        search_na_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_icon1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search_na_et1.getText().toString();
                if (text.length() > 0) {
                    search_icon1.setVisibility(View.GONE);
                    close_img1.setVisibility(View.VISIBLE);
                    if (text.length() > 2) {
//                        callWebservice(text);
                        page="1";
                        backflag=1;
                        CharityAPI(page);
                    } else {
                        charitylist.clear();
                        // charitylist1.clear();
//                        unitesStateLocationAdapterList.notifyDataSetChanged();
                      /*  search_name_et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {

                                if (hasFocus) {*/
                        // Here's the key code
                        page="1";
                        backflag=0;
                        CharityAPI(page);
                              /*  }
                            }
                        });*/
                       /* arrayList.clear();
                        adapter.notifyDataSetChanged();*/
                    }
                } else {
                    search_icon1.setVisibility(View.VISIBLE);
                    close_img1.setVisibility(View.GONE);
                    charitylist.clear();
                    //  charitylist1.clear();
//                    unitesStateLocationAdapterList.notifyDataSetChanged();
                    search_name_et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            if (hasFocus) {
                                // Here's the key code
                                page="1";
                                CharityAPI(page);
                            }
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (charitylist.size() != 0) {
                   /* if (indexsearch == 0) {

                        Log.e("name", "name" + indexsearch);
                        String text = search_name_et1.getText().toString().toLowerCase(Locale.getDefault());
                        unitesStateLocationAdapterList.filter(text);
                        Log.e("name", "name");
                    }*/

                }

            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_na_et1.setText("");
                search_name__searchet.setText("");
                page="1";
                backflag=0;
                CharityAPI(page);
            }
        });
        close_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_na_et1.setText("");
                search_name__searchet.setText("");
                page="1";
                backflag=0;
                CharityAPI(page);
            }
        });
        search_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               /* ChangeActivity.changeActivity(UnitedStateActivity.this, MapsActivity.class);
                finish();*/
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.d(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened

                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    CommonBackActivity.show();
                                }
                            }
                        });
            }
        });

        search_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChangeActivity.changeActivity(InternationalCharitiesActivity.this, AutosearchActivity.class);
                flag = 1;
                if (data.equalsIgnoreCase("3")){
                    ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, PlaceSearchActivity.class, "3");
                    finish();
                }else {
                    ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, PlaceSearchActivity.class, "2");
                    finish();
                }
            }
        });

        search_name_et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                if (data.equalsIgnoreCase("3")){
                ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, PlaceSearchActivity.class, "3");
                finish();
                }else {
                    ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, PlaceSearchActivity.class, "2");
                    finish();
                }
            }
        });
        locationtitle_search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namesearchLayout.setVisibility(View.GONE);
                locationtitle_search_layout.setVisibility(View.GONE);
                locationtitle_search_layout1.setVisibility(View.GONE);
                name_search_layout.setVisibility(View.VISIBLE);
                name_search_layout1.setVisibility(View.VISIBLE);
                search_location_layout.setVisibility(View.VISIBLE);
                name_loc=0;
            }
        });
        locationtitle_search_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namesearchLayout.setVisibility(View.GONE);
                locationtitle_search_layout.setVisibility(View.GONE);
                locationtitle_search_layout1.setVisibility(View.GONE);
                name_search_head_layout.setVisibility(View.GONE);
                location_search_layout.setVisibility(View.VISIBLE);
                name_search_layout.setVisibility(View.VISIBLE);
                name_search_layout1.setVisibility(View.VISIBLE);
                search_location_layout.setVisibility(View.VISIBLE);
                name_loc=0;
            }
        });
        name_search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_search_layout.setVisibility(View.GONE);
                name_search_layout1.setVisibility(View.GONE);
                search_location_layout.setVisibility(View.GONE);
                locationtitle_search_layout1.setVisibility(View.VISIBLE);
                namesearchLayout.setVisibility(View.VISIBLE);
                locationtitle_search_layout.setVisibility(View.VISIBLE);
                name_loc=1;
                /*if(search_et.getText().length()>0 || search_name_et1.getText().length()>0) {
                    if (data.equalsIgnoreCase("1")){
                        iDonateSharedPreference.setcountrycode(getApplicationContext(),"INTsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "1");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(),"normalsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "1");
                    }

                } else {
                    if (data.equalsIgnoreCase("1")) {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "INTsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "0");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "0");
                    }
                }*/
                // finish();
            }
        });
        search_name__searchet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {

                search_name__searchet.setFocusableInTouchMode(true);
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {


                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.d(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened

                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    CommonBackActivity.show();
                                }
                            }
                        });
                return false;
            }
        });
        search_name__searchet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {


                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.e(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened

                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    CommonBackActivity.show();
                                }
                            }
                        });
            }
        });
        search_name__searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_icon.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search_name__searchet.getText().toString();
                if (text.length() > 0) {
                    //
                    search_icon.setVisibility(View.GONE);
                    close_img.setVisibility(View.VISIBLE);
                    if (text.length() > 2) {
                        page = "1";
                        backflag=1;
                        CharityAPI(page);
                    } else {
//                        unitesStateLocationAdapterList.notifyDataSetChanged();
                        //  charitylist1.clear();
                        //charitylist.clear();
                       /* search_name_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {

                                if (hasFocus) {*/
                        // Here's the key code
                        Log.e("focusing", "focusing");
                        page = "1";
                        CharityAPI(page);
                        backflag=0;
                               /* }
                            }
                        });*/


//                        arrayList.clear();
//                        adapter.notifyDataSetChanged();
                    }
                } else {

                    search_icon.setVisibility(View.VISIBLE);
                    close_img.setVisibility(View.GONE);
//                    unitesStateLocationAdapterList.notifyDataSetChanged();
                    // charitylist1.clear();
                    charitylist.clear();
                    search_name__searchet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            if (hasFocus) {
                                // Here's the key code
                                page="1";
                                CharityAPI(page);
                            }
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                /*if (indexsearch == 0) {

                    Log.e("name", "name" + indexsearch);
                    String text = search_name_et.getText().toString().toLowerCase(Locale.getDefault());
                    unitesStateLocationAdapterList.filter(text);
                    Log.e("name", "name");


                }*/

            }
        });
/*
        search_name_et1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                search_name_et1.setFocusableInTouchMode(true);
                Log.e(TAG, "sample = " + "test");
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                Log.e(TAG, "sample = " + "test");

                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.e(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened
                                    Log.e(TAG, "open = " + keypadHeight);
                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    Log.e(TAG, "close = " + keypadHeight);
                                    CommonBackActivity.show();
                                }
                            }
                        });
                return false;
            }
        });


        search_name_et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Log.e(TAG, "sample111 = " + "test");
                CommonBackActivity.hide();
                v.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {

                                Log.e(TAG, "sample = " + "test");

                                Rect r = new Rect();
                                v.getWindowVisibleDisplayFrame(r);
                                int screenHeight = v.getRootView().getHeight();

                                // r.bottom is the position above soft keypad or device button.
                                // if keypad is shown, the r.bottom is smaller than that before.
                                int keypadHeight = screenHeight - r.bottom;

                                Log.e(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                                    // keyboard is opened
                                    Log.e(TAG, "open = " + keypadHeight);
                                    CommonBackActivity.hide();
                                } else {
                                    // keyboard is closed
                                    Log.e(TAG, "close = " + keypadHeight);
                                    CommonBackActivity.show();
                                }
                            }
                        });
            }
        });
        search_name_et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_icon1.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search_name_et1.getText().toString();
                if (text.length() > 0) {
                    search_icon1.setVisibility(View.GONE);
                    close_img1.setVisibility(View.VISIBLE);
                    if (text.length() > 2) {
//                        callWebservice(text);
                        page="1";
                        backflag=1;
                        CharityAPI(page);
                    } else {
                        charitylist.clear();
                        // charitylist1.clear();
//                        unitesStateLocationAdapterList.notifyDataSetChanged();
                      *//*  search_name_et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {

                                if (hasFocus) {*//*
                        // Here's the key code
                        page="1";
                        backflag=0;
                        CharityAPI(page);
                              *//*  }
                            }
                        });*//*
                       *//* arrayList.clear();
                        adapter.notifyDataSetChanged();*//*
                    }
                } else {
                    search_icon1.setVisibility(View.VISIBLE);
                    close_img1.setVisibility(View.GONE);
                    charitylist.clear();
                    //  charitylist1.clear();
//                    unitesStateLocationAdapterList.notifyDataSetChanged();
                    search_name_et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {

                            if (hasFocus) {
                                // Here's the key code
                                page="1";
                                CharityAPI(page);
                            }
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (charitylist.size() != 0) {
                   *//* if (indexsearch == 0) {

                        Log.e("name", "name" + indexsearch);
                        String text = search_name_et1.getText().toString().toLowerCase(Locale.getDefault());
                        unitesStateLocationAdapterList.filter(text);
                        Log.e("name", "name");
                    }*//*

                }

            }
        });*/
        name_search_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_search_layout.setVisibility(View.GONE);
                name_search_layout1.setVisibility(View.GONE);
                search_location_layout.setVisibility(View.GONE);
                location_search_layout.setVisibility(View.GONE);
                namesearchLayout.setVisibility(View.VISIBLE);
                name_search_head_layout.setVisibility(View.VISIBLE);
                locationtitle_search_layout.setVisibility(View.VISIBLE);
                locationtitle_search_layout1.setVisibility(View.VISIBLE);
                name_loc=1;
                /*if(search_et.getText().length()>0 || search_name_et1.getText().length()>0) {
                    if (data.equalsIgnoreCase("1")){
                        iDonateSharedPreference.setcountrycode(getApplicationContext(),"INTsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "1");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(),"normalsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "1");
                    }
                } else {
                    if (data.equalsIgnoreCase("1")) {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "INTsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "0");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NameSearchActivity.class, "0");
                    }
                }*/
                //   finish();
            }
        });
        type_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(session.isLoggedIn()){
                if(search_et.getText().length()>0 || search_name_et1.getText().length()>0) {
                    if (data.equalsIgnoreCase("3")){
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "1");
                    }else {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(),"international");
                    iDonateSharedPreference.setcountrycode(getApplicationContext(),"INTsearch");
                    ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "1");
                    }

                } else {
                    if (data.equalsIgnoreCase("3")) {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "0");
                    }else {
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "international");
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "INTsearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "0");
                    }
                }
                // finish();
               /* }else {
                    ChangeActivity.changeActivity(InternationalCharitiesActivity.this, LoginActivity.class);
                    //  finish();
                }*/
            }
        });
        type_linear_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if(session.isLoggedIn()){
                if(search_et.getText().length()>0 || search_name_et1.getText().length()>0) {
                    if (data.equalsIgnoreCase("3")){
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "1");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "INTsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "international");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "1");
                    }
                } else {
                    if (data.equalsIgnoreCase("3")){
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "0");
                    }else {
                        iDonateSharedPreference.setcountrycode(getApplicationContext(), "INTsearch");
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "international");
                        ChangeActivity.changeActivityData(InternationalCharitiesActivity.this, NewtypesActivity.class, "0");
                    }
                }
                //  finish();
               /* }else {
                    ChangeActivity.changeActivity(InternationalCharitiesActivity.this, LoginActivity.class);
                    //  finish();
                }
*/
            }
        });
        advance_search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfdate.clear();
                listofsubCategory.clear();
                listofchilCategory.clear();
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                if (session.isLoggedIn()) {
                    if (data.equalsIgnoreCase("3")) {
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivity(InternationalCharitiesActivity.this, AdvanceCompletedActivity.class);
                    }else {
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "international");
                        ChangeActivity.changeActivity(InternationalCharitiesActivity.this, AdvanceCompletedActivity.class);
                    }
                    //  finish();
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(InternationalCharitiesActivity.this, LoginActivity.class);
                    //  finish();
                }


            }
        });

        advance_search_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfdate.clear();
                listofsubCategory.clear();
                listofchilCategory.clear();
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                if (session.isLoggedIn()) {
                    if (data.equalsIgnoreCase("3")) {
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                        ChangeActivity.changeActivity(InternationalCharitiesActivity.this, AdvanceCompletedActivity.class);
                    }else {
                        iDonateSharedPreference.setAdvancepage(getApplicationContext(), "international");
                        ChangeActivity.changeActivity(InternationalCharitiesActivity.this, AdvanceCompletedActivity.class);
                    }
                    //  finish();
                } else {
                    LoginDailogue();
                   // ChangeActivity.changeActivity(InternationalCharitiesActivity.this, LoginActivity.class);
                    //  finish();
                }


            }
        });

        filter_show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_search1.setVisibility(View.VISIBLE);
            }
        });

        back_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("test", "test");
                onBackPressed();

            }
        });
        back_icon_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                boolean isShow = true;
                int scrollRange = -1;
                if (scrollRange == -1) {

                    scrollRange = appBarLayout.getTotalScrollRange();

                }
                String text = String.valueOf(verticalOffset);
                int newNumber = Integer.parseInt(text.replace("-", ""));
              //  Log.e("demo112", "" + verticalOffset);
                if (newNumber >= 170) {

                } else {
                }
                if (newNumber >= 270) {

                    if (index == 0) {
                        index = 1;
                       // Log.e("demo112", "" + verticalOffset);
                        relative_before_toolbar.setVisibility(View.GONE);
                        relative_toolbar.setVisibility(View.VISIBLE);
                        relative_toolbar.startAnimation(slideUp);
                        linear_tool_test.setVisibility(View.GONE);
                        if (name_loc==1){
                            location_search_layout.setVisibility(View.GONE);
                            name_search_head_layout.setVisibility(View.VISIBLE);
                            if (!search_name__searchet.getText().toString().isEmpty()){
                                search_na_et1.setText(search_name__searchet.getText().toString());
                            }else {
                                search_na_et1.setText("");
                            }
                        }else {
                            name_search_head_layout.setVisibility(View.GONE);
                            location_search_layout.setVisibility(View.VISIBLE);
                            if (!search_na_et1.getText().toString().isEmpty()){
                                search_name__searchet.setText(search_na_et1.getText().toString().trim(), TextView.BufferType.EDITABLE);
                            }else {
                                search_name__searchet.setText("");
                            }
                        }

                    }

                    isShow = true;

                } else if (isShow) {
                    index = 0;
                    search_relativelayout.setVisibility(View.VISIBLE);
                    relative_before_toolbar.setVisibility(View.VISIBLE);
                    linear_search1.setVisibility(View.GONE);
                    relative_toolbar.setVisibility(View.GONE);
                    linear_tool_test.setVisibility(View.GONE);

                    isShow = false;
                }

                if (scrollRange + verticalOffset == 0) {
                  //  Log.e("ifScroll", "ifscroll");

                    isShow = true;

                } else if (isShow) {
                    if (name_loc==1){
                        if (!search_na_et1.getText().toString().isEmpty()){
                            search_name__searchet.setText(search_na_et1.getText().toString().trim(), TextView.BufferType.EDITABLE);
                        }else {
                            search_name__searchet.setText("");
                        }
                    }
                   // Log.e("elsescroll", "elsescroll");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }

            }
        });

    }
    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InternationalCharitiesActivity.this);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(InternationalCharitiesActivity.this, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public  static void like(){
        page="1";
        pageno=1;
        CharityAPI(page);
    }
    public static String getDeviceUniqueID(Context activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    private static void CharityAPI(final String page) {
        userDetails = session.getUserDetails();
        Log.e("userDetails", "" + userDetails);
        Log.e("KEY_UID", "" + userDetails.get(SessionManager.KEY_UID));
        String user_id = "";

        if (session.isLoggedIn()) {
            user_id = userDetails.get(SessionManager.KEY_UID);
        }
      /*  if (userDetails.get(SessionManager.KEY_UID).equalsIgnoreCase("null")){
            Log.e("KEY_UID", "user_idnull");
            user_id="";
        }*/
        String lat="", lng="";
        String location = iDonateSharedPreference.getLocation(context);
        Log.e("location321",""+location);
        latlanvalue=location;
        if (location.equalsIgnoreCase(null)||location.equalsIgnoreCase("")){
            location="";
        }
        if(location.length() > 0 && flag==1) {
            LatLng loc = Constants.getFromLocation(context, location);
            lat = String.valueOf(loc.latitude);
            lng = String.valueOf(loc.longitude);
        }

        String searchName = "";
        String from_income = "";
        String to_income = "";
        String searchDeductible = "";

        JsonArray category_Array = new JsonArray();
        JsonArray subCategory_Array = new JsonArray();
        JsonArray childCategory_Array = new JsonArray();
        String countrycode="";
        if(data.equalsIgnoreCase("3")) {
            countrycode="";
            if (search_na_et1.getText().length() > 0) {
                searchName = search_na_et1.getText().toString().trim();
            }else
                if (search_name__searchet.getText().length() > 0) {
                searchName = search_name__searchet.getText().toString().trim();
            }else {
                searchName = iDonateSharedPreference.getSearchName(context);
            }

            String searchRevenue = iDonateSharedPreference.getRevenue(context);
            searchDeductible = iDonateSharedPreference.getDeductible(context);
            if(searchRevenue.equalsIgnoreCase("")){
//                Do Nothing
            } else if(searchRevenue.equalsIgnoreCase("90")){
                from_income = "0";
                to_income = "90000";

            } else if(searchRevenue.equalsIgnoreCase("200")) {
                from_income = "90001";
                to_income = "200000";
            }else if(searchRevenue.equalsIgnoreCase("500")){
                from_income = "200001";
                to_income = "500000";
            }else if(searchRevenue.equalsIgnoreCase("1000")){
                from_income = "500001";
                to_income = "1000000";
            }else if(searchRevenue.equalsIgnoreCase("2000")) {
                from_income = "1000001";
                to_income = "";
            }

            for (int i = 0; i < listOfdate.size(); i++) {
                category_Array.add(listOfdate.get(i));

            }

            for (int j=0; j<listofsubCategory.size();j++){
                subCategory_Array.add(listofsubCategory.get(j));
            }

            for (int k=0; k<listofchilCategory.size();k++){
                childCategory_Array.add(listofchilCategory.get(k));
            }
        }else {
            countrycode="INT";
            if (search_na_et1.getText().length() > 0) {
                searchName = search_na_et1.getText().toString().trim();
            }else if (search_name__searchet.getText().length() > 0) {
                searchName = search_name__searchet.getText().toString().trim();
            }else {
                searchName = iDonateSharedPreference.getSearchName(context);
            }
            String searchRevenue = iDonateSharedPreference.getRevenue(context);
            searchDeductible = iDonateSharedPreference.getDeductible(context);
            if(searchRevenue.equalsIgnoreCase("")){
//                Do Nothing
            } else if(searchRevenue.equalsIgnoreCase("90")){
                from_income = "0";
                to_income = "90000";

            } else if(searchRevenue.equalsIgnoreCase("200")) {
                from_income = "90001";
                to_income = "200000";
            }else if(searchRevenue.equalsIgnoreCase("500")){
                from_income = "200001";
                to_income = "500000";
            }else if(searchRevenue.equalsIgnoreCase("1000")){
                from_income = "500001";
                to_income = "1000000";
            }else if(searchRevenue.equalsIgnoreCase("2000")) {
                from_income = "1000001";
                to_income = "";
            }

            for (int i = 0; i < listOfdate.size(); i++) {
                category_Array.add(listOfdate.get(i));

            }

            for (int j=0; j<listofsubCategory.size();j++){
                subCategory_Array.add(listofsubCategory.get(j));
            }

            for (int k=0; k<listofchilCategory.size();k++){
                childCategory_Array.add(listofchilCategory.get(k));
            }
        }
        String device_id=getDeviceUniqueID(context);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", searchName);
        jsonObject1.addProperty("latitude", lat);
        jsonObject1.addProperty("page", page);
        jsonObject1.addProperty("longitude", lng);
        jsonObject1.addProperty("address", location);
        jsonObject1.addProperty("device_id", device_id);
        jsonObject1.add("category_code", category_Array);
        jsonObject1.addProperty("deductible" , searchDeductible);
        jsonObject1.addProperty("income_from" , from_income);
        jsonObject1.addProperty("income_to" , to_income);
        jsonObject1.addProperty("country_code", countrycode);
        jsonObject1.add("sub_category_code", subCategory_Array);
        jsonObject1.add("child_category_code", childCategory_Array);
        jsonObject1.addProperty("user_id", user_id);
        Log.e("jsonObject1", "" + jsonObject1);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.Charitylist(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);
                no_data_linear.setVisibility(View.GONE);
                if (page.equalsIgnoreCase("1")){
                    charitylist.clear();
                    charitylist1.clear();
                    arrayListsize=0;
                    jsonArray1=new JSONArray();
                    jsonArray2=new JSONArray();
                }

                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String message = jsonObject.getString("message");
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            String data = jsonObject.getString("data");
                             jsonArray = new JSONArray(data);
                            int maxvalue=10;
                            if (jsonArray.length()>10){
                                maxvalue=10;
                            }else {
                                maxvalue=jsonArray.length();
                            }
                            Log.e("jsonArraylength",""+jsonArray.length());
                            arrayListsize=arrayListsize+jsonArray.length();
                            if (page.equalsIgnoreCase("1")){
                                jsonArray1=new JSONArray();
                            }else {
                                //jsonArray2=jsonArray;
                            }

                            jsonArray2=concatArray(jsonArray2,jsonArray);
                            Log.e("jsonArray2length",""+jsonArray2.length());
                            for (int i = 0; i < maxvalue; i++) {
                                HashMap<String, String> map = new HashMap<>();
                                Charitylist charitylistm = new Charitylist();
                                JSONObject object = jsonArray.getJSONObject(i);

                                map.put("id", object.getString("id"));
                                charitylistm.setId(object.getString("id"));
                                charitylistm.setName(object.getString("name"));
                                charitylistm.setStreet(object.getString("street"));
                                charitylistm.setCity(object.getString("city"));
                                charitylistm.setState(object.getString("state"));
                                charitylistm.setZip_code(object.getString("zip_code"));
                                charitylistm.setLogo(object.getString("logo"));
                                //charitylistm.setBanner(object.getString("banner"));
                               // charitylistm.setLatitude(object.getString("latitude"));
                               // charitylistm.setLongitude(object.getString("longitude"));
                               // charitylistm.setDistance(object.getString("distance"));
                                charitylistm.setLiked(object.getString("liked"));
                                charitylistm.setFollowed(object.getString("followed"));
                                charitylistm.setLike_count(object.getString("like_count"));
                                //charitylistm.setDescription(object.getString("description"));
                                charitylistm.setCountry(object.getString("country"));
                                map.put("id", object.getString("id"));
                                map.put("name", object.getString("name"));
                                map.put("street", object.getString("street"));
                                map.put("city", object.getString("city"));
                                map.put("state", object.getString("state"));
                                map.put("zip_code", object.getString("zip_code"));
                                map.put("logo", object.getString("logo"));
                                //map.put("banner", object.getString("banner"));
                                //map.put("latitude", object.getString("latitude"));
                                ///map.put("longitude", object.getString("longitude"));
                               // map.put("distance", object.getString("distance"));
                                map.put("liked", object.getString("liked"));
                                map.put("followed", object.getString("followed"));
                                map.put("like_count", object.getString("like_count"));
                              //  map.put("description", object.getString("description"));
                                map.put("country", object.getString("country"));
                                charitylist.add(map);
                                charitylist1.add(charitylistm);

                            }
                      /*      if (page.equalsIgnoreCase("1")) {
                                if (latlanvalue.equalsIgnoreCase(null) || latlanvalue.equalsIgnoreCase("")) {
                                    Collections.sort(charitylist1, new Comparator<Charitylist>() {
                                        @Override
                                        public int compare(Charitylist s1, Charitylist s2) {
                                            return s1.getName().compareTo(s2.getName());
                                        }
                                    });

                                }
                            }*/

                            if (charitylist.size() != 0) {
                                united_state_recyclerview.setVisibility(View.VISIBLE);
                                layoutManager = new LinearLayoutManager(context);
                                united_state_recyclerview.setLayoutManager(layoutManager);
                                united_state_recyclerview.setHasFixedSize(true);
                                united_state_recyclerview.setNestedScrollingEnabled(true);
                                internationlocationAdapterList.notifyDataSetChanged();
                                united_state_recyclerview.setNestedScrollingEnabled(true);
                                /*internationlocationAdapterList = new InternationlocationAdapterList((InternationalCharitiesActivity) context, charitylist1);
                                united_state_recyclerview.setAdapter(internationlocationAdapterList);*/

                            } else {
                                no_data_linear.setVisibility(View.VISIBLE);
                                united_state_recyclerview.setVisibility(View.GONE);
                            }
                        } else {
                            if (page.equalsIgnoreCase("1")) {
                                no_data_linear.setVisibility(View.VISIBLE);
                                united_state_recyclerview.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    no_data_linear.setVisibility(View.VISIBLE);
                    united_state_recyclerview.setVisibility(View.GONE);
                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);
                no_data_linear.setVisibility(View.VISIBLE);
                united_state_recyclerview.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
                // shimmer_view_container.stopShimmerAnimation(); internationlocationAdapterList.notifyDataSetChanged();
                    united_state_recyclerview.setNestedScrollingEnabled(true);
                // shimmer_view_container.setVisibility(View.GONE);
            }
        });
    }
    private static JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }
    private void loadMore() {
        charitylist1.add(null);
        internationlocationAdapterList.notifyItemInserted(charitylist1.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                charitylist1.remove(charitylist1.size() - 1);
                int scrollPosition = charitylist1.size();
                internationlocationAdapterList.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                Log.e("currentSize",""+currentSize);
                int nextLimit = currentSize + 20;
                Log.e("nextLimit",""+nextLimit);
                Log.e("charitylist1232",""+arrayListsize);
               /* while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                    Log.e("currentSize1",""+currentSize);

                }*/
               if (nextLimit>=arrayListsize){
                   pageno++;
                   page= String.valueOf(pageno);
                   CharityAPI(page);
                   loading=false;
               }
                try {
                    for (int i = currentSize-1; i < nextLimit; i++) {
                        HashMap<String, String> map = new HashMap<>();
                        Charitylist charitylistm = new Charitylist();
                        JSONObject object = jsonArray2.getJSONObject(i);
                        map.put("id", object.getString("id"));
                        charitylistm.setId(object.getString("id"));
                        charitylistm.setName(object.getString("name"));
                        charitylistm.setStreet(object.getString("street"));
                        charitylistm.setCity(object.getString("city"));
                        charitylistm.setState(object.getString("state"));
                        charitylistm.setZip_code(object.getString("zip_code"));
                        charitylistm.setLogo(object.getString("logo"));
                       // charitylistm.setBanner(object.getString("banner"));
                       // charitylistm.setLatitude(object.getString("latitude"));
                      //  charitylistm.setLongitude(object.getString("longitude"));
                       // charitylistm.setDistance(object.getString("distance"));
                        charitylistm.setLiked(object.getString("liked"));
                        charitylistm.setFollowed(object.getString("followed"));
                        charitylistm.setLike_count(object.getString("like_count"));
                       // charitylistm.setDescription(object.getString("description"));
                        charitylistm.setCountry(object.getString("country"));
                        map.put("id", object.getString("id"));
                        map.put("name", object.getString("name"));
                        map.put("street", object.getString("street"));
                        map.put("city", object.getString("city"));
                        map.put("state", object.getString("state"));
                        map.put("zip_code", object.getString("zip_code"));
                        map.put("logo", object.getString("logo"));
                      //  map.put("banner", object.getString("banner"));
                       // map.put("latitude", object.getString("latitude"));
                      //  map.put("longitude", object.getString("longitude"));
                        //map.put("distance", object.getString("distance"));
                        map.put("liked", object.getString("liked"));
                        map.put("followed", object.getString("followed"));
                        map.put("like_count", object.getString("like_count"));
                      //  map.put("description", object.getString("description"));
                        map.put("country", object.getString("country"));
                        charitylist.add(map);
                        charitylist1.add(charitylistm);
                        loading=false;

                    }
                   /* if (latlanvalue.equalsIgnoreCase(null)||latlanvalue.equalsIgnoreCase("")){
                        Collections.sort(charitylist1, new Comparator<Charitylist>() {
                            @Override
                            public int compare(Charitylist s1, Charitylist s2) {
                                return s1.getName().compareTo(s2.getName());
                            }
                        });

                    }*/
                    layoutManager = new LinearLayoutManager(context);
                    united_state_recyclerview.setLayoutManager(layoutManager);
                    united_state_recyclerview.setHasFixedSize(true);
                    united_state_recyclerview.setNestedScrollingEnabled(true);
                    internationlocationAdapterList.notifyDataSetChanged();
                    united_state_recyclerview.setNestedScrollingEnabled(true);
                    united_state_recyclerview.setNestedScrollingEnabled(true);
                    internationlocationAdapterList.notifyDataSetChanged();
                    united_state_recyclerview.setNestedScrollingEnabled(true);
                    notifyAll();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 2000);


    }

    @Override
    public void onResume() {
        super.onResume();
        page="1";
        pageno=1;
        listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());
        listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
        listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
        if(listOfdate.size() > 0){
            CharityAPI(page);
            backflag=1;
        }
       /* if(flag==1){
            Log.e(TAG, "Places Search");

        }*/
        Log.e(TAG, data);
        if(data.equalsIgnoreCase("1")){
            backflag=1;
            flag=1;
        } else   if(data.equalsIgnoreCase("3")){
            backflag=1;
            flag=1;
        } else if(data.equalsIgnoreCase("0")){
            backflag=0;
            flag = 0;
        }

        if(flag==1){
            Log.e(TAG, "Places Search");
            search_et.setText(iDonateSharedPreference.getLocation(context));
            search_name_et1.setText(iDonateSharedPreference.getLocation(context));
            CharityAPI(page);
        } else {
            if (flag==0){
                CharityAPI(page);
            }else {
                internationlocationAdapterList = new LoadmoreInternationlocationAdapterList((InternationalCharitiesActivity) context, charitylist1);
                united_state_recyclerview.setAdapter(internationlocationAdapterList);
            }

        }
        internationlocationAdapterList = new LoadmoreInternationlocationAdapterList((InternationalCharitiesActivity) context, charitylist1);
        united_state_recyclerview.setAdapter(internationlocationAdapterList);
        //shimmer_view_container.stopShimmerAnimation();
        //  mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        // shimmer_view_container.stopShimmerAnimation();
        super.onPause();
    }

    public static void nodata(int i) {
        if (i == 0) {
            no_data_linear.setVisibility(View.VISIBLE);
            united_state_recyclerview.setVisibility(View.GONE);
            Log.e("yes", "yes");
        } else if (i == 1) {
            no_data_linear.setVisibility(View.GONE);
            united_state_recyclerview.setVisibility(View.VISIBLE);
            Log.e("no", "no");

        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (backflag==1){
            if (data.equalsIgnoreCase("3")){
                iDonateSharedPreference.setLocation(getApplicationContext(),"");
                finish();
            }else {
                page = "1";
                pageno = 1;
                search_et.setText("");
                search_name_et1.setText("");
                search_name__searchet.setText("");
                search_na_et1.setText("");
                listOfdate.clear();
//        listOfdate.add("Nonprofits, Charities near you");
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setSearchName(getApplicationContext(), "");
                iDonateSharedPreference.setLocation(getApplicationContext(), "");
                iDonateSharedPreference.setRevenue(getApplicationContext(), "");
                iDonateSharedPreference.setDeductible(getApplicationContext(), "");
                CategorylistAdapter.categoty_item.clear();
                listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
                listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
                CharityAPI(page);
                backflag = 0;
            }
        }else {
            if (search_na_et1.getText().toString().trim().isEmpty()&&search_name__searchet.getText().toString().trim().isEmpty()) {
                ChangeActivity.changeActivity(InternationalCharitiesActivity.this, BrowseActivity.class);
                finishAffinity();
            }else {
                page = "1";
                pageno = 1;
                search_et.setText("");
                search_name_et1.setText("");
                search_name__searchet.setText("");
                search_na_et1.setText("");
                listOfdate.clear();
//        listOfdate.add("Nonprofits, Charities near you");
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listOfdate);
                iDonateSharedPreference.setSearchName(getApplicationContext(), "");
                iDonateSharedPreference.setLocation(getApplicationContext(), "");
                iDonateSharedPreference.setRevenue(getApplicationContext(), "");
                iDonateSharedPreference.setDeductible(getApplicationContext(), "");
                CategorylistAdapter.categoty_item.clear();
                listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
                listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
                CharityAPI(page);
                backflag = 0;
            }
        }


    }
}
