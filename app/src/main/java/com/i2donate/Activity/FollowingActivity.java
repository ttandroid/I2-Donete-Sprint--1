package com.i2donate.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.i2donate.Adapter.DonatedlistAdapterList;
import com.i2donate.Adapter.FollowingAdapterList;
import com.i2donate.CommonActivity.CommonMenuActivity;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.Charitylist;
import com.i2donate.Model.DonatedCharityList;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivity extends CommonMenuActivity {
    private String TAG = "MyspaceActivity";
    Toolbar toolbar;
    ImageView menu;
    RecyclerView following_recyclerview;
    List<Map<String, String>> listOfdate = new ArrayList<Map<String, String>>();
    private RecyclerView.LayoutManager layoutManager;
    FollowingAdapterList followingAdapterList;
    DonatedlistAdapterList donatedlistAdapterList;
    LinearLayout no_data_linear;
    TextView no_data_tv, charity_name_tv;
    ArrayList<Charitylist> follow_charitylist1=new ArrayList<>();
    IDonateSharedPreference iDonateSharedPreference;
    static ArrayList<Charitylist> like_charitylist1 = new ArrayList<>();
    static ArrayList<Charitylist> follow_charitylist12 = new ArrayList<>();
    static ArrayList<Charitylist> donate_charitylist1 = new ArrayList<>();
    static ArrayList<DonatedCharityList> DonatedCharityList1 = new ArrayList<>();
    static HashMap<String, String> userDetails;
    SessionManager session;
    static ApiInterface apiService;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_following, TAG);
        setTitle("My space");
        toolbar = findViewById(R.id.commonMenuActivityToolbar);
        init();
        listioner();
    }

    private void init() {
        session = new SessionManager(getApplicationContext());
        iDonateSharedPreference = new IDonateSharedPreference();
        iDonateSharedPreference.setdailoguepage(getApplicationContext(),"0");
        Bundle bundle = getIntent().getExtras();
        // List<HashMap<String, String>> like_charity_Arraylist =( ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("mylist");
        Log.e("follow_charitylist1de", "" + follow_charitylist1);
         type = bundle.getString("type");
        Log.e("typetype", "" + type);
        if (type.equalsIgnoreCase("donate")){
            DonatedCharityList1 = (ArrayList) getIntent().getSerializableExtra("mylist");
        }else {
            follow_charitylist1 = (ArrayList) getIntent().getSerializableExtra("mylist");
        }
        menu = (ImageView)findViewById(R.id.menu);
        following_recyclerview = (RecyclerView) findViewById(R.id.following_recyclerview);
        no_data_linear = (LinearLayout) findViewById(R.id.no_data_linear);
        no_data_tv = (TextView) findViewById(R.id.no_data_tv);
        charity_name_tv = (TextView) findViewById(R.id.charity_name_tv);
        menu.setImageResource(R.drawable.back_icon);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (type.equalsIgnoreCase("likes")) {
            charity_name_tv.setText("My likes");
        } else if (type.equalsIgnoreCase("follows")) {
            charity_name_tv.setText("my followings");
        }else if (type.equalsIgnoreCase("donate")) {
            charity_name_tv.setText("my donations");
        }

            if (follow_charitylist1.size() != 0) {

                    layoutManager = new LinearLayoutManager(this);
                    following_recyclerview.setLayoutManager(layoutManager);
                    following_recyclerview.setItemAnimator(new DefaultItemAnimator());
                    followingAdapterList = new FollowingAdapterList(FollowingActivity.this, follow_charitylist1);
                    following_recyclerview.setAdapter(followingAdapterList);


            } else {
                if (type.equalsIgnoreCase("likes")) {
                    no_data_tv.setText("You haven't like any non-profits");
                    following_recyclerview.setVisibility(View.GONE);
                    no_data_linear.setVisibility(View.VISIBLE);

                }

                else if (type.equalsIgnoreCase("follows")) {
                    no_data_tv.setText("You haven't follow any non-profits");
                    following_recyclerview.setVisibility(View.GONE);
                    no_data_linear.setVisibility(View.VISIBLE);

                }else if (type.equalsIgnoreCase("donate")) {
                        Log.e("DonatedC_size","" + DonatedCharityList1.size());
                        Log.e("getCharity_name","" + DonatedCharityList1.get(0).getCharity_name());
                        Log.e("getCharity_name","" + DonatedCharityList1.get(1).getCharity_name());
                    if (DonatedCharityList1.size()!=0) {
                        following_recyclerview.setVisibility(View.VISIBLE);
                        no_data_linear.setVisibility(View.GONE);
                        layoutManager = new LinearLayoutManager(this);
                        following_recyclerview.setLayoutManager(layoutManager);
                        following_recyclerview.setItemAnimator(new DefaultItemAnimator());
                        donatedlistAdapterList = new DonatedlistAdapterList(FollowingActivity.this, DonatedCharityList1);
                        following_recyclerview.setAdapter(donatedlistAdapterList);
                    }else {
                        no_data_tv.setText("You haven't Donate any non-profits");
                        following_recyclerview.setVisibility(View.GONE);
                        no_data_linear.setVisibility(View.VISIBLE);
                    }
                }

        }


    }
    private void FollowLikedonationAPI() {
        userDetails = session.getUserDetails();
        Log.e("userDetails", "" + userDetails);
        Log.e("KEY_UID", "" + userDetails.get(SessionManager.KEY_UID));
        String user_id = "";
        String token="";
        if (session.isLoggedIn()) {
            user_id = userDetails.get(SessionManager.KEY_UID);
            token=userDetails.get(SessionManager.KEY_token);

        }
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token);
        Log.e("jsonObject1", "" + jsonObject1);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.likes_and_followings(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                like_charitylist1.clear();
//                follow_charitylist1.clear();
                donate_charitylist1.clear();
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String message = jsonObject.getString("message");
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            String data = jsonObject.getString("data");
                            Log.e(TAG,data);
                            JSONObject jsonObject2=new JSONObject(data);
                            String like_count=jsonObject2.getString("like_count");
                            String like_charity_list=jsonObject2.getString("like_charity_list");
                            Log.e("like_charity_list",like_charity_list);
                            String following_count=jsonObject2.getString("following_count");
                            String payment_count=jsonObject2.getString("payment_count");
                            String following_charity_list=jsonObject2.getString("following_charity_list");
                            String payment_history_list=jsonObject2.getString("payment_history_list");
                            Log.e("following_charity_list",following_charity_list);
                            JSONArray jsonArray = new JSONArray(like_charity_list);
                            Log.e("jsonArray",""+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                Charitylist charitylistm = new Charitylist();
                                JSONObject object = jsonArray.getJSONObject(i);

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
                                // map.put("banner", object.getString("banner"));
                                // map.put("latitude", object.getString("latitude"));
                                // map.put("longitude", object.getString("longitude"));
                                map.put("liked", object.getString("liked"));
                                map.put("followed", object.getString("followed"));
                                map.put("like_count", object.getString("like_count"));
                                // map.put("description", object.getString("description"));
                                map.put("country", object.getString("country"));
                               // like_charity_Arraylist.add(map);
                                like_charitylist1.add(charitylistm);
                               // Log.e("like_charity_Arraylist",""+like_charity_Arraylist);
                            }

                            JSONArray jsonArrayfollow = new JSONArray(following_charity_list);
                            Log.e("jsonArray",""+jsonArrayfollow);
                            for (int i = 0; i < jsonArrayfollow.length(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                Charitylist charitylistf = new Charitylist();
                                JSONObject object = jsonArrayfollow.getJSONObject(i);

                                charitylistf.setId(object.getString("id"));
                                charitylistf.setName(object.getString("name"));
                                charitylistf.setStreet(object.getString("street"));
                                charitylistf.setCity(object.getString("city"));
                                charitylistf.setState(object.getString("state"));
                                charitylistf.setZip_code(object.getString("zip_code"));
                                charitylistf.setLogo(object.getString("logo"));
                                // charitylistf.setBanner(object.getString("banner"));
                                // charitylistf.setLatitude(object.getString("latitude"));
                                //  charitylistf.setLongitude(object.getString("longitude"));
                                charitylistf.setLiked(object.getString("liked"));
                                charitylistf.setFollowed(object.getString("followed"));
                                charitylistf.setLike_count(object.getString("like_count"));
                                //   charitylistf.setDescription(object.getString("description"));
                                charitylistf.setCountry(object.getString("country"));
                                map.put("id", object.getString("id"));
                                map.put("name", object.getString("name"));
                                map.put("street", object.getString("street"));
                                map.put("city", object.getString("city"));
                                map.put("state", object.getString("state"));
                                map.put("zip_code", object.getString("zip_code"));
                                map.put("logo", object.getString("logo"));
                                // map.put("banner", object.getString("banner"));
                                // map.put("latitude", object.getString("latitude"));
                                // map.put("longitude", object.getString("longitude"));
                                map.put("liked", object.getString("liked"));
                                map.put("followed", object.getString("followed"));
                                map.put("like_count", object.getString("like_count"));
                                //  map.put("description", object.getString("description"));
                                map.put("country", object.getString("country"));
                                //follow_charity_Arraylist.add(map);
                                follow_charitylist1.add(charitylistf);
                               // Log.e("follow_Arraylist",""+follow_charity_Arraylist);
                            }

                            JSONArray jsonArraypayment = new JSONArray(payment_history_list);
                            Log.e("jsonArray",""+jsonArraypayment);
                            for (int i = 0; i < jsonArraypayment.length(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                Charitylist charitylistf = new Charitylist();
                                JSONObject object = jsonArraypayment.getJSONObject(i);

                                charitylistf.setId(object.getString("id"));
                                charitylistf.setName(object.getString("name"));
                                charitylistf.setStreet(object.getString("street"));
                                charitylistf.setCity(object.getString("city"));
                                charitylistf.setState(object.getString("state"));
                                charitylistf.setZip_code(object.getString("zip_code"));
                                charitylistf.setLogo(object.getString("logo"));
                                //  charitylistf.setBanner(object.getString("banner"));
                                //charitylistf.setLatitude(object.getString("latitude"));
                                //charitylistf.setLongitude(object.getString("longitude"));
                                charitylistf.setLiked(object.getString("liked"));
                                charitylistf.setFollowed(object.getString("followed"));
                                charitylistf.setLike_count(object.getString("like_count"));
                                // charitylistf.setDescription(object.getString("description"));
                                charitylistf.setCountry(object.getString("country"));
                                map.put("id", object.getString("id"));
                                map.put("name", object.getString("name"));
                                map.put("street", object.getString("street"));
                                map.put("city", object.getString("city"));
                                map.put("state", object.getString("state"));
                                map.put("zip_code", object.getString("zip_code"));
                                map.put("logo", object.getString("logo"));
                                //map.put("banner", object.getString("banner"));
                                // map.put("latitude", object.getString("latitude"));
                                // map.put("longitude", object.getString("longitude"));
                                map.put("liked", object.getString("liked"));
                                map.put("followed", object.getString("followed"));
                                map.put("like_count", object.getString("like_count"));
                                //map.put("description", object.getString("description"));
                                map.put("country", object.getString("country"));
                                //payment_charity_Arraylist.add(map);
                                donate_charitylist1.add(charitylistf);
                              //  Log.e("follow_Arraylist",""+payment_charity_Arraylist);
                            }

                            if (type.equalsIgnoreCase("likes")) {
                                if (like_charitylist1.size()!=0) {
                                    followingAdapterList = new FollowingAdapterList(FollowingActivity.this, like_charitylist1);
                                    following_recyclerview.setAdapter(followingAdapterList);
                                }else {
                                    following_recyclerview.setVisibility(View.GONE);
                                    no_data_linear.setVisibility(View.VISIBLE);
                                }
                            } else if (type.equalsIgnoreCase("follows")) {
                                if (follow_charitylist1.size()!=0){
                                followingAdapterList = new FollowingAdapterList(FollowingActivity.this, follow_charitylist1);
                                following_recyclerview.setAdapter(followingAdapterList);
                                }else {
                                    following_recyclerview.setVisibility(View.GONE);
                                    no_data_linear.setVisibility(View.VISIBLE);
                                }
                            }else if (type.equalsIgnoreCase("donate")) {
                                if (donate_charitylist1.size()!=0){
                                followingAdapterList = new FollowingAdapterList(FollowingActivity.this, donate_charitylist1);
                                following_recyclerview.setAdapter(followingAdapterList);
                                }else {
                                    following_recyclerview.setVisibility(View.GONE);
                                    no_data_linear.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("ER","error1");
                        e.printStackTrace();
                    }
                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.toString());
                Log.e("ER","error2");
            }
        });
    }
    private void listioner() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        ///FollowLikedonationAPI();
    }
    @Override
    public void onBackPressed() {

       startActivity(new Intent(FollowingActivity.this, MyspaceActivity.class));
        finish();
    }
}