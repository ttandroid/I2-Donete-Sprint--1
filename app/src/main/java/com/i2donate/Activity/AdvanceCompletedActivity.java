package com.i2donate.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.i2donate.Adapter.CategorylistAdapter;
import com.i2donate.Model.Category_new;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.child_categorynew;
import com.i2donate.Model.subcategorynew;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdvanceCompletedActivity extends AppCompatActivity {
    private static final String TAG = AdvanceCompletedActivity.class.getSimpleName();
    RecyclerView recyclerview_advancesearch;
    ApiInterface apiService;
    static SessionManager session;
    AlertDialog.Builder builder;
    private RecyclerView.LayoutManager layoutManager;
    static HashMap<String, String> userDetails;
    static ArrayList<Category_new> category_newArrayList = new ArrayList<>();
    static ArrayList<subcategorynew> sub_category_newArrayList = new ArrayList<>();
    static ArrayList<child_categorynew> child_category_newArrayList = new ArrayList<>();
    CategorylistAdapter categorylistAdapter;
    TextView exempt_tv_deselect, exempt_tv_select, non_exempt_deselect_tv, non_exempt_select_tv;
//    SeekBar seekbar;
    LinearLayout bottom_layout;
    ImageView back_icon_login_img;
    String select = "";
    String select1 = "";
    String seekbarvalue;
    static ArrayList<String> listOfdate = new ArrayList<>();
    RelativeLayout search_types_sub_types, annualRevenue;
    int flag=1;

    Button reset_button,apply_button;
    IDonateSharedPreference iDonateSharedPreference;
     ArrayList<String> arraychecked_item=new ArrayList<>();
     ArrayList<String> categorychecked_item = new ArrayList<>();
     String response_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_completed);
        init();
        listioner();

    }

    private void listioner() {
        exempt_tv_deselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "a";
                iDonateSharedPreference.setDeductible(getApplicationContext(), "1");
                non_exempt_select_tv.setVisibility(View.GONE);
                non_exempt_deselect_tv.setVisibility(View.VISIBLE);
                exempt_tv_deselect.setVisibility(View.GONE);
                exempt_tv_select.setVisibility(View.VISIBLE);

                if (select.equalsIgnoreCase("a")) {
                    bottom_layout.setVisibility(View.VISIBLE);
                }else {
                    bottom_layout.setVisibility(View.GONE);
                    for (String details : arraychecked_item) {
                        Log.e("details",""+details);
                        if (details.equalsIgnoreCase("YES")){
                            bottom_layout.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });
        exempt_tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "b";
                iDonateSharedPreference.setDeductible(getApplicationContext(), "");
                exempt_tv_select.setVisibility(View.GONE);
                exempt_tv_deselect.setVisibility(View.VISIBLE);
                if (select.equalsIgnoreCase("a")) {
                    bottom_layout.setVisibility(View.VISIBLE);
                }else {
                    bottom_layout.setVisibility(View.GONE);
                    for (String details : arraychecked_item) {
                        Log.e("details",""+details);
                        if (details.equalsIgnoreCase("YES")){
                            bottom_layout.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });
        non_exempt_deselect_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select1 = "a";
                iDonateSharedPreference.setDeductible(getApplicationContext(), "2");
                exempt_tv_select.setVisibility(View.GONE);
                exempt_tv_deselect.setVisibility(View.VISIBLE);
                non_exempt_deselect_tv.setVisibility(View.GONE);
                non_exempt_select_tv.setVisibility(View.VISIBLE);
                if (select1.equalsIgnoreCase("a")) {
                    bottom_layout.setVisibility(View.VISIBLE);
                }else {
                    bottom_layout.setVisibility(View.GONE);
                    for (String details : arraychecked_item) {
                        Log.e("details",""+details);
                        if (details.equalsIgnoreCase("YES")){
                            bottom_layout.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });
        non_exempt_select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select1 = "b";
                iDonateSharedPreference.setDeductible(getApplicationContext(), "");
                non_exempt_select_tv.setVisibility(View.GONE);
                non_exempt_deselect_tv.setVisibility(View.VISIBLE);
                if (select1.equalsIgnoreCase("a")) {
                    bottom_layout.setVisibility(View.VISIBLE);
                }else {
                    bottom_layout.setVisibility(View.GONE);
                    for (String details : arraychecked_item) {
                        Log.e("details",""+details);
                        if (details.equalsIgnoreCase("YES")){
                            bottom_layout.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });

        annualRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                ChangeActivity.changeActivity(AdvanceCompletedActivity.this, AnnualRevenueActivity.class);
            }
        });

        search_types_sub_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity.changeActivity(AdvanceCompletedActivity.this, TitleSubTitleActivity.class);

            }
        });
        back_icon_login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogue_forgot();
            }
        });

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraychecked_item.clear();

                iDonateSharedPreference.setselected_iem_list(getApplicationContext(),arraychecked_item);
                Log.e("search_type",""+iDonateSharedPreference.getAdvancepage(getApplicationContext()));
                //Intent intent = new Intent(AdvanceCompletedActivity.this, NameSearchActivity.class);
                if (iDonateSharedPreference.getAdvancepage(getApplicationContext()).equalsIgnoreCase("unitedstate")){
                    Intent intent = new Intent(AdvanceCompletedActivity.this, UnitedStateActivity.class);
                    intent.putExtra("data","1");
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                iDonateSharedPreference.setadvance(getApplicationContext(),"1");
                    startActivity(intent);
                    TitleSubTitleActivity.listOfcategory.clear();
                    iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), TitleSubTitleActivity.listOfcategory);
                    Log.e("listOfcategory_item1",""+arraychecked_item);
                    finishAffinity();
                }else if (iDonateSharedPreference.getAdvancepage(getApplicationContext()).equalsIgnoreCase("international")){
                    Intent intent = new Intent(AdvanceCompletedActivity.this, InternationalCharitiesActivity.class);
                    intent.putExtra("data","1");
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                iDonateSharedPreference.setadvance(getApplicationContext(),"1");
                    startActivity(intent);
                    TitleSubTitleActivity.listOfcategory.clear();
                    iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), TitleSubTitleActivity.listOfcategory);
                    Log.e("listOfcategory_item1",""+arraychecked_item);
                    finishAffinity();
                }else {
                    Intent intent = new Intent(AdvanceCompletedActivity.this, NameSearchActivity.class);
                    intent.putExtra("data","1");
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                iDonateSharedPreference.setadvance(getApplicationContext(),"1");
                    startActivity(intent);
                    TitleSubTitleActivity.listOfcategory.clear();
                    iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), TitleSubTitleActivity.listOfcategory);
                    Log.e("listOfcategory_item1",""+arraychecked_item);
                    finishAffinity();
                }

            }
        });
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraychecked_item.clear();
                CategorylistAdapter.categoty_item.clear();
//                SubCategoryActivity.listofsubcategory.clear();
//                SubCategoryActivity.listofchildcategory.clear();
//                SubCategoryActivity.listofchildcodecategory.clear();
                iDonateSharedPreference.setselected_iem_list(getApplicationContext(),arraychecked_item);
//                iDonateSharedPreference.setselectedsubcategorydata(getApplicationContext(), SubCategoryActivity.listofsubcategory);
//                iDonateSharedPreference.setselectedchildcategorydata(getApplicationContext(), SubCategoryActivity.listofchildcategory);
                bottom_layout.setVisibility(View.GONE);
                exempt_tv_select.setVisibility(View.GONE);
                non_exempt_select_tv.setVisibility(View.GONE);
                exempt_tv_deselect.setVisibility(View.VISIBLE);
                non_exempt_deselect_tv.setVisibility(View.VISIBLE);
                TitleSubTitleActivity.listOfcategory.clear();
                iDonateSharedPreference.setselectedcategorydata(getApplicationContext(), TitleSubTitleActivity.listOfcategory);
                iDonateSharedPreference.setRevenue(getApplicationContext(),"");
                iDonateSharedPreference.setDeductible(getApplicationContext(), "");
//                seekbar.setProgress(0);
//                seletedApi(response_data);
            }
        });

//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.e("progress21",""+progress);
//                if (progress==0){
//                    seekbarvalue= String.valueOf(progress);
//                }else if (progress==1){
//                    seekbarvalue= String.valueOf(progress);
//                }else if (progress==2){
//                    seekbarvalue= String.valueOf(progress);
//                }else if (progress==3){
//                    seekbarvalue= String.valueOf(progress);
//                }else if (progress==4){
//                    seekbarvalue= String.valueOf(progress);
//                }else if (progress==5){
//                    seekbarvalue= String.valueOf(progress);
//                }
//                if (!seekbarvalue.equalsIgnoreCase("0")){
//                    bottom_layout.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    private void seletedApi(String response_data) {
        category_newArrayList.clear();
        child_category_newArrayList.clear();
        sub_category_newArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response_data);
            Log.e("jsonObject", "" + jsonObject);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            String data = jsonObject.getString("data");
            if (status.equalsIgnoreCase("1")) {

                JSONArray jsonArray = new JSONArray(data);
                Log.e("1232", "" + jsonArray.length());
                if (!data.equalsIgnoreCase("")) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        sub_category_newArrayList.clear();
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category_new category_new = new Category_new();
                        category_new.setCategory_id(object.getString("category_id"));
                        category_new.setCategory_code(object.getString("category_code"));
                        category_new.setCategory_name(object.getString("category_name"));
//                        category_new.setSubcategory(object.getJSONArray("subcategory"));
//                        category_new.setSlected("false");
                        JSONArray subjsonarray = object.getJSONArray("subcategory");
                        Log.e("subcategory", "" + subjsonarray);
                        Log.e("subjsonarraylength", "" + subjsonarray.length());
                        category_newArrayList.add(category_new);
                        Log.e("category_newArrayList", "" + category_newArrayList);
                        Log.e("catetsize", "" + category_newArrayList.size());
                        categorylistAdapter = new CategorylistAdapter(AdvanceCompletedActivity.this, category_newArrayList);
                        recyclerview_advancesearch.setAdapter(categorylistAdapter);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        iDonateSharedPreference = new IDonateSharedPreference();
        iDonateSharedPreference.setselected_iem_list(getApplicationContext(),arraychecked_item);
        session = new SessionManager(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        recyclerview_advancesearch = (RecyclerView) findViewById(R.id.recyclerview_advancesearch);
        exempt_tv_deselect = (TextView) findViewById(R.id.exempt_tv_deselect);
        exempt_tv_select = (TextView) findViewById(R.id.exempt_tv_select);
        annualRevenue = (RelativeLayout) findViewById(R.id.revenueRelativeLayout);
        non_exempt_deselect_tv = (TextView) findViewById(R.id.non_exempt_deselect_tv);
        non_exempt_select_tv = (TextView) findViewById(R.id.non_exempt_select_tv);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
//        seekbar = (SeekBar) findViewById(R.id.seekbar);
        back_icon_login_img = (ImageView) findViewById(R.id.back_icon_login_img);
        reset_button=(Button)findViewById(R.id.reset_button);
        apply_button=(Button)findViewById(R.id.apply_button);
        search_types_sub_types=(RelativeLayout)findViewById(R.id.search_types_sub_types);
//        seekbar.setMax(4);
        layoutManager = new LinearLayoutManager(this);
        recyclerview_advancesearch.setLayoutManager(layoutManager);
        recyclerview_advancesearch.setItemAnimator(new DefaultItemAnimator());
        Log.e("select_item",""+iDonateSharedPreference.getselected_iem_list(getApplicationContext()));
        arraychecked_item=iDonateSharedPreference.getselected_iem_list(getApplicationContext());
        for (String details : arraychecked_item) {
            Log.e("details",""+details);
            if (details.equalsIgnoreCase("YES")){
                bottom_layout.setVisibility(View.VISIBLE);
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        String deductible = iDonateSharedPreference.getDeductible(getApplicationContext());
        if(deductible.equalsIgnoreCase("")){
            //Do Nothing
        } else if(deductible.equalsIgnoreCase("1")){
            exempt_tv_select.setVisibility(View.VISIBLE);
            exempt_tv_deselect.setVisibility(View.GONE);
            non_exempt_deselect_tv.setVisibility(View.VISIBLE);
            non_exempt_select_tv.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.VISIBLE);
        } else if(deductible.equalsIgnoreCase("2")){
            exempt_tv_select.setVisibility(View.GONE);
            exempt_tv_deselect.setVisibility(View.VISIBLE);
            non_exempt_deselect_tv.setVisibility(View.GONE);
            non_exempt_select_tv.setVisibility(View.VISIBLE);
            bottom_layout.setVisibility(View.VISIBLE);
        }

        listOfdate = iDonateSharedPreference.getselectedcategorydata(getApplicationContext());
        if(listOfdate.size() > 0){
            listOfdate.clear();
            iDonateSharedPreference.setselectedtypedata(getApplicationContext(), CategorylistAdapter.categoty_item);
        } else {
            listOfdate.clear();
            iDonateSharedPreference.setselectedtypedata(getApplicationContext(), listOfdate);
        }
        iDonateSharedPreference.setSearchName(getApplicationContext(),"");
        arraychecked_item=iDonateSharedPreference.getselected_iem_list(getApplicationContext());
        categorychecked_item = iDonateSharedPreference.getselectedcategorydata(getApplicationContext());
        if(categorychecked_item.size()>0){
            Log.e(TAG, "Non-Empty");
            bottom_layout.setVisibility(View.VISIBLE);
        }
        for (String details : arraychecked_item) {
            Log.e("details",""+details);
            if (details.equalsIgnoreCase("YES")){
                bottom_layout.setVisibility(View.VISIBLE);
            }

        }
        if(iDonateSharedPreference.getRevenue(getApplicationContext()).equalsIgnoreCase("")){
            //Do Nothing

        } else bottom_layout.setVisibility(View.VISIBLE);
        /*if(flag == 0){
            bottom_layout.setVisibility(View.VISIBLE);
        }*/
//            categorylistAdapter = new CategorylistAdapter(AdvanceCompletedActivity.this, category_newArrayList);
//            recyclerview_advancesearch.setAdapter(categorylistAdapter);
    }
    private void dailogue_forgot() {
        builder.setMessage(R.string.alert_message);

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.alert_message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dailogue_forgot();
    }
}
