package com.i2donate.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.i2donate.Adapter.CategorylistAdapter;
import com.i2donate.CommonActivity.CommonMenuActivity;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.Selected;
import com.i2donate.Notification.fcm.Constants;
import com.i2donate.Notification.fcm.MyNotificationManager;
import com.i2donate.R;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;

import java.util.ArrayList;

public class BrowseActivity extends CommonMenuActivity {
    private String TAG = "BrowseActivity";
    Toolbar toolbar;
    TextView better_tv, advance_search_tv;
    SessionManager session;
    RelativeLayout name_relative_layout, united_state_location_relative, type_relative_layout, international_layout;
    IDonateSharedPreference iDonateSharedPreference;
    static ArrayList<String> listOfdate = new ArrayList<>();
    static ArrayList<String> arraychecked_item = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_bowse, TAG);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("Browse");

        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        setSelected(Selected.Browse);
        init();
        listioner();
    }

    private void notification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        /*
         * Displaying a notification locally
         */
        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");
    }

    @SuppressLint("MissingPermission")
    private void init() {
        iDonateSharedPreference = new IDonateSharedPreference();
        iDonateSharedPreference.setdailogueamt(getApplicationContext(), "");
        session = new SessionManager(getApplicationContext());
        better_tv = (TextView) findViewById(R.id.better_tv);
        name_relative_layout = (RelativeLayout) findViewById(R.id.name_relative_layout);
        advance_search_tv = (TextView) findViewById(R.id.advance_search_tv);
        international_layout = (RelativeLayout) findViewById(R.id.international_layout);
        united_state_location_relative = (RelativeLayout) findViewById(R.id.united_state_location_relative);
        type_relative_layout = (RelativeLayout) findViewById(R.id.type_relative_layout);
        better_tv.setTypeface(Typeface.createFromAsset(getAssets(), "commercial-script-bt.ttf"));
        listOfdate.clear();
        iDonateSharedPreference.settype(getApplicationContext(), "0");
        iDonateSharedPreference.setadvance(getApplicationContext(), "0");
//        listOfdate.add("Nonprofits, Charities near you");
        iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
        iDonateSharedPreference.setselected_iem_list(getApplicationContext(), arraychecked_item);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.e("device_id",""+ getDeviceUniqueID(BrowseActivity.this));
    }

    public String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    private void listioner() {
        united_state_location_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonateSharedPreference.setLocation(getApplicationContext(),"");
                iDonateSharedPreference.setSelectedtype(getApplicationContext(),"");
                ChangeActivity.changeActivityData(BrowseActivity.this, UnitedStateActivity.class, "0");
                // finish();
            }
        });
        name_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonateSharedPreference.setLocation(getApplicationContext(),"");
                iDonateSharedPreference.setSelectedtype(getApplicationContext(),"");
                iDonateSharedPreference.setcountrycode(getApplicationContext(),"normalsearch");
                ChangeActivity.changeActivityData(BrowseActivity.this, NameSearchActivity.class, "0");
                // finish();
            }
        });
        advance_search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    iDonateSharedPreference.setAdvancepage(getApplicationContext(),"namesearch");
                    iDonateSharedPreference.setLocation(getApplicationContext(),"");
                    iDonateSharedPreference.setSelectedtype(getApplicationContext(),"advancesearch");
                    iDonateSharedPreference.setselected_iem_list(getApplicationContext(), arraychecked_item);
                    ChangeActivity.changeActivity(BrowseActivity.this, AdvanceCompletedActivity.class);
                    // finish();
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(BrowseActivity.this, LoginActivity.class);
                    //  finish();
                }

            }
        });
        type_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(session.isLoggedIn()){
                iDonateSharedPreference.setLocation(getApplicationContext(),"");
                iDonateSharedPreference.setAdvancepage(getApplicationContext(),"namesearch");
                iDonateSharedPreference.setcountrycode(getApplicationContext(),"normalsearch");
                iDonateSharedPreference.setSelectedtype(getApplicationContext(),"typesearch");
                ChangeActivity.changeActivityData(BrowseActivity.this, NewtypesActivity.class,"0");
                //finish();
              /*  }else {
                    ChangeActivity.changeActivity(BrowseActivity.this, LoginActivity.class);
                    //  finish();
                }*/

                // finish();
            }
        });
        international_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonateSharedPreference.setLocation(getApplicationContext(),"");
                iDonateSharedPreference.setSelectedtype(getApplicationContext(),"");
                ChangeActivity.changeActivityData(BrowseActivity.this, InternationalCharitiesActivity.class,"0");
                // finish();
            }
        });
    }

    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BrowseActivity.this);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(BrowseActivity.this, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        listOfdate.clear();
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
}