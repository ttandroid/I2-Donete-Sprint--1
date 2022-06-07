package com.i2donate.Activity;

import android.annotation.SuppressLint;
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
import com.i2donate.Adapter.LoadMoreUnitesStateLocationAdapterList;
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

public class NameSearchActivity extends CommonBackActivity {

    private static String TAG = "NameSearchActivity";
    //  private final static String API_KEY = "";
    static ApiInterface apiService;
    Toolbar toolbar;
    ImageView close_img, filter_show_img, back_icon_img, back_icon_img1, search_icon, search_icon1, close_img1, type_img1;
    static RecyclerView united_state_name_recyclerview;
    static EditText search_name_et, search_name_et1;
    LinearLayout type_linear_layout, type_linear_layout1, name_loc_search_layout, name_search_layout1_default, name_search_layout, name_location_search_layout1, type_linear_layout_white, type_linear_layout1_white, name_search_layout_default, name_search_layout1;
    TextView title_tv1, advance_search_text, advance_search_text1, text_type1, advance_search_text_white, advance_search_text1_white;
    static LinearLayout no_data_linear, linear_search1, linear_tool_test;
    static List<HashMap<String, String>> charitylist = new ArrayList<HashMap<String, String>>();
    static ArrayList<Charitylist> charitylist1 = new ArrayList<>();
    private static LinearLayoutManager layoutManager;
    static LoadMoreUnitesStateLocationAdapterList unitesStateLocationAdapterList;
    static ShimmerFrameLayout shimmer_view_container;
    private CollapsingToolbarLayout collapsingtoolbar_layout;
    private AppBarLayout appbar_layout;
    Animation slideUp;
    static SessionManager session;
    static HashMap<String, String> userDetails;
    RelativeLayout relative_before_toolbar, relative_toolbar, search_relativelayout;
    int index = 0;
    int index1 = 0;
    static int flag = 0,backflag=0;
    static String data;
    int indexsearch = 0;
    static Context context;
    static IDonateSharedPreference iDonateSharedPreference;
    static ArrayList<String> listOfdate = new ArrayList<>();
    static ArrayList<String> listofsubCategory = new ArrayList<>();
    static ArrayList<String> listofchilCategory = new ArrayList<>();
    String show_type, show_advance;
    static Context instance;
    static String latlanvalue = "";
    static JSONArray jsonArray;
    Boolean loading = false;
    NestedScrollView nestedscrollview;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    static JSONArray jsonArray1;
    static JSONArray jsonArray2;
    static int arrayListsize = 0;
    static String page = "1";
    static int pageno = 1;
    TextView name_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_name_search_new, TAG);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("United State");
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        toolbar.setVisibility(View.GONE);
        instance = getApplicationContext();
        init();
        listioner();
    }

    @SuppressLint({"Range", "ResourceAsColor"})
    private void init() {
        iDonateSharedPreference = new IDonateSharedPreference();
        name_title_tv=(TextView)findViewById(R.id.name_title_tv);
        if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("typesearch")){
            name_title_tv.setText("Search By Type");
        }else  if (iDonateSharedPreference.getSelectedtype(getApplicationContext()).equalsIgnoreCase("advancesearch")){
            name_title_tv.setText("Advanced Search");
        }else {
            name_title_tv.setText("Search By Name");
        }
        iDonateSharedPreference.setdailoguepage(getApplicationContext(), "0");
        session = new SessionManager(getApplicationContext());
        slideUp = AnimationUtils.loadAnimation(this, R.anim.visiblity_animation);
        back_icon_img = (ImageView) findViewById(R.id.back_icon_name_img);
        back_icon_img1 = (ImageView) findViewById(R.id.back_icon_img1);
        collapsingtoolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar_layout);
        appbar_layout = (AppBarLayout) findViewById(R.id.appbar_layout);
        relative_before_toolbar = (RelativeLayout) findViewById(R.id.relative_before_toolbar);
        relative_toolbar = (RelativeLayout) findViewById(R.id.relative_toolbar);
        filter_show_img = (ImageView) findViewById(R.id.filter_show_img);
        linear_tool_test = (LinearLayout) findViewById(R.id.linear_tool_test);
        advance_search_text1_white = (TextView) findViewById(R.id.advance_search_text1_white);
        text_type1 = (TextView) findViewById(R.id.text_type1);
        name_loc_search_layout = (LinearLayout) findViewById(R.id.name_loc_search_layout);
        name_search_layout = (LinearLayout) findViewById(R.id.name_search_layout);
        name_location_search_layout1 = (LinearLayout) findViewById(R.id.name_location_search_layout1);
        title_tv1 = (TextView) findViewById(R.id.title_tv1);
        name_search_layout_default = (LinearLayout) findViewById(R.id.name_search_layout_default);
        name_search_layout1 = (LinearLayout) findViewById(R.id.name_search_layout1);
        search_relativelayout = (RelativeLayout) findViewById(R.id.search_relativelayout);
        linear_search1 = (LinearLayout) findViewById(R.id.linear_search1);
        type_img1 = (ImageView) findViewById(R.id.type_img1);
        name_search_layout1_default = (LinearLayout) findViewById(R.id.name_search_layout1_default);
        united_state_name_recyclerview = (RecyclerView) findViewById(R.id.united_state_name_recyclerview);
        search_name_et = (EditText) findViewById(R.id.search_name_et1);
        type_linear_layout_white = (LinearLayout) findViewById(R.id.type_linear_layout_white);
        type_linear_layout1_white = (LinearLayout) findViewById(R.id.type_linear_layout1_white);
        search_name_et.setFocusable(false);
        search_name_et1 = (EditText) findViewById(R.id.search_na_et1);
        search_name_et1.setFocusable(false);
        search_icon = (ImageView) findViewById(R.id.search_icon);
        search_icon1 = (ImageView) findViewById(R.id.search_icon1);
        advance_search_text = (TextView) findViewById(R.id.advance_search_text);
        advance_search_text_white = (TextView) findViewById(R.id.advance_search_text_white);
        advance_search_text1 = (TextView) findViewById(R.id.advance_search_text1);
        close_img1 = (ImageView) findViewById(R.id.close_img1);
        no_data_linear = (LinearLayout) findViewById(R.id.no_data_linear);
        type_linear_layout = (LinearLayout) findViewById(R.id.type_linear_layout);
        type_linear_layout1 = (LinearLayout) findViewById(R.id.type_linear_layout1);
        close_img = (ImageView) findViewById(R.id.close_img);
        nestedscrollview = (NestedScrollView) findViewById(R.id.nestedscrollview);
        shimmer_view_container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmerAnimation();
        context = NameSearchActivity.this;
        data = getIntent().getStringExtra("data");
        listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());

        listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
        listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
        layoutManager = new LinearLayoutManager(context);
        united_state_name_recyclerview.setLayoutManager(layoutManager);
        unitesStateLocationAdapterList = new LoadMoreUnitesStateLocationAdapterList((NameSearchActivity) context, charitylist1);
        united_state_name_recyclerview.setAdapter(unitesStateLocationAdapterList);
        /*show_type = iDonateSharedPreference.gettype(getApplicationContext());
        show_advance=iDonateSharedPreference.getadvance(getApplicationContext());
        if (show_type.equalsIgnoreCase("1")) {
            type_linear_layout.setVisibility(View.GONE);
            type_linear_layout1.setVisibility(View.GONE);
            type_linear_layout_white.setVisibility(View.VISIBLE);
            type_linear_layout1_white.setVisibility(View.VISIBLE);
            name_search_layout_default.setVisibility(View.VISIBLE);
            name_search_layout1_default.setVisibility(View.VISIBLE);
            name_search_layout.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.GONE);
        } else {
            type_linear_layout1.setVisibility(View.VISIBLE);
            type_linear_layout.setVisibility(View.VISIBLE);
            name_search_layout.setVisibility(View.VISIBLE);
            name_search_layout1_default.setVisibility(View.GONE);
            type_linear_layout_white.setVisibility(View.GONE);
            type_linear_layout1_white.setVisibility(View.GONE);
            name_search_layout_default.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.VISIBLE);
        }

        if (show_advance.equalsIgnoreCase("1")){
            advance_search_text.setVisibility(View.GONE);
            advance_search_text_white.setVisibility(View.VISIBLE);
            advance_search_text1.setVisibility(View.GONE);
            advance_search_text1_white.setVisibility(View.VISIBLE);
            name_search_layout_default.setVisibility(View.VISIBLE);
            name_search_layout.setVisibility(View.GONE);
        }else if (show_advance.equalsIgnoreCase("0")){
            advance_search_text.setVisibility(View.VISIBLE);
            advance_search_text_white.setVisibility(View.GONE);
            advance_search_text1.setVisibility(View.VISIBLE);
            advance_search_text1_white.setVisibility(View.GONE);
            type_linear_layout1.setVisibility(View.VISIBLE);
            type_linear_layout.setVisibility(View.VISIBLE);
            name_search_layout.setVisibility(View.VISIBLE);
            name_search_layout1_default.setVisibility(View.GONE);
            type_linear_layout_white.setVisibility(View.GONE);
            type_linear_layout1_white.setVisibility(View.GONE);
            name_search_layout_default.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.VISIBLE);
        }*/
        Log.e("listOfdate", "" + listOfdate);
        StringBuilder builder = new StringBuilder();
        for (String details : listOfdate) {
            for (int i = 0; i < listOfdate.size(); i++) {
                builder.append(details + " ," + " ");
            }
            //  builder.append(details);


          /*  if (!details.equalsIgnoreCase("")){
                builder.append(details+" ,");
            }else {
                builder.append(details);
            }*/

        }
        /*String strNew = builder.toString().substring(0, builder.toString().length()-3);
        Log.e("strNew",""+strNew);
        title_tv1.setText(strNew);
        Log.e("strNew2",""+title_tv1.getText().toString());
        if (session.isLoggedIn()) {
            title_tv1.setVisibility(View.VISIBLE);
        } else {
            title_tv1.setVisibility(View.GONE);
        }*/

        if (isOnline()) {
            CharityAPI(page);
        } else {
            Toast.makeText(this, "Please check Internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void listioner() {

        nestedscrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int i, int i1, int i2, int i3) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((i1 >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            i1 > i3) {

                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = true;
                                Log.e("loadmore", "loadmore");
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
        name_search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  listOfdate.clear();
//                listOfdate.add("Nonprofits, Charities near you");
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);*/
                flag = 1;
                String getText = "";
                if (search_name_et1.getText().length() > 0) {
                    getText = search_name_et1.getText().toString().trim();
                } else if (search_name_et.getText().length() > 0) {
                    getText = search_name_et.getText().toString().trim();
                }
                iDonateSharedPreference.setSearchName(getApplicationContext(), getText);
                if (iDonateSharedPreference.getcountrycode(getApplicationContext()).equalsIgnoreCase("INTsearch")) {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "2");
                } else if (iDonateSharedPreference.getcountrycode(getApplicationContext()).equalsIgnoreCase("USsearch")) {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "1");
                } else {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "3");
                }
                advance_search_text.setVisibility(View.VISIBLE);
                advance_search_text_white.setVisibility(View.GONE);
                advance_search_text1.setVisibility(View.VISIBLE);
                advance_search_text1_white.setVisibility(View.GONE);
                name_search_layout_default.setVisibility(View.GONE);
                name_search_layout.setVisibility(View.VISIBLE);
                type_linear_layout_white.setVisibility(View.GONE);
                type_linear_layout.setVisibility(View.VISIBLE);
                /*listOfdate.clear();
                listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());
                Log.e("listOfdatewer12",""+listOfdate.size());
                StringBuilder builder1 = new StringBuilder();
                for (String details : listOfdate) {
                    for (int i = 0; i < listOfdate.size(); i++) {

                      //  Log.e("builder1",""+builder1.toString());
                        builder1.append(details + " ," + " ");
                    }*/
                // builder1.append(details);


          /*  if (!details.equalsIgnoreCase("")){
                builder.append(details+" ,");
            }else {
                builder.append(details);
            }*/

//                }

//                String strNew = builder1.toString().substring(0, builder1.toString().length()-3);
               /* Log.e("strNew",""+strNew);
                title_tv1.setText("");
               title_tv1.setText(strNew);*/
            }
        });
        name_search_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*listOfdate.clear();
                listOfdate.add("Nonprofits, Charities near you");
                iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);*/
                flag = 1;
                Log.e("locationtype123", "" + iDonateSharedPreference.getcountrycode(getApplicationContext()));
                if (iDonateSharedPreference.getcountrycode(getApplicationContext()).equalsIgnoreCase("INTsearch")) {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "2");
                } else if (iDonateSharedPreference.getcountrycode(getApplicationContext()).equalsIgnoreCase("USsearch")) {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "1");
                } else {
                    ChangeActivity.changeActivityData(NameSearchActivity.this, PlaceSearchActivity.class, "3");
                }

                advance_search_text.setVisibility(View.VISIBLE);
                advance_search_text_white.setVisibility(View.GONE);
                advance_search_text1.setVisibility(View.VISIBLE);
                advance_search_text1_white.setVisibility(View.GONE);
                name_search_layout1_default.setVisibility(View.GONE);
                name_search_layout1.setVisibility(View.VISIBLE);
                type_linear_layout1_white.setVisibility(View.GONE);
                type_linear_layout1.setVisibility(View.VISIBLE);
                listOfdate.clear();
                listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());
                listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
                listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());

                Log.e("listOfdatewer34", "" + listOfdate);
                StringBuilder builder = new StringBuilder();
                for (String details : listOfdate) {
                    for (int i = 0; i < listOfdate.size(); i++) {
                        builder.append(details + " ," + " ");
                    }
                    // builder.append(details);


          /*  if (!details.equalsIgnoreCase("")){
                builder.append(details+" ,");
            }else {
                builder.append(details);
            }*/

                }
                /*String strNew = builder.toString().substring(0, builder.toString().length()-3);
                Log.e("strNew",""+strNew);
                // title_tv1.setText(strNew);
                Log.e("strNew2",""+title_tv1.getText().toString());
                title_tv1.setText("");
                title_tv1.setText(strNew);*/
            }
        });
        back_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "test");
                onBackPressed();

            }
        });
        back_icon_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_name_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {

                search_name_et.setFocusableInTouchMode(true);
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
        search_name_et.setOnClickListener(new View.OnClickListener() {
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
        search_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search_icon.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = search_name_et.getText().toString();
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
                    search_name_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                search_name_et.setText("");
                search_name_et1.setText("");
                page="1";
                backflag=0;
                CharityAPI(page);
            }
        });
        close_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_name_et1.setText("");
                search_name_et.setText("");
                page="1";
                backflag=0;
                CharityAPI(page);
            }
        });
        type_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if(session.isLoggedIn()){
                advance_search_text.setVisibility(View.VISIBLE);
                advance_search_text_white.setVisibility(View.GONE);
                advance_search_text1.setVisibility(View.VISIBLE);
                advance_search_text1_white.setVisibility(View.GONE);
                iDonateSharedPreference.setadvance(getApplicationContext(), "0");
                if (data.equalsIgnoreCase("1")) {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "1");
                } else {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "0");
                }
                //  finish();
               /* }else {
                    ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    //  finish();
                }*/

            }
        });
        type_linear_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(session.isLoggedIn()){
                advance_search_text.setVisibility(View.VISIBLE);
                advance_search_text_white.setVisibility(View.GONE);
                advance_search_text1.setVisibility(View.VISIBLE);
                advance_search_text1_white.setVisibility(View.GONE);
                iDonateSharedPreference.setadvance(getApplicationContext(), "0");
                if (data.equalsIgnoreCase("1")) {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "1");
                } else {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    iDonateSharedPreference.setcountrycode(getApplicationContext(), "normalsearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "0");
                }
//                ChangeActivity.changeActivity(NameSearchActivity.this, TypesActivity.class);
               /*     finish();
                }else {
                    ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    //  finish();
                }
*/
                // finish();
            }
        });
        type_linear_layout_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advance_search_text.setVisibility(View.VISIBLE);
                advance_search_text_white.setVisibility(View.GONE);
                advance_search_text1.setVisibility(View.VISIBLE);
                advance_search_text1_white.setVisibility(View.GONE);
                iDonateSharedPreference.setadvance(getApplicationContext(), "0");
//                ChangeActivity.changeActivity(NameSearchActivity.this, TypesActivity.class);
                if (data.equalsIgnoreCase("1")) {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "1");
                } else {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivityData(NameSearchActivity.this, NewtypesActivity.class, "0");
                }
            }
        });

        advance_search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()) {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivity(NameSearchActivity.this, AdvanceCompletedActivity.class);
                    finish();
                } else {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    LoginDailogue();
                   // ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    // finish();
                }
            }
        });
        advance_search_text_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivity(NameSearchActivity.this, AdvanceCompletedActivity.class);
                   // finish();
                } else {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    LoginDailogue();
                    //ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    // finish();
                }
            }
        });
        advance_search_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()) {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivity(NameSearchActivity.this, AdvanceCompletedActivity.class);
                  //  finish();
                } else {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    LoginDailogue();
                  //  ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    // finish();
                }
            }
        });
        advance_search_text1_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(), "namesearch");
                    ChangeActivity.changeActivity(NameSearchActivity.this, AdvanceCompletedActivity.class);
                   // finish();
                } else {
                    listOfdate.clear();
                    listofsubCategory.clear();
                    listofchilCategory.clear();
//                    listOfdate.add("Nonprofits, Charities near you");
                    iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
                    iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listofsubCategory);
                    iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listofchilCategory);
                    iDonateSharedPreference.settype(getApplicationContext(), "0");
                    LoginDailogue();
                    //ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    // finish();
                }
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
                Log.e("demo112", "" + verticalOffset);
                if (newNumber >= 170) {

                } else {
                }
                if (newNumber >= 270) {

                    if (index == 0) {
                        index = 1;
                        Log.e("demo112", "" + verticalOffset);
                        relative_before_toolbar.setVisibility(View.GONE);
                        relative_toolbar.setVisibility(View.VISIBLE);
                        relative_toolbar.startAnimation(slideUp);
                        linear_tool_test.setVisibility(View.GONE);

                        if (!search_name_et.getText().toString().trim().isEmpty()) {
                            //   search_name_et1.setText("");
                            index1 = 1;
                            search_name_et1.setText(search_name_et.getText().toString().trim(), TextView.BufferType.EDITABLE);
                            search_name_et.setText("");
                            search_name_et.setFocusable(false);

                        } else {
                            search_name_et1.setText(search_name_et.getText().toString().trim(), TextView.BufferType.EDITABLE);
                            //search_name_et1.setText("");
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
                    Log.e("ifScroll", "ifscroll");


                    isShow = true;

                } else if (isShow) {
                    Log.e("elsescroll", "elsescroll");//careful there should a space between double quote otherwise it wont work

                    if (!search_name_et1.getText().toString().trim().isEmpty()) {

                        search_name_et.setText(search_name_et1.getText().toString().trim(), EditText.BufferType.EDITABLE);
                        //search_name_et1.setText("");
                        search_name_et1.setFocusable(false);
                    } else {
                        search_name_et.setText(search_name_et1.getText().toString().trim(), TextView.BufferType.EDITABLE);

                    }

                    isShow = false;
                }

            }
        });

        filter_show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_search1.setVisibility(View.VISIBLE);
            }
        });
    }
    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NameSearchActivity.this);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(NameSearchActivity.this, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public static void like() {
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
        session = new SessionManager(instance);
        userDetails = session.getUserDetails();
        Log.e("userDetails", "" + userDetails);
        Log.e("KEY_UID", "" + userDetails.get(SessionManager.KEY_UID));
        String user_id = "";
        String catory = "";
        if (session.isLoggedIn()) {
            user_id = userDetails.get(SessionManager.KEY_UID);
        }
        JsonArray category_Array = new JsonArray();
        JsonArray subCategory_Array = new JsonArray();
        JsonArray childCategory_Array = new JsonArray();

        /*Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!catory.isEmpty()) {
            category_Array.add(catory);
        }

         */
        for (int i = 0; i < listOfdate.size(); i++) {
            category_Array.add(listOfdate.get(i));
            ;
        }

        for (int j = 0; j < listofsubCategory.size(); j++) {
            subCategory_Array.add(listofsubCategory.get(j));
        }

        for (int k = 0; k < listofchilCategory.size(); k++) {
            childCategory_Array.add(listofchilCategory.get(k));
        }

        String searchDeductible = iDonateSharedPreference.getDeductible(context);
        String searchRevenue = iDonateSharedPreference.getRevenue(context);
        String from_income = "";
        String to_income = "";

        if (searchRevenue.equalsIgnoreCase("")) {
            from_income = "";
            to_income = "";
        } else if (searchRevenue.equalsIgnoreCase("90")) {
            from_income = "0";
            to_income = "90000";

        } else if (searchRevenue.equalsIgnoreCase("200")) {
            from_income = "90001";
            to_income = "200000";
        } else if (searchRevenue.equalsIgnoreCase("500")) {
            from_income = "200001";
            to_income = "500000";
        } else if (searchRevenue.equalsIgnoreCase("1000")) {
            from_income = "500001";
            to_income = "1000000";
        } else if (searchRevenue.equalsIgnoreCase("2000")) {
            from_income = "1000001";
            to_income = "";
        }
        String countrycode = "";
        if (iDonateSharedPreference.getcountrycode(context).equalsIgnoreCase("INTsearch")) {
            countrycode = "INT";
        } else if (iDonateSharedPreference.getcountrycode(context).equalsIgnoreCase("USsearch")) {
            countrycode = "US";
        } else if (iDonateSharedPreference.getcountrycode(context).equalsIgnoreCase("normalsearch")) {
            countrycode = "";
        }

        String getText = "";
        if (search_name_et1.getText().length() > 0) {
            getText = search_name_et1.getText().toString().trim();
        } else if (search_name_et.getText().length() > 0) {
            getText = search_name_et.getText().toString().trim();
        }
        Log.e("Text", getText);
        String lat = "", lng = "";
        String location = iDonateSharedPreference.getLocation(context);
        latlanvalue = location;
        if (location.equalsIgnoreCase(null) || location.equalsIgnoreCase("")) {
            location = "";
        }
        Log.e("Location : ", location);
        if (!location.equalsIgnoreCase("")) {
            if (data.equalsIgnoreCase("1")) {
                LatLng loc = Constants.getFromLocation(context, location);
                lat = String.valueOf(loc.latitude);
                lng = String.valueOf(loc.longitude);
            }
        }

       /* JsonArray sub_category_Array = new JsonArray();
        // sub_category_Array.add("A20");
        JsonArray child_category_Array = new JsonArray();
        // child_category_Array.add("A23");*/
        String device_id=getDeviceUniqueID(context);
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", getText);
        jsonObject1.addProperty("latitude", lat);
        jsonObject1.addProperty("longitude", lng);
        jsonObject1.addProperty("page", page);
        jsonObject1.addProperty("address", location);
        jsonObject1.addProperty("device_id", device_id);
        jsonObject1.addProperty("deductible", searchDeductible);
        jsonObject1.addProperty("income_from", from_income);
        jsonObject1.addProperty("income_to", to_income);
        jsonObject1.addProperty("country_code", countrycode);
        jsonObject1.add("category_code", category_Array);
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
                if (page.equalsIgnoreCase("1")) {
                    charitylist.clear();
                    charitylist1.clear();
                    arrayListsize = 0;
                    jsonArray1 = new JSONArray();
                    jsonArray2 = new JSONArray();
                }
                if (response.isSuccessful()) {
                    try {
                        Log.e(TAG, response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String message = jsonObject.getString("message");
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            String data = jsonObject.getString("data");

                            jsonArray = new JSONArray(data);
                            Log.e("jsonArraylength1", "" + jsonArray.length());
                            int maxvalue = 10;
                            if (jsonArray.length() >=10) {
                                maxvalue = 10;
                            } else {
                                maxvalue = jsonArray.length();
                            }
                            Log.e("jsonArraylength", "" + jsonArray.length());
                            arrayListsize = arrayListsize + jsonArray.length();
                            if (page.equalsIgnoreCase("1")) {
                                jsonArray1 = new JSONArray();
                            } else {
                                //jsonArray2=jsonArray;
                            }

                            jsonArray2 = concatArray(jsonArray2, jsonArray);
                            Log.e("jsonArray2length", "" + jsonArray2.length());
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
                                //charitylistm.setBanner("");
                                //charitylistm.setLatitude("");
                                //charitylistm.setLongitude("");
                                //  charitylistm.setDistance(object.getString("distance"));
                                charitylistm.setLiked(object.getString("liked"));
                                charitylistm.setFollowed(object.getString("followed"));
                                charitylistm.setLike_count(object.getString("like_count"));
                                //charitylistm.setDescription("");
                                charitylistm.setCountry(object.getString("country"));
                                map.put("name", object.getString("name"));
                                map.put("street", object.getString("street"));
                                map.put("city", object.getString("city"));
                                map.put("state", object.getString("state"));
                                map.put("zip_code", object.getString("zip_code"));
                                map.put("logo", object.getString("logo"));
                                // map.put("banner", "");
                                // map.put("latitude", "");
                                // map.put("longitude", "");
                                //map.put("distance", object.getString("distance"));
                                map.put("liked", object.getString("liked"));
                                map.put("followed", object.getString("followed"));
                                map.put("like_count", object.getString("like_count"));
                                //map.put("description", "");
                                map.put("country", object.getString("country"));
                                charitylist.add(map);
                                charitylist1.add(charitylistm);

                            }
                        /*    if (page.equalsIgnoreCase("1")) {
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
                                united_state_name_recyclerview.setVisibility(View.VISIBLE);
                                layoutManager = new LinearLayoutManager(context);
                                united_state_name_recyclerview.setLayoutManager(layoutManager);
                                united_state_name_recyclerview.setHasFixedSize(true);
                                united_state_name_recyclerview.setNestedScrollingEnabled(true);
                                unitesStateLocationAdapterList.notifyDataSetChanged();
                                united_state_name_recyclerview.setNestedScrollingEnabled(true);

//                            united_state_name_recyclerview.setItemAnimator(new DefaultItemAnimator());


                            } else {
                                united_state_name_recyclerview.setVisibility(View.GONE);
                                no_data_linear.setVisibility(View.VISIBLE);

                            }


                        } else {
                            if (page.equalsIgnoreCase("1")) {
                                united_state_name_recyclerview.setVisibility(View.GONE);
                                no_data_linear.setVisibility(View.VISIBLE);
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    shimmer_view_container.stopShimmerAnimation();
                    shimmer_view_container.setVisibility(View.GONE);
                    no_data_linear.setVisibility(View.VISIBLE);
                    united_state_name_recyclerview.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.toString());
                shimmer_view_container.stopShimmerAnimation();
                shimmer_view_container.setVisibility(View.GONE);
                no_data_linear.setVisibility(View.VISIBLE);
                united_state_name_recyclerview.setVisibility(View.GONE);
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
        Log.e("loadmore", "loadmore");
        Log.e("currentSize1", "" + charitylist1.size());
        unitesStateLocationAdapterList.notifyItemInserted(charitylist1.size() - 1);
        Log.e("loadmore", "loadmore");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    charitylist1.remove(charitylist1.size() - 1);
                    int scrollPosition = charitylist1.size();
                    Log.e("loadmore", "loadmore");
                    united_state_name_recyclerview.setNestedScrollingEnabled(true);
                    unitesStateLocationAdapterList.notifyItemRemoved(scrollPosition);
                    int currentSize = scrollPosition;
                    Log.e("currentSize", "" + currentSize);
                    int nextLimit = currentSize + 20;
                    Log.e("nextLimit", "" + nextLimit);
               /* while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                    Log.e("currentSize1",""+currentSize);
                }*/
                    if (nextLimit >= arrayListsize) {
                        pageno++;
                        page = String.valueOf(pageno);
                        CharityAPI(page);
                        loading=false;
                    }
                    try {
                        for (int i = currentSize - 1; i < nextLimit; i++) {
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
                            // charitylistm.setBanner("");
                            //charitylistm.setLatitude("");
                            //charitylistm.setLongitude("");
                            //charitylistm.setDistance(object.getString("distance"));
                            charitylistm.setLiked(object.getString("liked"));
                            charitylistm.setFollowed(object.getString("followed"));
                            charitylistm.setLike_count(object.getString("like_count"));
                            charitylistm.setDescription("");
                            charitylistm.setCountry(object.getString("country"));
                            map.put("name", object.getString("name"));
                            map.put("street", object.getString("street"));
                            map.put("city", object.getString("city"));
                            map.put("state", object.getString("state"));
                            map.put("zip_code", object.getString("zip_code"));
                            map.put("logo", object.getString("logo"));
                            // map.put("banner", "");
                            // map.put("latitude", "");
                            // map.put("longitude","");
                            // map.put("distance", object.getString("distance"));
                            map.put("liked", object.getString("liked"));
                            map.put("followed", object.getString("followed"));
                            map.put("like_count", object.getString("like_count"));
                            // map.put("description", "");
                            map.put("country", object.getString("country"));

                            charitylist.add(map);
                            charitylist1.add(charitylistm);
                            loading = false;

                        }
                       /* if (latlanvalue.equalsIgnoreCase(null) || latlanvalue.equalsIgnoreCase("")) {
                            Collections.sort(charitylist1, new Comparator<Charitylist>() {
                                @Override
                                public int compare(Charitylist s1, Charitylist s2) {
                                    return s1.getName().compareTo(s2.getName());
                                }
                            });
                        }*/
                        layoutManager = new LinearLayoutManager(context);
                        united_state_name_recyclerview.setLayoutManager(layoutManager);
                        united_state_name_recyclerview.setHasFixedSize(true);
                        united_state_name_recyclerview.setNestedScrollingEnabled(true);
                        unitesStateLocationAdapterList.notifyDataSetChanged();
                        united_state_name_recyclerview.setNestedScrollingEnabled(true);
                        united_state_name_recyclerview.setNestedScrollingEnabled(true);
                        unitesStateLocationAdapterList.notifyDataSetChanged();
                        united_state_name_recyclerview.setNestedScrollingEnabled(true);
                        notifyAll();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 2000);


    }

    public static void nodata(int i) {
        if (i == 0) {
            no_data_linear.setVisibility(View.VISIBLE);
            united_state_name_recyclerview.setVisibility(View.GONE);
            Log.e("yes", "yes");
        } else if (i == 1) {
            no_data_linear.setVisibility(View.GONE);
            united_state_name_recyclerview.setVisibility(View.VISIBLE);
            Log.e("no", "no");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onresume", "onresume");
        listOfdate = iDonateSharedPreference.getselectedtypedata(getApplicationContext());
        listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
        listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
        page="1";
        pageno=1;
        if (listOfdate.size() > 0) {
            backflag=1;
            CharityAPI(page);
        }
        Log.e("listOfdate", "" + listOfdate);
        StringBuilder builder = new StringBuilder();
        for (String details : listOfdate) {
            if (!details.isEmpty()) {
                builder.append(details + " ," + " ");
            } else {
                builder.append(details);
            }

        }
        if(data.equalsIgnoreCase("1")){
            backflag=1;
            flag = 1;
            Log.e("data121",""+data);
            // titleTextView.setText("Search by location");
        } else if(data.equalsIgnoreCase("0")){
            Log.e("data123",""+data);
            backflag=0;
            flag = 0;
        }
        show_type = iDonateSharedPreference.gettype(getApplicationContext());
        /*if (show_type.equalsIgnoreCase("1")) {
            type_linear_layout.setVisibility(View.GONE);
            type_linear_layout1.setVisibility(View.GONE);
            type_linear_layout_white.setVisibility(View.VISIBLE);
            type_linear_layout1_white.setVisibility(View.VISIBLE);
            name_search_layout_default.setVisibility(View.GONE);
            name_search_layout1_default.setVisibility(View.GONE);
            name_search_layout.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.GONE);
        } else {
            type_linear_layout1.setVisibility(View.VISIBLE);
            type_linear_layout.setVisibility(View.VISIBLE);
            type_linear_layout_white.setVisibility(View.GONE);
            type_linear_layout1_white.setVisibility(View.GONE);
            name_search_layout.setVisibility(View.VISIBLE);
            name_search_layout1_default.setVisibility(View.GONE);
            name_search_layout_default.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.VISIBLE);
        }*/
        show_advance = iDonateSharedPreference.getadvance(getApplicationContext());
        /*if (show_advance.equalsIgnoreCase("1")){
            advance_search_text.setVisibility(View.GONE);
            advance_search_text_white.setVisibility(View.VISIBLE);
            advance_search_text1.setVisibility(View.GONE);
            advance_search_text1_white.setVisibility(View.VISIBLE);
            name_search_layout.setVisibility(View.GONE);
            name_search_layout1_default.setVisibility(View.VISIBLE);
            name_search_layout_default.setVisibility(View.VISIBLE);
            name_search_layout1.setVisibility(View.GONE);
        }else  if (show_advance.equalsIgnoreCase("0")){
            advance_search_text.setVisibility(View.VISIBLE);
            advance_search_text_white.setVisibility(View.GONE);
            advance_search_text1.setVisibility(View.VISIBLE);
            advance_search_text1_white.setVisibility(View.GONE);
            name_search_layout.setVisibility(View.VISIBLE);
            name_search_layout1_default.setVisibility(View.GONE);
            name_search_layout_default.setVisibility(View.GONE);
            name_search_layout1.setVisibility(View.VISIBLE);
            if (show_type.equalsIgnoreCase("1")) {
                type_linear_layout.setVisibility(View.GONE);
                type_linear_layout1.setVisibility(View.GONE);
                type_linear_layout_white.setVisibility(View.VISIBLE);
                type_linear_layout1_white.setVisibility(View.VISIBLE);
                name_search_layout_default.setVisibility(View.VISIBLE);
                name_search_layout1_default.setVisibility(View.VISIBLE);
                name_search_layout.setVisibility(View.GONE);
                name_search_layout1.setVisibility(View.GONE);
            }else {
                type_linear_layout1.setVisibility(View.VISIBLE);
                type_linear_layout.setVisibility(View.VISIBLE);
                type_linear_layout_white.setVisibility(View.GONE);
                type_linear_layout1_white.setVisibility(View.GONE);
                name_search_layout.setVisibility(View.VISIBLE);
                name_search_layout1_default.setVisibility(View.GONE);
                name_search_layout_default.setVisibility(View.GONE);
                name_search_layout1.setVisibility(View.VISIBLE);
            }
        }*/
       /* String strNew = builder.toString().substring(0, builder.toString().length()-3);
        Log.e("strNew",""+strNew);
        // title_tv1.setText(strNew);
        Log.e("strNew2",""+title_tv1.getText().toString());
        title_tv1.setText("");
        title_tv1.setText(strNew);*/
//        CharityAPI();
        unitesStateLocationAdapterList = new LoadMoreUnitesStateLocationAdapterList((NameSearchActivity) context, charitylist1);
        united_state_name_recyclerview.setAdapter(unitesStateLocationAdapterList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "onPause");
       /* unitesStateLocationAdapterList = new UnitesStateLocationAdapterList((NameSearchActivity) context, charitylist1);
        united_state_name_recyclerview.setAdapter(unitesStateLocationAdapterList);*/
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
            page="1";
            pageno=1;
            listOfdate.clear();
            search_name_et1.setText("");
            search_name_et.setText("");
//        listOfdate.add("Nonprofits, Charities near you");
            iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
            iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), listOfdate);
            iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), listOfdate);
            iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), listOfdate);
            iDonateSharedPreference.setSearchName(getApplicationContext(),"");
            iDonateSharedPreference.setLocation(getApplicationContext(),"");
            iDonateSharedPreference.setRevenue(getApplicationContext(),"");
            iDonateSharedPreference.setDeductible(getApplicationContext(), "");
            CategorylistAdapter.categoty_item.clear();
            listofsubCategory = iDonateSharedPreference.getselectedsubcategorydata(getApplicationContext());
            listofchilCategory = iDonateSharedPreference.getselectedchildcategorydata(getApplicationContext());
            CharityAPI(page);
            backflag=0;
        }else {
            ChangeActivity.changeActivity(NameSearchActivity.this, BrowseActivity.class);
            finishAffinity();
        }

    }


}
