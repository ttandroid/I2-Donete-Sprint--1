package com.i2donate.CommonActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.i2donate.Activity.BrowseActivity;
import com.i2donate.Activity.LoginActivity;
import com.i2donate.Activity.MyspaceActivity;
import com.i2donate.Interwork.ConnectivityReceiver;
import com.i2donate.Interwork.MyApplication;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.Selected;
import com.i2donate.R;
import com.i2donate.Session.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.i2donate.Model.ChangeActivity.showSnackbar;


/**
 * Created by IM028 on 8/2/16.
 */
public class CommonBackActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private static final String TAG = "MenuCommonActivity";
    private Toolbar toolbar;
    private FrameLayout frameLayout, menuActivityFrameLayout;
    private DrawerLayout drawerLayout;
    public ImageView back_icon_img, sosImg, refreshMenu;
    private CircleImageView myprofile_img;
    private TextView myaccount_name_tv, titleTextView;
    private ListView menuListView;
    TextView browse_tv,myspace_tv;
    static LinearLayout linear_browse;
    LinearLayout linear_myspace;
    static LinearLayout tabMode;
    ImageView browse_img,myspace_img;
    private View headerView;
    private Selected select;
    boolean doubleclickToClose = false;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_back);
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        checkConnection();
        session = new SessionManager(getApplicationContext());
        drawerLayout = findViewById(R.id.commonMenuActivityDrawerLayout);
        back_icon_img = findViewById(R.id.back_icon_img);
        refreshMenu = findViewById(R.id.refreshMenu);
        browse_img=(ImageView)findViewById(R.id.browse_img);
        myspace_img=(ImageView)findViewById(R.id.my_space_img);
        browse_tv=(TextView)findViewById(R.id.browse_tv);
        myspace_tv=(TextView)findViewById(R.id.my_space_tv);
        linear_browse=(LinearLayout)findViewById(R.id.linear_browse);
        linear_myspace=(LinearLayout)findViewById(R.id.linear_myspace);
        menuListView = findViewById(R.id.commonMenuActivityDrawerListView);
        menuActivityFrameLayout = findViewById(R.id.menuActivityFrameLayout);
        tabMode=(LinearLayout)findViewById(R.id.tabMode);
        //titleTextView = findViewById(R.id.commonMenuActivityTitleTextView);
//        sosImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendNotification();
//            }
//        });
        linear_browse.setOnClickListener(this);
        linear_myspace.setOnClickListener(this);

        back_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });





    }

    public static void hide(){
        tabMode.setVisibility(View.GONE);
    }
    public static void show(){
        tabMode.setVisibility(View.VISIBLE);
    }

    public void setView(int viewLayout, String activity) {
        frameLayout = (FrameLayout) findViewById(R.id.commonMenuActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        if (activity.equalsIgnoreCase("DashboardActivity")) {
            refreshMenu.setVisibility(View.VISIBLE);
        } else {
            refreshMenu.setVisibility(View.GONE);
        }
        frameLayout.addView(activityView);
    }

    /* public void setTitle(String title) {
         titleTextView.setText(title);
     }
 */

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    private void showSnack(boolean isConnected) {
        String message = null;

        if (!isConnected) {
            message = "Sorry! Not connected to internet";

        } else {
            message = "Good! Connected to Internet";

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("network", "checking");
        showSnack(isConnected);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.linear_browse:
                if (Selected.Browse != select) {
                    Log.e("Browse","Browse");
                    ChangeActivity.changeActivityback(CommonBackActivity.this, BrowseActivity.class);
                    browse_tv.setTextColor(getResources().getColor(R.color.quantum_white_text));
                    // browse_img.setImageTintMode();
                    browse_img.setColorFilter(getApplicationContext().getResources().getColor(R.color.quantum_white_text));
                    finish();
                } else{
                    Log.e("Browse1","Browse2");
                    /*ChangeActivity.changeActivity(CommonMenuActivity.this, MyspaceActivity.class);
                    myspace_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    finish();*/
                }

                break;
            case R.id.linear_myspace:
                if (Selected.Myspace != select) {
                    if (session.isLoggedIn()) {
                        Log.e("Myspace","Myspace");
                        ChangeActivity.changeActivityback(CommonBackActivity.this, MyspaceActivity.class);
                        myspace_tv.setTextColor(getResources().getColor(R.color.quantum_white_text));
                        myspace_img.setColorFilter(getApplicationContext().getResources().getColor(R.color.quantum_white_text));
                        finish();
                    } else {
                        LoginDailogue();
                       // ChangeActivity.changeActivityback(CommonBackActivity.this, LoginActivity.class);
                        //finish();
                    }

                } else{
                    Log.e("Myspace2","Myspace2");
                   /* ChangeActivity.changeActivity(CommonMenuActivity.this, BrowseActivity.class);
                    browse_tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    finish();*/
                }
                break;

        }


    }
    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonBackActivity.this);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(CommonBackActivity.this, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public void setSelected(Selected select) {
        this.select = select;
        int textColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            textColor = getResources().getColor(R.color.quantum_white_text, getTheme());
        } else {
            textColor = getResources().getColor(R.color.quantum_white_text);
        }
        switch (select) {
            case Browse:
                // home.setImageResource(R.drawable.logo);
                browse_img.setColorFilter(getApplicationContext().getResources().getColor(R.color.quantum_white_text));
                browse_tv.setTextColor(textColor);
                break;
            case Myspace:
                // store.setImageResource(R.drawable.shopping_color);
                myspace_img.setColorFilter(getApplicationContext().getResources().getColor(R.color.quantum_white_text));
                myspace_tv.setTextColor(textColor);
                break;


        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (Selected.Browse == select) {
                if (doubleclickToClose) {
                    super.onBackPressed();
                    return;
                }
                this.doubleclickToClose = true;
                showSnackbar(drawerLayout, "Please click BACK again to exit", CommonBackActivity.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleclickToClose = false;
                    }
                }, 2000);
            } else {

                ChangeActivity.changeActivityback(CommonBackActivity.this, BrowseActivity.class);
                browse_tv.setTextColor(getResources().getColor(R.color.quantum_white_text));
                finish();
                // super.onBackPressed();
            }
        }
    }
}


