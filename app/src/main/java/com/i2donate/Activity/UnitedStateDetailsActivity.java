package com.i2donate.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitedStateDetailsActivity extends AppCompatActivity {
    private String TAG = "TypesActivity";
    Toolbar toolbar;
    TextView name_tv, location_tv, like_count_tv, unlike_count_tv, follow_count_tv, unfollow_count_tv, description_tv;
    ImageView back_icon_login_img, logo_img;
    static HashMap<String, String> userDetails;
    LinearLayout like_linear_layout, unlike_linear_layout, follow_linear_layout, unfollow_linear_layout;
    SessionManager session;
    String id, followed, liked, searchname;
    ApiInterface apiService;
    LinearLayout donate_linear_layout;
    Dialog d;
    IDonateSharedPreference iDonateSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_united_state_details);
        getWindow().setBackgroundDrawableResource(R.drawable.dashbord_background);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        listioner();
    }

    private void init() {
        iDonateSharedPreference = new IDonateSharedPreference();
        iDonateSharedPreference.setdailoguepage(getApplicationContext(), "0");
        session = new SessionManager(UnitedStateDetailsActivity.this);
        userDetails = session.getUserDetails();
        Bundle bundle = getIntent().getExtras();
        Log.e("bundle", "" + bundle);
        String name = bundle.getString("name");
        String logo = bundle.getString("logo");
        String street = bundle.getString("street");
        String city = bundle.getString("city");
        String likecount = bundle.getString("likecount");
        String description = bundle.getString("description");
        id = bundle.getString("id");
        liked = bundle.getString("liked");
        followed = bundle.getString("followed");
        searchname = bundle.getString("searchname");
        back_icon_login_img = (ImageView) findViewById(R.id.back_icon_login_img);
        name_tv = (TextView) findViewById(R.id.name_tv);
        logo_img = (ImageView) findViewById(R.id.logo_img);
        location_tv = (TextView) findViewById(R.id.location_tv);
        like_count_tv = (TextView) findViewById(R.id.like_count_tv);
        unlike_count_tv = (TextView) findViewById(R.id.unlike_count_tv);
        follow_count_tv = (TextView) findViewById(R.id.follow_count_tv);
        unfollow_count_tv = (TextView) findViewById(R.id.unfollow_count_tv);
        description_tv = (TextView) findViewById(R.id.description_tv);
        like_linear_layout = (LinearLayout) findViewById(R.id.like_linear_layout);
        unlike_linear_layout = (LinearLayout) findViewById(R.id.unlike_linear_layout);
        follow_linear_layout = (LinearLayout) findViewById(R.id.follow_linear_layout);
        unfollow_linear_layout = (LinearLayout) findViewById(R.id.unfollow_linear_layout);
        donate_linear_layout = (LinearLayout) findViewById(R.id.donate_linear_layout);
        name_tv.setText(name);
        location_tv.setText( city+ " " +street);
        like_count_tv.setText(likecount + " " + "Likes");
        unlike_count_tv.setText(likecount + " " + "Likes");
        if (!description.equalsIgnoreCase("null")){
            description_tv.setText(description);
        }

        if (liked.equalsIgnoreCase("1")) {
            unlike_linear_layout.setVisibility(View.VISIBLE);
            like_linear_layout.setVisibility(View.GONE);
        } else {
            unlike_linear_layout.setVisibility(View.GONE);
            like_linear_layout.setVisibility(View.VISIBLE);
        }
        if (followed.equalsIgnoreCase("1")) {
            follow_linear_layout.setVisibility(View.VISIBLE);
            unfollow_linear_layout.setVisibility(View.GONE);
            follow_count_tv.setText("Following");
            unfollow_count_tv.setText("Following");
        } else {
            follow_linear_layout.setVisibility(View.GONE);
            unfollow_linear_layout.setVisibility(View.VISIBLE);
            follow_count_tv.setText("Follow");
            unfollow_count_tv.setText("Follow");
        }
        try {
            URL url = new URL(logo);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            logo_img.setImageBitmap(bmp);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    private void listioner() {
        back_icon_login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        like_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {

                    String like = "1";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    likeAPI(id, like, user_id, token_id);
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    //finish();
                }

            }
        });
        unlike_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    String like = "0";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    likeAPI(id, like, user_id, token_id);
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    // finish();
                }

            }
        });

        follow_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()) {

                    String like = "0";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    followAPI(id, like, user_id, token_id);
                    follow_linear_layout.setVisibility(View.GONE);
                    unfollow_linear_layout.setVisibility(View.VISIBLE);
                    follow_count_tv.setText("Follow");
                    unfollow_count_tv.setText("Follow");
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    // finish();
                }

            }
        });

        unfollow_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()) {

                    String like = "1";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    followAPI(id, like, user_id, token_id);
                    follow_linear_layout.setVisibility(View.VISIBLE);
                    unfollow_linear_layout.setVisibility(View.GONE);
                    follow_count_tv.setText("Following");
                    unfollow_count_tv.setText("Following");
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    //finish();
                }

            }
        });

        donate_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    paymentdailogue();
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    //finish();
                }
            }
        });
    }
    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UnitedStateDetailsActivity.this);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    @SuppressLint("ResourceAsColor")
    private void paymentdailogue() {

        d = new BottomSheetDialog(UnitedStateDetailsActivity.this, R.style.payment_dailog);
        d.setContentView(R.layout.payment_alert_dailog);
        LinearLayout payment_dailog_linear = (LinearLayout) d.findViewById(R.id.payment_dailog_linear);
        final EditText payment_et = (EditText) d.findViewById(R.id.payment_et);
        TextView cancel_tv = (TextView) d.findViewById(R.id.cancel_tv);
        Button payment_continue_btn = (Button) d.findViewById(R.id.payment_continue_btn);
        TextView textview_percentage = (TextView) d.findViewById(R.id.textview_percentage);
        String code = "Merchant charges and processing fee will be added to whatever donation amount is entered. <img src ='addbutton.png'>";

        Spanned spanned = Html.fromHtml(code, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String arg0) {
                int id = 0;

                if(arg0.equals("addbutton.png")){
                    id = R.drawable.ic_info;
                }
                LevelListDrawable d = new LevelListDrawable();
                Drawable empty = getResources().getDrawable(id);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                return d;

            }
        }, null);
        textview_percentage.setText(spanned);
       // payment_et.append("10.00");
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.trans_black));
        payment_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().startsWith(".")) {
                    payment_et.setText("");
                    Toast.makeText(getApplicationContext(), "Dot Not allowed", Toast.LENGTH_LONG).show();
                    //disableButton(...)
                } else {

                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
                    //enableButton(...)
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        payment_dailog_linear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) UnitedStateDetailsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(payment_et.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
        });
        textview_percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UnitedStateDetailsActivity.this,R.style.CustomAlertDialog);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.percentage_detail_layout, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                String payment_amt=payment_et.getText().toString().trim();
                TextView donationamt_tv = (TextView) dialogView.findViewById(R.id.donationamt_tv);
                ImageView close_img = (ImageView) dialogView.findViewById(R.id.close_img);
                if (!payment_amt.isEmpty()){
                    donationamt_tv.setText("$ "+payment_amt);;
                }else {
                    donationamt_tv.setText("$ "+"10");;
                    payment_amt="10";
                }
                Double amount= Double.valueOf(payment_amt);
                double processing_fee = ((amount / 100.0f) * 1);
                double total_amt=processing_fee+amount;
                double percentage = ((total_amt / 100.0f) * 2.9)+0.30;

                Double payment_amt_total= amount + percentage+processing_fee;
                TextView merchantcharges_tv = (TextView) dialogView.findViewById(R.id.merchantcharges_tv);
                merchantcharges_tv.setText("$ "+String.format(" %.2f", percentage));
                TextView processing_tv = (TextView) dialogView.findViewById(R.id.processing_tv);
                processing_tv.setText("$ "+String.valueOf(processing_fee));;
                TextView totalamt_tv = (TextView) dialogView.findViewById(R.id.totalamt_tv);
                totalamt_tv.setText("$ "+String.format("%.2f", payment_amt_total));
                //editText.setText("test label");
                close_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        payment_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payment = payment_et.getText().toString().trim();
                if (!payment.isEmpty()){
                    float fpay= Float.parseFloat(payment);
                    int pay= (int)fpay;
                    if (pay>=1){
                    Intent intent = new Intent(UnitedStateDetailsActivity.this, SelectPaymentActivity.class);
                    Bundle bundle = new Bundle();

                    iDonateSharedPreference.setdailogueamt(UnitedStateDetailsActivity.this, payment);
                    bundle.putString("payment_amt", payment);
                    bundle.putString("charity_name", name_tv.getText().toString());
                    bundle.putString("charity_id", id);
                    iDonateSharedPreference.setcharity_id(getApplicationContext(),name_tv.getText().toString());
                    iDonateSharedPreference.setcharity_name(getApplicationContext(),id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    d.dismiss();
                }else {
                    Toast.makeText(UnitedStateDetailsActivity.this, "Please enter the amount greater than Zero", Toast.LENGTH_SHORT).show();

                }
                }else {
                    Toast.makeText(UnitedStateDetailsActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();

                }

            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonateSharedPreference.setdailoguepage(getApplicationContext(), "0");
                d.dismiss();
            }
        });
        d.setCancelable(true);
        d.show();

    }

    private void followAPI(String id, final String like, String user_id, String token_id) {


        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token_id);
        jsonObject1.addProperty("charity_id", id);
        jsonObject1.addProperty("status", like);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(UnitedStateDetailsActivity.this));
        Log.e("jsonObject112", "" + jsonObject1);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.charity_following(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("likeresponse", "" + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                    Log.e("jsonObject", "" + jsonObject);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        if (searchname.equalsIgnoreCase("namesearch")) {
                            NameSearchActivity.like();
                        } else if (searchname.equalsIgnoreCase("locationsearch")) {
                            UnitedStateActivity.like();
                        } else if (searchname.equalsIgnoreCase("international")) {
                            InternationalCharitiesActivity.like();
                        }

                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = new JSONObject(data);

                        if (like.equalsIgnoreCase("1")) {


                           /* follow_linear_layout.setVisibility(View.VISIBLE);
                            unfollow_linear_layout.setVisibility(View.GONE);*/
                            //  notifyDataSetChanged();
                        } else {
                            Log.e("dislike", "" + like);
                           /* follow_linear_layout.setVisibility(View.GONE);
                            unfollow_linear_layout.setVisibility(View.VISIBLE);*/
                            //notifyDataSetChanged();
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("unitedstate", t.toString());

            }
        });

    }

    private void likeAPI(String id, final String like, String user_id, String token_id) {


        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token_id);
        jsonObject1.addProperty("charity_id", id);
        jsonObject1.addProperty("status", like);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(UnitedStateDetailsActivity.this));
        Log.e("jsonObject112", "" + jsonObject1);
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.charity_like_dislike(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("likeresponse", "" + response.body());
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                    Log.e("jsonObject", "" + jsonObject);

                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        if (searchname.equalsIgnoreCase("namesearch")) {
                            NameSearchActivity.like();
                        } else if (searchname.equalsIgnoreCase("locationsearch")) {
                            UnitedStateActivity.like();
                        } else if (searchname.equalsIgnoreCase("international")) {
                            InternationalCharitiesActivity.like();

                        }

                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = new JSONObject(data);
                        Log.e("like_count", "" + jsonObject2.getString("like_count"));
                        like_count_tv.setText(jsonObject2.getString("like_count") + " " + "Likes");
                        unlike_count_tv.setText(jsonObject2.getString("like_count") + " " + "Likes");
                        if (like.equalsIgnoreCase("1")) {
                            Log.e("like", "" + like);
                            unlike_linear_layout.setVisibility(View.VISIBLE);
                            like_linear_layout.setVisibility(View.GONE);
                        } else {
                            Log.e("dislike", "" + like);
                            unlike_linear_layout.setVisibility(View.GONE);
                            like_linear_layout.setVisibility(View.VISIBLE);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Log.e("unitedstate", t.toString());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String page = iDonateSharedPreference.getdailoguepage(getApplicationContext());
        if (page.equalsIgnoreCase("1")) {
            Log.e("pageback", "page");
            paymentdailogue();
        }
        session = new SessionManager(UnitedStateDetailsActivity.this);
        userDetails = session.getUserDetails();
        Log.e("userdetails01", "" + userDetails);
    }

    @Override
    protected void onPause() {
        super.onPause();
        session = new SessionManager(UnitedStateDetailsActivity.this);
        userDetails = session.getUserDetails();
        Log.e("userdetails12", "" + userDetails);
    }

    @Override
    public void onBackPressed() {
        //  ChangeActivity.changeActivity(UnitedStateDetailsActivity.this, UnitedStateActivity.class);
        finish();
        super.onBackPressed();
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
