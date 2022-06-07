package com.i2donate.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.i2donate.Activity.LoginActivity;
import com.i2donate.Activity.MyspaceActivity;
import com.i2donate.Activity.SelectPaymentActivity;
import com.i2donate.Activity.UnitedStateActivity;
import com.i2donate.Activity.UnitedStateDetailsActivity;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.Charitylist;
import com.i2donate.Model.CustomImageView;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Gowrishankar on 14/05/19.
 */
public class LoadMoreUnitesStateLocationDetailsAdapterList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    List<HashMap<String, String>> charitylist;
    private ArrayList<Charitylist> charitylist1;
    List<Charitylist> names;
    int index = 0;
    static HashMap<String, String> userDetails;
    SessionManager session;
    ApiInterface apiService;
    Dialog d;
    String donation_amt="",merchant_charge="",processing_fee="",total_amt="";;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    IDonateSharedPreference iDonateSharedPreference;

    public LoadMoreUnitesStateLocationDetailsAdapterList(Activity applicationContext, List<HashMap<String, String>> charitylist) {
        this.mContext = applicationContext;
        this.charitylist = charitylist;
        Log.e("listOfdate", "" + charitylist);
    }

    public LoadMoreUnitesStateLocationDetailsAdapterList(UnitedStateActivity mContext, ArrayList<Charitylist> charitylist1) {
        this.mContext = mContext;
        this.charitylist1 = charitylist1;
        this.names = new ArrayList<Charitylist>();
        this.names.addAll(charitylist1);
        Log.e("charitylist", "" + charitylist1);
        session = new SessionManager(mContext);
        userDetails = session.getUserDetails();
        iDonateSharedPreference = new IDonateSharedPreference();
        String page = iDonateSharedPreference.getdailoguepage(mContext);
        Log.e("pageback1", "page"+" "+page);
        if (page.equalsIgnoreCase("1")) {
            Log.e("pageback", "page");
            dailogue();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.united_state_adapter_item, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof MyViewHolder) {
            populateItemRows((MyViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @SuppressLint("ResourceAsColor")
    private void dailogue() {
        /*d = new BottomSheetDialog(mContext, R.style.payment_dailog);
        d.setContentView(R.layout.payment_alert_dailog);
        LinearLayout payment_dailog_linear = (LinearLayout) d.findViewById(R.id.payment_dailog_linear);
        final EditText payment_et = (EditText) d.findViewById(R.id.payment_et);
        TextView cancel_tv = (TextView) d.findViewById(R.id.cancel_tv);
        Button payment_continue_btn = (Button) d.findViewById(R.id.payment_continue_btn);
        String amount = iDonateSharedPreference.getdailogueamt(mContext);
        payment_et.append(amount);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.trans_black));
        payment_dailog_linear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(payment_et.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
        });
        payment_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectPaymentActivity.class);
                Bundle bundle = new Bundle();
                final String payment = payment_et.getText().toString().trim();
                iDonateSharedPreference.setdailogueamt(mContext, payment);
                bundle.putString("payment_amt", payment);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                d.dismiss();

            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDonateSharedPreference.setdailoguepage(mContext, "0");
                d.dismiss();
            }
        });
        d.setCancelable(true);
        d.show();*/
        d = new BottomSheetDialog(mContext, R.style.payment_dailog);
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
                Drawable empty = mContext.getResources().getDrawable(id);
                d.addLevel(0, 0, empty);
                d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                return d;

            }
        }, null);
        textview_percentage.setText(spanned);
        String amount=iDonateSharedPreference.getdailogueamt(mContext);
        payment_et.append(amount);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.trans_black));
        payment_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().startsWith(".")) {
                    payment_et.setText("");
                    Toast.makeText(mContext, "Dot Not allowed", Toast.LENGTH_LONG).show();
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
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(payment_et.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                return false;
            }
        });
        textview_percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext,R.style.CustomAlertDialog);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = mContext.getLayoutInflater();

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
                final String payment = payment_et.getText().toString().trim();
                if (!payment.isEmpty()){
                    float fpay= Float.parseFloat(payment);
                    int pay= (int)fpay;
                    if (pay>=1){
                        Intent intent = new Intent(mContext, SelectPaymentActivity.class);
                        Bundle bundle = new Bundle();
                        iDonateSharedPreference.setdailogueamt(mContext, payment);
                        iDonateSharedPreference.setdailogueamt(mContext, payment);
                        bundle.putString("payment_amt", payment);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        d.dismiss();
                    }else {
                        Toast.makeText(mContext, "Please enter the amount greater than Zero", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(mContext, "Please enter the amount", Toast.LENGTH_SHORT).show();

                }


            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();
            }
        });
        d.setCancelable(true);
        d.show();
    }
    public static String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    private void followAPI(String id, final String like, String user_id, String token_id, final MyViewHolder holder) {


        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token_id);
        jsonObject1.addProperty("charity_id", id);
        jsonObject1.addProperty("status", like);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(mContext));
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
                        UnitedStateActivity.like();
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = new JSONObject(data);

                        if (like.equalsIgnoreCase("1")) {
                           /* holder.follow_linear_layout.setVisibility(View.VISIBLE);
                            holder.unfollow_linear_layout.setVisibility(View.GONE);*/
                            //  notifyDataSetChanged();
                        } else {
                            Log.e("dislike", "" + like);
                           /* holder.follow_linear_layout.setVisibility(View.GONE);
                            holder.unfollow_linear_layout.setVisibility(View.VISIBLE);*/
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

    private void likeAPI(String id, final String like, String user_id, String token_id, final MyViewHolder holder) {


        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token_id);
        jsonObject1.addProperty("charity_id", id);
        jsonObject1.addProperty("status", like);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(mContext));
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
                        UnitedStateActivity.like();
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject2 = new JSONObject(data);
                        Log.e("like_count", "" + jsonObject2.getString("like_count"));
                        holder.like_count_tv.setText(jsonObject2.getString("like_count") + " " + "Likes");
                        holder.unlike_count_tv.setText(jsonObject2.getString("like_count") + " " + "Likes");
                        if (like.equalsIgnoreCase("1")) {
                            Log.e("like", "" + like);
                            holder.unlike_linear_layout.setVisibility(View.VISIBLE);
                            holder.like_linear_layout.setVisibility(View.GONE);
                        } else {
                            Log.e("dislike", "" + like);
                            holder.unlike_linear_layout.setVisibility(View.GONE);
                            holder.like_linear_layout.setVisibility(View.VISIBLE);
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
    public int getItemCount() {
        return charitylist1 == null ? 0 : charitylist1.size();
    }
    @Override
    public int getItemViewType(int position) {
        return charitylist1.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_tv, location_name_tv, like_count_tv, unlike_count_tv, follow_count_tv, unfollow_count_tv;
        public CustomImageView logo_img;
        public ImageView like_icon_img;
        public LinearLayout united_item_layout, donate_linear_layout, like_linear_layout, unlike_linear_layout, follow_linear_layout, unfollow_linear_layout;

        public MyViewHolder(View view) {
            super(view);
            united_item_layout = (LinearLayout) view.findViewById(R.id.united_item_layout);
            donate_linear_layout = (LinearLayout) view.findViewById(R.id.donate_linear_layout);
            name_tv = (TextView) view.findViewById(R.id.name_tv);
            location_name_tv = (TextView) view.findViewById(R.id.location_name_tv);
            logo_img = (CustomImageView) view.findViewById(R.id.logo_img);
            like_count_tv = (TextView) view.findViewById(R.id.like_count_tv);
            unlike_count_tv = (TextView) view.findViewById(R.id.unlike_count_tv);
            like_icon_img = (ImageView) view.findViewById(R.id.like_icon_img);
            follow_count_tv = (TextView) view.findViewById(R.id.follow_count_tv);
            unfollow_count_tv = (TextView) view.findViewById(R.id.unfollow_count_tv);
            like_linear_layout = (LinearLayout) view.findViewById(R.id.like_linear_layout);
            unlike_linear_layout = (LinearLayout) view.findViewById(R.id.unlike_linear_layout);
            follow_linear_layout = (LinearLayout) view.findViewById(R.id.follow_linear_layout);
            unfollow_linear_layout = (LinearLayout) view.findViewById(R.id.unfollow_linear_layout);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
    private void populateItemRows(final MyViewHolder holder, final int position) {
        holder.name_tv.setText(charitylist1.get(position).getName());
        holder.location_name_tv.setText(charitylist1.get(position).getCity() + " " + charitylist1.get(position).getStreet());
        holder.like_count_tv.setText(charitylist1.get(position).getLike_count() + " " + "Likes");
        holder.unlike_count_tv.setText(charitylist1.get(position).getLike_count() + " " + "Likes");

        if (charitylist1.get(position).getLiked().equalsIgnoreCase("1")) {
            holder.unlike_linear_layout.setVisibility(View.VISIBLE);
            holder.like_linear_layout.setVisibility(View.GONE);
        } else {
            holder.unlike_linear_layout.setVisibility(View.GONE);
            holder.like_linear_layout.setVisibility(View.VISIBLE);
        }
        if (charitylist1.get(position).getFollowed().equalsIgnoreCase("1")) {
            holder.follow_count_tv.setText("Following");
            holder.unfollow_count_tv.setText("Following");
            holder.follow_linear_layout.setVisibility(View.VISIBLE);
            holder.unfollow_linear_layout.setVisibility(View.GONE);
        } else {
            holder.follow_count_tv.setText("Follow");
            holder.unfollow_count_tv.setText("Follow");
            holder.follow_linear_layout.setVisibility(View.GONE);
            holder.unfollow_linear_layout.setVisibility(View.VISIBLE);
        }
        holder.follow_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {

                    String like = "0";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    followAPI(charitylist1.get(position).getId(), like, user_id, token_id, holder);
                    holder.follow_linear_layout.setVisibility(View.GONE);
                    holder.unfollow_linear_layout.setVisibility(View.VISIBLE);
                    holder.follow_count_tv.setText("Follow");
                    holder.unfollow_count_tv.setText("Follow");

                } else {
                    LoginDailogue();

                    // mContext.finish();
                }
            }
        });
        holder.unfollow_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {

                    String like = "1";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    followAPI(charitylist1.get(position).getId(), like, user_id, token_id, holder);
                    holder.follow_linear_layout.setVisibility(View.VISIBLE);
                    holder.unfollow_linear_layout.setVisibility(View.GONE);
                    holder.follow_count_tv.setText("Following");
                    holder.unfollow_count_tv.setText("Following");
                } else {
                    LoginDailogue();
                  //  ChangeActivity.changeActivity(mContext, LoginActivity.class);
                    //  mContext.finish();
                }
            }
        });
        holder.like_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {

                    String like = "1";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("blike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    likeAPI(charitylist1.get(position).getId(), like, user_id, token_id, holder);
                } else {
                    LoginDailogue();
                }
            }
        });

        holder.unlike_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {

                    String like = "0";
                    String user_id = userDetails.get(SessionManager.KEY_UID);
                    Log.e("disblike", "" + like);
                    String token_id = userDetails.get(SessionManager.KEY_token);
                    likeAPI(charitylist1.get(position).getId(), like, user_id, token_id, holder);
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(mContext, LoginActivity.class);
                    //  mContext.finish();
                }
            }
        });
        if (!charitylist1.get(position).getLogo().isEmpty()) {
            Charitylist charitylistimg = charitylist1.get(position);
            if (!charitylist1.get(position).getLogo().equalsIgnoreCase("null")) {
                try {

                    Picasso.with(mContext).load(charitylistimg.getLogo()).into(holder.logo_img);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        holder.united_item_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UnitedStateDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", charitylist1.get(position).getName());
                bundle.putString("logo", charitylist1.get(position).getLogo());
                bundle.putString("street", charitylist1.get(position).getStreet());
                bundle.putString("city", charitylist1.get(position).getCity());
                bundle.putString("likecount", charitylist1.get(position).getLike_count());
                bundle.putString("description", "");
               // bundle.putString("description", charitylist1.get(position).getDescription());
                bundle.putString("id", charitylist1.get(position).getId());
                bundle.putString("followed", charitylist1.get(position).getFollowed());
                bundle.putString("liked", charitylist1.get(position).getLiked());
                bundle.putString("searchname", "locationsearch");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


            }
        });
        holder.donate_linear_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (session.isLoggedIn()) {
                    d = new BottomSheetDialog(mContext, R.style.payment_dailog);
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
                            Drawable empty = mContext.getResources().getDrawable(id);
                            d.addLevel(0, 0, empty);
                            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                            return d;

                        }
                    }, null);
                    textview_percentage.setText(spanned);
                  //  payment_et.append("10.00");
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.trans_black));
                    payment_et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                            if (charSequence.toString().startsWith(".")) {
                                payment_et.setText("");
                                Toast.makeText(mContext, "Dot Not allowed", Toast.LENGTH_LONG).show();
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
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(payment_et.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
                            return false;
                        }
                    });
                    textview_percentage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext,R.style.CustomAlertDialog);
// ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = mContext.getLayoutInflater();

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
                            final String payment = payment_et.getText().toString().trim();
                            if (!payment.isEmpty()){
                                float fpay= Float.parseFloat(payment);
                                int pay= (int)fpay;
                                if ( pay>=1){
                                    Intent intent = new Intent(mContext, SelectPaymentActivity.class);
                                    Bundle bundle = new Bundle();
                                    iDonateSharedPreference.setdailogueamt(mContext, payment);
                                    bundle.putString("payment_amt", payment);
                                    bundle.putString("charity_name", charitylist1.get(position).getName());
                                    bundle.putString("charity_id", charitylist1.get(position).getId());
                                    iDonateSharedPreference.setcharity_id(mContext,charitylist1.get(position).getId());
                                    iDonateSharedPreference.setcharity_name(mContext,charitylist1.get(position).getName());
                                    iDonateSharedPreference.setpaymentvalue(mContext,payment);
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent);
                                    mContext.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    d.dismiss();
                                }else {
                                    Toast.makeText(mContext, "Please enter the amount greater than Zero", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(mContext, "Please enter the amount", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
                    cancel_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            d.dismiss();
                        }
                    });
                    d.setCancelable(true);
                    d.show();
                } else {
                    LoginDailogue();
                    //ChangeActivity.changeActivity(mContext, LoginActivity.class);
                    //  mContext.finish();
                }
            }
        });
    }

    private void LoginDailogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("");
        builder.setMessage("For Advance Features Please Log-in/Register");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ChangeActivity.changeActivity(mContext, LoginActivity.class);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void filter(String charText) {
        index = 1;
        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("charText", "" + charText);
        charitylist1.clear();
        if (charText.length() == 0) {
            index = 1;
            UnitedStateActivity.nodata(1);
            Log.e("texsting", "sampletest");
            charitylist1.addAll(names);

        } else {
            for (Charitylist charitylist2 : names) {

                //String title1= Normalizer.normalize(title, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                if (charitylist2.getName().toLowerCase(Locale.getDefault()).contains(charText) || charitylist2.getCity().toLowerCase(Locale.getDefault()).contains(charText) || charitylist2.getStreet().toLowerCase(Locale.getDefault()).contains(charText)) {

                    Log.e("getName", "" + charitylist2.getName());
                    Log.e("getStreet", "" + charitylist2.getStreet());
                    charitylist1.add(charitylist2);

                    UnitedStateActivity.nodata(1);
                    notifyDataSetChanged();

                }

                if (charitylist1.size() == 0) {
                    UnitedStateActivity.nodata(0);
                    Log.e("charitylist1", "" + charitylist1.size());
                } else {
                    UnitedStateActivity.nodata(1);
                    Log.e("charitylist12", "" + charitylist1.size());
                }
            }
        }
        notifyDataSetChanged();
    }
}