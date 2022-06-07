package com.i2donate.Activity;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.widget.Toast.LENGTH_SHORT;

import static androidx.camera.core.CameraX.getContext;
import static com.i2donate.Model.Imgmodelclass.getDataColumn;
import static com.i2donate.Model.Imgmodelclass.isDownloadsDocument;
import static com.i2donate.Model.Imgmodelclass.isExternalStorageDocument;
import static com.i2donate.Model.Imgmodelclass.isGooglePhotosUri;
import static com.i2donate.Model.Imgmodelclass.isMediaDocument;
import static com.yalantis.ucrop.util.BitmapLoadUtils.exifToDegrees;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.i2donate.BuildConfig;
import com.i2donate.CameraHelper;
import com.i2donate.Commonmethod.ConstantFunctions;
import com.i2donate.Commonmethod.SearchableSpinner;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.Model.CurrencyBean;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.RetrofitAPI.CommonResponseModel;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;
import com.i2donate.Utils;
import com.i2donate.Validation.Validation;
import com.i2donate.utility.FileUtils;
import com.i2donate.utility.PathUtil;
import com.sromku.simple.storage.SimpleStorage;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import in.gauriinfotech.commons.Commons;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
       private int REQUEST_CODE_PERMISSIONS = 101;
//    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private PopupWindow pw;
    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_DOC = 123;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 777;
    private static String storage;
    private String userChoosenTask;
    private String document_choice="";
    private String file_type="";
    private File file,file1,file2;
    private ArrayList<Uri> docPaths = new ArrayList<>();
    private Uri fileUri = null;
    private String selectedPath = "";
    private Uri myuri = null;
    private Uri myuri2 = null;
    private Uri myuri3 = null;
    private ProgressDialog dialog;
    private ImageView imageview1;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    ImageView back_icon_img,imgPREVIEW1,imgPREVIEW2,imgPREVIEW3;
    TextView login_btn_tv,business_text,incorp_text,taxid_text,other_text;
    LinearLayout progresslay;

    @BindView(R.id.name_input_layout)
    TextInputLayout name_input_layout;
    @BindView(R.id.email_input_layout)
    TextInputLayout email_input_layout;
    @BindView(R.id.mobile_input_layout)
    TextInputLayout mobile_input_layout;
    @BindView(R.id.b_mobile_input_layout)
    TextInputLayout b_mobile_input_layout;
    @BindView(R.id.b_email_input_layout)
    TextInputLayout b_email_input_layout;
    @BindView(R.id.password_input_layout)
    TextInputLayout password_input_layout;
    @BindView(R.id.confirm_input_layout)
    TextInputLayout confirm_input_layout;
    @BindView(R.id.business_name_input_layout)
    TextInputLayout business_first_name_input_layout;
    @BindView(R.id.business_last_name_input_layout)
    TextInputLayout business_last_name_input_layout;
    @BindView(R.id.address_input_layout)
    TextInputLayout address_input_layout;
    @BindView(R.id.Zipcode_input_layout)
    TextInputLayout Zipcode_input_layout;



    TextInputLayout business_name_input_layout;
    EditText reg_name_et, reg_address_et,reg_address_et1,business_reg_first_name_et,business_last_reg_name_et, reg_email_et, reg_mobile_et,
            b_reg_email_et,Zib_address_et, b_reg_mobile_et,reg_password_et, reg_confirm_password_et,business_reg_name_et;
    SearchableSpinner country_spinner;

 /*   SearchableSpinner state_spinner;
    SearchableSpinner city_spinner;*/

    TextView state_spinner;
    TextView city_spinner;
    Button register_btn;
    private LinearLayout linear_layout,address_linear;
     private  Button activity_signup_documentUpload_LAY,activity_signup_documentUpload_LAY1,activity_signup_documentUpload_LAY2;
    TextView iagree_tv,activity_signup_docPreview_TXT,password_note;
    private final static String API_KEY = "";
    ArrayList<String> country = new ArrayList<>();
    ArrayList<String> State = new ArrayList<>();
    ArrayList<String> city = new ArrayList<>();
    ArrayList<String> country_name_id = new ArrayList<String>();
    ArrayList<CurrencyBean> country1 = new ArrayList<CurrencyBean>();
    List<CurrencyBean> country_name_list = new ArrayList<CurrencyBean>();

    Dialog dialog1;
    Dialog dialog2;
    private String check_state="";
    private String check_city_click="0";
    private String check_city="";
    ApiInterface apiService;
    String country_name;
    JSONArray jsonArray;
    ImageView back_icon_login_img, google_sign_btn, facebook_login, twitter_login;
//    LoginButton facebook_login_btn;
    private GoogleApiClient mGoogleApiClient;
//    CallbackManager callbackManager;
private Context context;
    private final String PACKAGE = "com.i2donate";
    RadioButton radio_btn_male, radio_btn_female, radio_btn_orthers,radio_btn_yes,radio_btn_no;
    String radi_gender = "";
    String radi_business = "";
    CheckBox checkbox_btn;
    String country_symbol = "";
    SessionManager sessionManager;
    IDonateSharedPreference iDonateSharedPreference;
    private LinearLayout register_gender_layout;
    String type="";
    String business_name="";
    private static int CAMERA_REQUEST_2 = 22;
    TwitterLoginButton twitter_login_btn;
    private TwitterAuthClient client;
    String device_token;
    LinearLayout terms_layout,linear_business,document_layout;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // getWindow().setBackgroundDrawableResource(R.drawable.dashbord_background);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       FileUtils.enableStrictMode();



      //  CountryAPI1();

        init();
        getToken();
        listioner();
        CountryAPI();

        incorp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (file_type.equals("SELECT_DOC")){

                   FileUtils.pdfViewer(myuri.toString(),RegisterActivity.this);

               } else {
                    PopView("","",""+myuri.toString());
                }

            }
        });


        taxid_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if (file_type.equals("SELECT_DOC")){
                  FileUtils.pdfViewer(myuri2.toString(),RegisterActivity.this);
               } else {
                PopView("","",""+myuri2.toString());
            }}
        });

        other_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   imageview1.setVisibility(View.VISIBLE);
                taxid_text.setVisibility(View.GONE);*/
              if (file_type.equals("SELECT_DOC")){
                  FileUtils.pdfViewer(myuri3.toString(),RegisterActivity.this);
               } else {
                PopView("","",""+myuri3.toString());
            }
            }
        });

    }




    private void individual() {
        if (radio_btn_yes.isChecked()){
            radi_business="no";
            type="Individual";
            name_input_layout.setVisibility(View.VISIBLE);
            email_input_layout.setVisibility(View.VISIBLE);
            mobile_input_layout.setVisibility(View.VISIBLE);
            b_email_input_layout.setVisibility(View.GONE);
            b_mobile_input_layout.setVisibility(View.GONE);
            document_layout.setVisibility(View.GONE);
            linear_business.setVisibility(View.GONE);
            business_text.setVisibility(View.GONE);
            password_note.setVisibility(View.VISIBLE);
            password_input_layout.setVisibility(View.VISIBLE);
            business_reg_name_et.setText("");
            register_gender_layout.setVisibility(View.VISIBLE);
            business_name_input_layout.setVisibility(View.GONE);
            if (radio_btn_male.isChecked()) {
                radi_gender = "M";
            } else if (radio_btn_female.isChecked()) {
                radi_gender = "F";
            } else if (radio_btn_orthers.isChecked()) {
                radi_gender = "O";
            } else {
                radi_gender = "";
            }
        }else {
            type="business";
            password_note.setVisibility(View.GONE);
            radi_gender = "";
            business_name=business_reg_name_et.getText().toString();
            register_gender_layout.setVisibility(View.GONE);
            business_name_input_layout.setVisibility(View.VISIBLE);

        }

    }



    private void CountryAPI() {
    /*    final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/

        progresslay.setVisibility(View.VISIBLE);
        State.clear();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("email", API_KEY);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.i("TESTSTATE","TESTSTATE before :");
        Call<JsonObject> call = apiService.Statecity(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("TESTSTATE","TESTSTATE success :");
                Log.e(TAG,"123" + response);

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                    Log.e(TAG, "123" + jsonObject);
                    //String message = jsonObject.getString("message");
                    //Log.e(TAG, "" + message);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                        JSONObject jsonObject3 =jsonObject2.getJSONObject("state");
                        Log.i("TESTSTATE","TESTSTATE jsonObject3 :"+jsonObject3);
                        Iterator<String> keys= jsonObject3.keys();

                        while (keys.hasNext())
                        {
                            String keyValue = (String)keys.next();
                            String valueString = jsonObject3.getString(keyValue);
                            Log.i("TESTSTATE","TESTSTATE keyValue :"+keyValue);

                            State.add(keyValue);
                        }
                    //    selectstate();

//                        progressDialog.dismiss();
                        progresslay.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.i("TESTSTATE","TESTSTATE failure :"+t.toString());
                Log.e(TAG, t.toString());
//                progressDialog.dismiss();
                progresslay.setVisibility(View.GONE);
            }
        });
    }


    private void CountryAPI1() {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
        progresslay.setVisibility(View.VISIBLE);
        if (check_city_click.equals("1")){
         city.clear();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("email", API_KEY);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonObject> call = apiService.Statecity(jsonObject1);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG,"123" + response);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                    Log.e(TAG, "123" + jsonObject);

                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                        JSONObject jsonObject3 =jsonObject2.getJSONObject("state");
                        String values=state_spinner.getText().toString();
                        JSONArray jsonArray=jsonObject3.getJSONArray(values);

                        for (int b = 0; b < jsonArray.length(); b++) {

                            city.add(jsonArray.getString(b));
                        }
                        check_city_click="0";
                        selectcity();
                        progresslay.setVisibility(View.GONE);
                     //   progressDialog.dismiss();
//                        city_spinner.setData(city);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
               // progressDialog.dismiss();
                progresslay.setVisibility(View.GONE);
            }
        });

        }
    }



    private void selectstate(){
        try {
            dialog1=new Dialog(this );

            // set custom dialog
            dialog1.setContentView(R.layout.spinner);

            // set custom height and width
            dialog1.getWindow().setLayout(650,800);

            // set transparent background
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog1.show();

            // Initialize and assign variable
            EditText editText=dialog1.findViewById(R.id.edit_text);
            TextView textview =dialog1.findViewById(R.id.textview);
            ListView listView=dialog1.findViewById(R.id.list_view);

            textview.setText("Select State");
            Log.i("TESTSTATE","TESTSTATE"+State);
            // Initialize array adapter
            final ArrayAdapter<String> adapter=new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1,State);

            // set adapter
            listView.setAdapter(adapter);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);



                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    state_spinner.setText(adapter.getItem(i));
                    check_state=adapter.getItem(i);
                    city_spinner.setText("Select City*");
                    check_city="";
                    // CountryAPI1();
                    // Dismiss dialog


                    dialog1.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void  selectcity(){

        try {
            if (city!=null && city.size()>0) {

            dialog2=new Dialog(this);

            // set custom dialog
            dialog2.setContentView(R.layout.spinner);

            // set custom height and width
            dialog2.getWindow().setLayout(650,800);

            // set transparent background
            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog2.show();

            // Initialize and assign variable
            EditText editText=dialog2.findViewById(R.id.edit_text);
            ListView listView=dialog2.findViewById(R.id.list_view);
            TextView textview =dialog2.findViewById(R.id.textview);

            textview.setText("Select City");


                // Initialize array adapter
                final ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1, city);

                // set adapter
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        city_spinner.setText(adapter.getItem(i));
                        check_city = adapter.getItem(i);

                        dialog2.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            Log.i("TESTAPP","TESTAPP"+e.getMessage().toString());
            e.printStackTrace();
        }

    }










    private void init() {


        context=this;
        iDonateSharedPreference = new IDonateSharedPreference();
        sessionManager = new SessionManager(getApplicationContext());
        linear_layout=findViewById(R.id.linear_layout);
        address_linear=findViewById(R.id.address_linear);
        linear_business=findViewById(R.id.linear_business);
        document_layout=findViewById(R.id.document_layout);

        activity_signup_documentUpload_LAY=findViewById(R.id.activity_signup_documentUpload_LAY);
        activity_signup_documentUpload_LAY1=findViewById(R.id.activity_signup_documentUpload_LAY1);
        activity_signup_documentUpload_LAY2=findViewById(R.id.activity_signup_documentUpload_LAY2);
        //activity_signup_docPreview_TXT=findViewById(R.id.activity_signup_docPreview_TXT);
        password_note=findViewById(R.id.password_note);
        back_icon_img = (ImageView) findViewById(R.id.back_icon_img);
        imageview1=findViewById(R.id.imageview1);

        login_btn_tv = (TextView) findViewById(R.id.login_btn_tv);
        business_text=findViewById(R.id.business_text);
        incorp_text=findViewById(R.id.incorp_text);
        taxid_text=findViewById(R.id.taxid_document_text);
        other_text=findViewById(R.id.other_document_text);

        business_first_name_input_layout = (TextInputLayout) findViewById(R.id.business_first_name_input_layout);
        business_last_name_input_layout = (TextInputLayout) findViewById(R.id.business_last_name_input_layout);
        address_input_layout = (TextInputLayout) findViewById(R.id.address_input_layout);
        Zipcode_input_layout = (TextInputLayout) findViewById(R.id.Zipcode_input_layout);


        reg_name_et = (EditText) findViewById(R.id.reg_name_et);
        reg_address_et=findViewById(R.id.reg_address_et);
        reg_address_et1=findViewById(R.id.reg_address_et1);
        Zib_address_et=findViewById(R.id.Zib_address_et);
        progresslay=findViewById(R.id.progresslay);

        business_reg_first_name_et=findViewById(R.id.business_reg_first_name_et);
        business_last_reg_name_et=findViewById(R.id.business_last_reg_name_et);

        name_input_layout = (TextInputLayout) findViewById(R.id.name_input_layout);
        reg_email_et = (EditText) findViewById(R.id.reg_email_et);
        b_reg_email_et = (EditText) findViewById(R.id.b_reg_email_et);
        b_reg_mobile_et = (EditText) findViewById(R.id.b_reg_mobile_et);


        email_input_layout = (TextInputLayout) findViewById(R.id.email_input_layout);
        b_email_input_layout = (TextInputLayout) findViewById(R.id.b_email_input_layout);
        reg_mobile_et = (EditText) findViewById(R.id.reg_mobile_et);
        mobile_input_layout = (TextInputLayout) findViewById(R.id.mobile_input_layout);
        b_mobile_input_layout = (TextInputLayout) findViewById(R.id.b_mobile_input_layout);
        reg_password_et = (EditText) findViewById(R.id.reg_password_et);
        password_input_layout = (TextInputLayout) findViewById(R.id.password_input_layout);
        reg_confirm_password_et = (EditText) findViewById(R.id.reg_confirm_password_et);
        confirm_input_layout = (TextInputLayout) findViewById(R.id.confirm_input_layout);
        country_spinner = (SearchableSpinner) findViewById(R.id.spin_country);
//        state_spinner = (SearchableSpinner) findViewById(R.id.spin_state);
//        city_spinner = (SearchableSpinner) findViewById(R.id.spin_city);

        state_spinner = (TextView) findViewById(R.id.spin_state);
        city_spinner = (TextView) findViewById(R.id.spin_city);

        register_btn = (Button) findViewById(R.id.register_btn);
        radio_btn_male = (RadioButton) findViewById(R.id.radio_btn_male);
        twitter_login_btn = (TwitterLoginButton) findViewById(R.id.twitter_login_btn);
        radio_btn_female = (RadioButton) findViewById(R.id.radio_btn_female);
        radio_btn_orthers = (RadioButton) findViewById(R.id.radio_btn_orthers);
        checkbox_btn = (CheckBox) findViewById(R.id.checkbox_btn);
        business_reg_name_et=(EditText)findViewById(R.id.business_reg_name_et);
        business_name_input_layout=(TextInputLayout)findViewById(R.id.business_name_input_layout);
        radio_btn_yes=(RadioButton)findViewById(R.id.radio_btn_yes);
        radio_btn_no=(RadioButton)findViewById(R.id.radio_btn_no);
        google_sign_btn = (ImageView) findViewById(R.id.google_sign_btn);
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        twitter_login = (ImageView) findViewById(R.id.twitter_login);
//        facebook_login_btn = (LoginButton) findViewById(R.id.facebook_login_btn);
        register_gender_layout=(LinearLayout)findViewById(R.id.register_gender_layout);
        terms_layout=(LinearLayout)findViewById(R.id.terms_layout);
        iagree_tv=(TextView)findViewById(R.id.iagree_tv);

        radio_btn_yes.setChecked(true);
        individual();



      /*  try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(country_spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(80);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }*/

        activity_signup_documentUpload_LAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document_choice="upload1";
                Select_Option();
            }
        });


//        activity_signup_documentUpload_LAY.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean result = Utils.checkExtPermission(RegisterActivity.this);
//                userChoosenTask = "Choose from Library";
//                document_choice="upload1";
//                if (result) {
//                    //  Crop.pickImage(RegistrationFormActivity.this);
//                    openGallery(SELECT_FILE1);
//                }
//            }
//        });



        activity_signup_documentUpload_LAY1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document_choice="upload2";
                Select_Option();

            }
        });

        activity_signup_documentUpload_LAY2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document_choice="upload3";
                Select_Option();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        LoginManager.getInstance().logOut();
//
//
//        /*Facebook login*/
//
//        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

      /*  if (!loggedOut) {

            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());


            getUserProfile(AccessToken.getCurrentAccessToken());
        }
*/
//        facebook_login_btn.setReadPermissions(Arrays.asList("email", "public_profile"));
//        callbackManager = CallbackManager.Factory.create();
        client = new TwitterAuthClient();
       /* facebook_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getUserProfile(AccessToken.getCurrentAccessToken());
                Log.e("click", "click");
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
               Log.d("API123", loggedIn + " ??");

            }

            @Override
            public void onCancel() {

                Log.e("click", "click1");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {

                Log.e("click", "click2" + exception);

                // App code
            }
        });*/
//        facebook_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                getUserProfile(AccessToken.getCurrentAccessToken());
//                Log.e("click", "click");
//
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                Log.e("click", "click");
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.e("click", "click");
//                // App code
//            }
//        });
    }

    public void openGallery(int req_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload"), req_code);
    }

    public void pickPdf() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/pdf");
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        return intent;

      /*  Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), SELECT_DOC);*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {"image/*", "application/pdf"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), SELECT_DOC);
        } else {
            Intent intent = new Intent();
            intent.setType("*/*");
            String[] mimetypes = {"image/*", "application/pdf"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), SELECT_DOC);
        }
    }


    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    @SuppressLint("RestrictedApi")
    private void PopView(final String position, final String title, final String alert) {
        final Dialog dialog = new Dialog(RegisterActivity.this,android.R.style.Theme_Light);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_view_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final String title1=title;
        final String alert1=alert;
//        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        ImageView imageview = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView ivclose = (ImageView) dialog.findViewById(R.id.ivclose);
        ImageView ivdelete = (ImageView) dialog.findViewById(R.id.ivdelete);

//        tv_no.setText(db.getvalue("view"));
//        tv_yes.setText(db.getvalue("upload"));
  /*      Bitmap bitmap = null;
        //  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(alert));
        bitmap = decodeSampledBitmapFromPath(alert, 275, 200);
        imageview.setImageBitmap(bitmap);*/


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math
                .abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap bitmap = BitmapFactory.decodeFile(alert, options);


        Bitmap thumbnail = bitmap;
        final String picturePath = alert;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        File curFile = new File(picturePath);
        try {
            ExifInterface exif = new ExifInterface(curFile.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);

            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
            }
            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
        } catch (IOException ex) {
            Log.e("TAG", "Failed to get Exif data", ex);
        }

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);






        imageview.setImageBitmap(thumbnail);


        ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sessionManager.setbitmapPop("");
//                sessionManager.setS3Pop("");
//                binding.dispatchPreview.setText(db.getvalue("takephoto_pop"));
//                binding.iconImage.setVisibility(View.VISIBLE);
//                dialog.dismiss();
            }
        });

//        tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                typeofimage  =0;
//                //  images/dispatch_proof
//                startTripViewModel.uploadpohoto(0);
//                dialog.dismiss();
//            }});
//        tv_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//            }
//        });

    }


    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (is != null){
                is.close();
            }
            if (os != null){
                os.close();
            }
        }
    }




    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 4;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    if (Environment.isExternalStorageRemovable()) {
                        storageDefinition = "EXTERNAL_STORAGE";
                    } else {
                        storageDefinition = "SECONDARY_STORAGE";
                    }
                    return System.getenv(storageDefinition) + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {

                // DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String otherId = null;
                if (id != null && id.startsWith("msf:")) {
                    String[] split = id.split(":");
                    otherId = split[1];
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {

                    Uri contentUri = null;
                    if (otherId == null) {
                        contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    } else {
                        contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(otherId));
                    }
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        } else {
                            //debug.e("Path is null for " + contentUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //DebugLog.e(e.getMessage());
                    }
                }

               // DebugLog.e("File is not accessible");

                // return destinationPath;
                return null;
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }

        return null;
    }


    public String getStringPdf (Uri filepath){
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream =  getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }


    public String getPaths(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath1(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }


            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void Select_Option() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Document" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add file");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    boolean result = Utils.checkExtPermission(RegisterActivity.this);
                    if (result) {
                        if (SimpleStorage.isExternalStorageWritable()) {
                            storage = Environment.getExternalStorageDirectory() + "/DCIM";
                            Log.e("Test", "Writing in External" + storage);
                        } else {
                            storage = SimpleStorage.getInternalStorage(RegisterActivity.this) + "/DCIM";
                            Log.e("Test", "Writing in Internal");
                        }
                        captureImage();
                    }
                }
                else if (options[item].equals("Choose from Gallery")) {
                    boolean result = Utils.checkExtPermission(RegisterActivity.this);
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        //  Crop.pickImage(RegistrationFormActivity.this);
                        openGallery(SELECT_FILE1);
                    }
                }
                else if (options[item].equals("Document")) {
                    boolean result = Utils.checkExtPermission(RegisterActivity.this);
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        //  Crop.pickImage(RegistrationFormActivity.this);
                        // openGallery(SELECT_FILE1);
                        pickPdf();
                       // startActivityForResult(Intent.createChooser(pickPdf(), "Open with"), SELECT_DOC);
                    }
                   // dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
             if (requestCode == CameraHelper.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

                 try {

                     if (requestCode == CameraHelper.REQUEST_TAKE_PHOTO) {

                         File curFile=CameraHelper.INSTANCE.getImageFile();
                         selectedPath = curFile.getAbsolutePath();
//                         Uri picUri = Uri.fromFile(curFile);

                     }

                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 file_type="CAMERA_REQUEST_2";
               // selectedPath = fileUri.getPath();

//                Uri imguri = data.getData ();
                if(document_choice.equalsIgnoreCase("upload1")){
                    final Bitmap bitmap = decodeSampledBitmapFromPath(selectedPath, 275, 200);
                  //  myuri = SaveImage(bitmap, "I2Donate" + "_" + random() + ".jpg");
                    myuri= Uri.parse(selectedPath);
                    String lStr = myuri.toString();
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    incorp_text.setText(lStr);

                    incorp_text.setTextColor(getResources().getColor(R.color.register_blue));
                }
                else if(document_choice.equalsIgnoreCase("upload2")){
                    final Bitmap bitmap = decodeSampledBitmapFromPath(selectedPath, 275, 200);
                   // myuri2 = SaveImage(bitmap, "I2Donate" + "_" + random() + ".jpg");
                      myuri2= Uri.parse(selectedPath);
                    String lStr = myuri2.toString();

                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    taxid_text.setText(lStr);
                    taxid_text.setTextColor(getResources().getColor(R.color.register_blue));

                }
                else{
                    final Bitmap bitmap = decodeSampledBitmapFromPath(selectedPath, 275, 200);
                  //  myuri3 = SaveImage(bitmap, "I2Donate" + "_" + random() + ".jpg");

                    myuri3 = Uri.parse(selectedPath);
                    String lStr = myuri3.toString();
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    other_text.setText(lStr);

                    other_text.setTextColor(getResources().getColor(R.color.register_blue));
                }

            }

            else if (requestCode == SELECT_FILE1) {
                 file_type="CAMERA_REQUEST_2";
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = RegisterActivity.this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                if(document_choice.equalsIgnoreCase("upload1")) {
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    imageview1.setVisibility(View.GONE);
                    imageview1.setImageBitmap(bitmap);
                    myuri = Uri.parse(new File(picturePath).toString());
                    String lStr = picturePath;
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    incorp_text.setText(lStr);
                    incorp_text.setTextColor(getResources().getColor(R.color.register_blue));
                }
                else if(document_choice.equalsIgnoreCase("upload2")){
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    myuri2 = Uri.parse(new File(picturePath).toString());
                    String lStr = picturePath;
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    taxid_text.setText(lStr);
                    taxid_text.setTextColor(getResources().getColor(R.color.register_blue));
                }
                else{
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    myuri3 = Uri.parse(new File(picturePath).toString());
                    String lStr = picturePath;
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    other_text.setText(lStr);
                    other_text.setTextColor(getResources().getColor(R.color.register_blue));

                }
            }

            else if (requestCode == SELECT_DOC) {
                file_type="SELECT_DOC";
                Uri uri = data.getData();
                selectedPath = uri.getPath();
                 myuri = Uri.parse(selectedPath);
                 //selectedPath= Commons.getPath(uri, this);
                 String filePath = FileUtils.getRealPath(this, uri);
                 File file2 = new File(filePath);

                 selectedPath= String.valueOf(file2);

                 if(document_choice.equalsIgnoreCase("upload1")) {

                    myuri = Uri.parse(selectedPath);
                    String lStr = String.valueOf(myuri);
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    incorp_text.setText(lStr);
                    incorp_text.setTextColor(getResources().getColor(R.color.register_blue));
                }
                else if(document_choice.equalsIgnoreCase("upload2")){
                    myuri2 = Uri.parse(selectedPath);
                    String lStr = String.valueOf(myuri2);
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    taxid_text.setText(lStr);
                    taxid_text.setTextColor(getResources().getColor(R.color.register_blue));
                }
                else if(document_choice.equalsIgnoreCase("upload3")){
                    String pathss= getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
                    myuri3 = Uri.parse(selectedPath);
                   // Uri path = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".helper.ProviderClass", pdfFile);
                    //myuri3 = Uri.parse(new File(pathss).toString());
                    String lStr = String.valueOf(myuri3);
                    lStr = lStr.substring(lStr.lastIndexOf("/")+1);
                    other_text.setText(lStr);
                    other_text.setTextColor(getResources().getColor(R.color.register_blue));
                }



             //    ContentUriUtils.INSTANCE.getFilePath(getContext(), uri);

            }


            else{
                //callbackManager.onActivityResult(requestCode, resultCode, data);

                if (requestCode == RC_SIGN_IN) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result);
                    Log.e(TAG, "Got cached sign-in1");
                }
                if (client != null)
                    client.onActivityResult(requestCode, resultCode, data);

                // Pass the activity result to the login button.
                twitter_login_btn.onActivityResult(requestCode, resultCode, data);


            }




        } catch (Exception e) {
            e.printStackTrace();
            // question_rv.removeAllViewsInLayout();
        }
    }




    File photoFile,imageFilePath;
    private void captureImage() {

        /*photoFile = getFileToProfile();
        Uri photoURI = null;
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".fileprovider", photoFile);
        }
        Intent c = new Intent(ACTION_IMAGE_CAPTURE);
        c.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        c.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(c, CAMERA_REQUEST_2);*/


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camera_FileUri = getOutputMediaFileUri(1);
        Uri camera_FileUri = CameraHelper.INSTANCE.getOutputMediaFileUri(this);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, camera_FileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        /*if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_2);
        }*/
        startActivityForResult(intent, CameraHelper.REQUEST_TAKE_PHOTO);



      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }*/




    }



    private File getFileToProfile() {
        String appDirectoryName = "i2donate";
        imageFilePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);
        if (!imageFilePath.exists()) {
            imageFilePath.mkdir();
        } else if (!imageFilePath.isDirectory()) {
            imageFilePath.delete();
            imageFilePath.mkdir();
        }
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File finalFile = new File(imageFilePath,
                name + ".jpg");
        return finalFile;
    }



    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }



    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(storage);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(storage, "Oops! Failed create "
                        + storage + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_"+ ".jpg");
        } else {


            return null;
        }

        return mediaFile;
    }

/*
    private Uri SaveImage(Bitmap finalBitmap, String filename) {

        String root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)), appDirectoryName;
        File myDir = new File(root + "/I2Donate");
        myDir.mkdirs();

        String fname = filename;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            Uri uri = Uri.fromFile(file);

            out.flush();
            out.close();
            return uri;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/




    private void listioner() {
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOnline()) {
                    defaultLoginTwitter();
                    Log.e("click", "click243");
//                    Log.e("click", "click243");


                } else {
                    //Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    ConstantFunctions.showSnakBar("Please check internet connection", v);
                }

            }
        });
        back_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        login_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity.changeActivity(RegisterActivity.this, LoginActivity.class);
                finish();
            }
        });
        radio_btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_btn_no.isChecked()){
                    radi_business="yes";
                    type="business";

                    email_input_layout.setVisibility(View.GONE);
                    mobile_input_layout.setVisibility(View.GONE);

                    b_email_input_layout.setVisibility(View.VISIBLE);
                    b_mobile_input_layout.setVisibility(View.VISIBLE);

                    name_input_layout.setVisibility(View.GONE);
                    document_layout.setVisibility(View.VISIBLE);
                    business_text.setVisibility(View.VISIBLE);
                    linear_business.setVisibility(View.VISIBLE);
                    linear_layout.setVisibility(View.GONE);
                    address_linear.setVisibility(View.VISIBLE);
                    password_note.setVisibility(View.GONE);
                    password_input_layout.setVisibility(View.GONE);
                    register_gender_layout.setVisibility(View.GONE);
                    radi_gender="";
                    business_name=business_reg_name_et.getText().toString();
                    business_name_input_layout.setVisibility(View.VISIBLE);
                }else {
                    password_note.setVisibility(View.VISIBLE);
                    password_input_layout.setVisibility(View.VISIBLE);
                    register_gender_layout.setVisibility(View.VISIBLE);
                    business_reg_name_et.setText("");
                    type="Individual";
                    if (radio_btn_male.isChecked()) {
                        radi_gender = "M";
                    } else if (radio_btn_female.isChecked()) {
                        radi_gender = "F";
                    } else if (radio_btn_orthers.isChecked()) {
                        radi_gender = "O";
                    } else {

                        radi_gender = "";
                    }
                    business_name_input_layout.setVisibility(View.GONE);
                }
            }
        });
        radio_btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                individual();
            }
        });

        business_reg_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (business_reg_name_et.getText().toString().trim().length() <= 2) {
                    business_name_input_layout.setError("Required valid Business name");
                } else {
                    business_name_input_layout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        business_reg_first_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (business_reg_first_name_et.getText().toString().trim().length() <= 1) {
                    business_first_name_input_layout.setError("Required valid First name");
                } else {
                    business_first_name_input_layout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        business_last_reg_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (business_last_reg_name_et.getText().toString().trim().length() <= 1) {
                    business_last_name_input_layout.setError("Required valid Last name");
                } else {
                    business_last_name_input_layout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b_reg_mobile_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b_reg_mobile_et.getText().toString().trim().length() <= 9) {
                    b_mobile_input_layout.setError("Required valid mobile number");
                } else {
                    b_mobile_input_layout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reg_address_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (reg_address_et.getText().toString().trim().length() <= 0) {
                    address_input_layout.setError("Required address line 1");
                } else {
                    address_input_layout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Zib_address_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Zib_address_et.getText().toString().trim().length() <= 0) {
                    Zipcode_input_layout.setError("Required Zipcode");
                } else {
                    Zipcode_input_layout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reg_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(isValidPassword(reg_password_et.getText().toString().trim()));
                {
                    password_input_layout.setError("Enter valid password minimum 8 char and maximum 16 char");
                }{
                    password_input_layout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordvalidation = s.toString();
                if (passwordvalidation.length()>=8 && passwordvalidation.matches(Validation.PASSWORD_PATTERN)){
                    password_input_layout.setError("");
                }else {
                    password_input_layout.setError("Password is invalid");
                }
            }
        });





        reg_email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (reg_email_et.getText().toString().trim().matches(Validation.emailPattern)) {
                    email_input_layout.setError("");
                } else {
                    email_input_layout.setError("Required email");
                }


//                if (charSequence.toString().startsWith(" ")) {
//                    reg_email_et.setText("");
//                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
//                    email_input_layout.setError("Required email");
//                    //disableButton(...)
//                } else {
//                    email_input_layout.setError("");
//                    if (reg_name_et.getText().toString().trim().length() <= 0) {
//                        name_input_layout.setError("Required name");
//                    } else
//                        {
//                        name_input_layout.setError("");
//                        if (reg_email_et.getText().toString().trim().length() <= 0) {
//                            email_input_layout.setError("Required username");
//                        } else {
//                            if (reg_email_et.getText().toString().trim().matches(Validation.emailPattern)) {
//                                email_input_layout.setError("");
//                            } else {
//                                email_input_layout.setError("Required email");
//                            }
//
//                        }
//                    }

                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
                    //enableButton(...)
              //  }

//                if (reg_mobile_et.getText().toString().trim().length() <= 10) {
//                    mobile_input_layout.setError("Enter valid Mobile Number");
//                }
//                else{
//                    mobile_input_layout.setError("");
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//
//        reg_email_et.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (reg_name_et.getText().toString().trim().length() <= 0) {
//                    name_input_layout.setError("Required name");
//                } else {
//                    name_input_layout.setError("");
//
//                }
//                if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                    mobile_input_layout.setError("Required Mobile Number");
//                }
//                else{
//                    mobile_input_layout.setError("");
//                }
//                return false;
//            }
//        });

//        reg_mobile_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.toString().startsWith(" ")) {
//                    reg_mobile_et.setText("");
//                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
//                   // mobile_input_layout.setError("Required mobile number");
//                    //disableButton(...)
//                } else {
//                    if (reg_email_et.getText().toString().trim().length() <= 0) {
//                        email_input_layout.setError("Required email");
//                    }
//                    else {
//                        if (reg_email_et.getText().toString().trim().matches(Validation.emailPattern)) {
//                            email_input_layout.setError("");
//                          /*  if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                              //  mobile_input_layout.setError("Required mobile number");
//                            } else {
//                               // mobile_input_layout.setError("");
//                            }*/
//                        } else {
//                            email_input_layout.setError("Required email");
//                        }
//
//                    }
//                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
//                    //enableButton(...)
//                }
//                if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                    mobile_input_layout.setError("Required Mobile Number");
//                }
//                else{
//                    mobile_input_layout.setError("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

//        reg_mobile_et.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (reg_email_et.getText().toString().trim().length() <= 0) {
//                    email_input_layout.setError("Required email");
//                } else {
//                    if (reg_email_et.getText().toString().trim().matches(Validation.emailPattern)) {
//                        email_input_layout.setError("");
//                    } else {
//                        email_input_layout.setError("Required email");
//                    }
//
//                }
//                if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                    mobile_input_layout.setError("Required Mobile Number");
//                }
//                else{
//                    mobile_input_layout.setError("");
//                }
//                return false;
//            }
//        });

        reg_password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.toString().startsWith(" ")) {
//                    reg_password_et.setText("");
//                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
//                    password_input_layout.setError("Required password");
//                    //disableButton(...)
//                }
//                else {
//                    if (reg_password_et.getText().toString().trim().length() <= 0) {
//                        password_input_layout.setError("Required password");
//                    }
//                    else {
//                        password_input_layout.setError("");
//
//                    }
//                   /* if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                        mobile_input_layout.setError("Required email");
//                    } else {
//                        mobile_input_layout.setError("");
//                        if (reg_password_et.getText().toString().trim().length() <= 0) {
//                            password_input_layout.setError("Required mobile number");
//                        } else {
//                            password_input_layout.setError("");
//
//                        }
//                    }*/
//                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
//                    //enableButton(...)
//                }
//                if (reg_mobile_et.getText().toString().trim().length() <= 0) {
//                    mobile_input_layout.setError("Required Mobile Number");
//                }
//                else{
//                    mobile_input_layout.setError("");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordvalidation = s.toString();
                if (passwordvalidation.length()>=8 && passwordvalidation.matches(Validation.PASSWORD_PATTERN)){
                    password_input_layout.setError("");
                }else {
                    password_input_layout.setError("Password is invalid");
                }
            }
        });

       /* reg_password_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (reg_mobile_et.getText().toString().trim().length() <= 0) {
                    mobile_input_layout.setError("Required mobile number");
                } else {
                    mobile_input_layout.setError("");

                }
                return false;
            }
        });*/
//        reg_confirm_password_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (charSequence.toString().startsWith(" ")) {
//                    reg_confirm_password_et.setText("");
//                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
//                    confirm_input_layout.setError("Required password");
//                    //disableButton(...)
//                } else {
//
//                    if (reg_password_et.getText().toString().trim().length() <= 0) {
//                        password_input_layout.setError("Required email");
//                    } else {
//                        password_input_layout.setError("");
//                        if (reg_confirm_password_et.getText().toString().trim().length() <= 0) {
//                            confirm_input_layout.setError("Required password");
//                        } else {
//                            confirm_input_layout.setError("");
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (reg_password_et.getText().toString().trim().equals(reg_confirm_password_et.getText().toString().trim())) {
//
//                } else {
//                    confirm_input_layout.setError("Required correct password");
//                }
//
//            }
//        });
//        reg_confirm_password_et.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (reg_password_et.getText().toString().trim().length() <= 0) {
//                    password_input_layout.setError("Required password");
//                } else {
//                    password_input_layout.setError("");
//
//                }
//                return false;
//            }
//        });
        iagree_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkbox_btn.isChecked()){
                   checkbox_btn.setChecked(false);
               }else {
                   checkbox_btn.setChecked(true);
               }
            }
        });
        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity.changeActivityData(RegisterActivity.this, TermsAndConditionActivity.class,"");
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (type.equalsIgnoreCase("Individual")) {
                    individual_Register();
                } else if (type.equalsIgnoreCase("business")) {
                   business_Register();
                }
//                 else{
//                     Toast.makeText(RegisterActivity.this, "please select Individual or Business", LENGTH_SHORT).show();
//                 }
//                if (!reg_name_et.getText().toString().trim().isEmpty() && !reg_email_et.getText().toString().trim().isEmpty() && !reg_password_et.getText().toString().trim().isEmpty()
//                ||!business_reg_name_et.getText().toString().trim().isEmpty()&&radi_business.equalsIgnoreCase("yes")) {
//
//                    if (reg_email_et.getText().toString().trim().matches(Validation.emailPattern)) {
//                        // if (reg_password_et.getText().toString().trim().equals(reg_confirm_password_et.getText().toString().trim())) {
//
////                        if(radi_business.equalsIgnoreCase("yes")){
////
////                        }
//
//                        if (reg_password_et.getText().toString().trim().matches(Validation.PASSWORD_PATTERN) && reg_password_et.getText().toString().trim().length()>=8||radi_business.equalsIgnoreCase("yes") ){
//                            radiofn();
//                            radiobussi();
//                            /// if (!radi_gender.isEmpty()) {
//                            if (!radi_business.isEmpty()){
//                                if (checkbox_btn.isChecked()) {
//                                    if (!country_symbol.isEmpty()) {
//
//                                        if (isOnline()) {
//
//                                            //RegisterAPI();
//
//
//
//
//                                        }
//                                        else {
//                                            //Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
//                                            ConstantFunctions.showSnakBar("Please check internet connection", v);
//                                        }
//                                    } else {
//                                        Toast.makeText(RegisterActivity.this, "Select country", Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(RegisterActivity.this, "Please accept our Terms and Conditions", Toast.LENGTH_SHORT).show();
//                                }
//                            }else {
//                                Toast.makeText(RegisterActivity.this, "Please select Register as Individual / Business", Toast.LENGTH_SHORT).show();
//                            }
//
//                           /* } else {
//                                Toast.makeText(RegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
//                            }*/
//                        }else {
//                            password_input_layout.setError("Password format is invalid");
//                            Toast.makeText(RegisterActivity.this, "Password format is invalid", Toast.LENGTH_SHORT).show();
//                        }
//                       /* } else {
//                            Toast.makeText(RegisterActivity.this, "Verify password", Toast.LENGTH_SHORT).show();
//                        }*/
//
//
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Enter Correct Mail ID", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
//                }
//            }

            }
        });


        country_spinner.setSelectionListener(new SearchableSpinner.OnSelectionListener() {
            @Override
            public void onSelect(int spinnerId, int i, String value) {
                Log.e("Select2", "Position : " + i + " : Value : " + value + " : " + spinnerId);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(country_spinner.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                for (int i1 = 0; i1 < jsonArray.length(); i1++) {

                    JSONObject countryobject = null;
                    try {
                        countryobject = jsonArray.getJSONObject(i1);
                        if (countryobject.getString("name").equals(value)) {
                            country_symbol = countryobject.getString("sortname");
                            Log.e(TAG, country_symbol);
                        }
                    } catch (JSONException e)
                    {
                    }
                }
            }
        });


//        state_spinner.setSelectionListener(new SearchableSpinner.OnSelectionListener() {
//            @Override
//            public void onSelect(int spinnerId, int position, String value) {
//                CountryAPI1();
//                state_spinner.getValue();
//            }
//        });
//
//        city_spinner.setSelectionListener(new SearchableSpinner.OnSelectionListener() {
//            @Override
//            public void onSelect(int spinnerId, int position, String value) {
//                CountryAPI1();
//                city_spinner.getValue();
//            }
//        });
        state_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  CountryAPI();
                selectstate();
            }
        });

        city_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check_state.equalsIgnoreCase("")||check_state==null){
                    Toast.makeText(RegisterActivity.this, "Please select State", LENGTH_SHORT).show();
                }
                else{

                    if (check_city_click.equals("0")){
                        check_city_click="1";
                        CountryAPI1();
                    }


                }

            }
        });


        google_sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {

                    signIn();

                } else {
                    //Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    ConstantFunctions.showSnakBar("Please check internet connection", v);
                }

            }
        });

        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {

                   // facebook_login_btn.performClick();
                    Log.e("click", "click243");

                } else {
                    //Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    ConstantFunctions.showSnakBar("Please check internet connection", v);
                }

            }
        });
    }

    private void individual_Register(){
        if(reg_name_et.getText().toString().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter name", LENGTH_SHORT).show();
        }else if (reg_name_et.getText().toString().trim().length() <= 1) {
            name_input_layout.setError("Required valid First name");
        }
        else if(reg_email_et.getText().toString().trim().isEmpty()||!reg_email_et.getText().toString().trim().matches(Validation.emailPattern)){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", LENGTH_SHORT).show();
        }
        else if(reg_mobile_et.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid mobile number", LENGTH_SHORT).show();

        }else if(reg_mobile_et.getText().toString().trim().length()<= 9){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid mobile number", LENGTH_SHORT).show();

        }

        else if(reg_password_et.getText().toString().trim().isEmpty()||!reg_password_et.getText().toString().trim().matches(Validation.PASSWORD_PATTERN)){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid Password", LENGTH_SHORT).show();

        }
//        else if(radi_gender.isEmpty()){
//            Toast.makeText(RegisterActivity.this, "Please select Gender", LENGTH_SHORT).show();
//        }
        /*else if(reg_address_et1.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter your address1", LENGTH_SHORT).show();

        }*/
        else if(check_state.equalsIgnoreCase("")||check_state.isEmpty()){
            Toast.makeText(RegisterActivity.this, "please select state", LENGTH_SHORT).show();

        }
        else if(check_city.equalsIgnoreCase("")||check_city.isEmpty()){
            Toast.makeText(RegisterActivity.this, "please select City", LENGTH_SHORT).show();

        }
        else if(Zib_address_et.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "please Enter Zipcode  ", LENGTH_SHORT).show();

        }
        else if(!checkbox_btn.isChecked()){
            Toast.makeText(RegisterActivity.this, "please agree Termas and conditions", LENGTH_SHORT).show();
        }
        else{
            RegisterAPI();
        }
    }


    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void business_Register(){
        if(business_reg_name_et.getText().toString().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Business name", LENGTH_SHORT).show();
        }
        else if(business_reg_first_name_et.getText().toString().trim().isEmpty()||business_last_reg_name_et.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter person name", LENGTH_SHORT).show();
        } else if (business_reg_first_name_et.getText().toString().trim().length() <= 1) {
            business_first_name_input_layout.setError("Required valid First name");
        } else if (business_last_reg_name_et.getText().toString().trim().length() <= 1){
            business_last_name_input_layout.setError("Required valid Last name");
        }
        else if(b_reg_email_et.getText().toString().trim().isEmpty()||!b_reg_email_et.getText().toString().trim().matches(Validation.emailPattern)){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid Email", LENGTH_SHORT).show();
        } else if(b_reg_mobile_et.getText().toString().trim().length()<= 9){
            Toast.makeText(RegisterActivity.this, "Please Enter Valid mobile number", LENGTH_SHORT).show();

        } else if(reg_address_et.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter your address", LENGTH_SHORT).show();
        }
        else if(check_state.equalsIgnoreCase("")||check_state.isEmpty()){
            Toast.makeText(RegisterActivity.this, "please select state", LENGTH_SHORT).show();
        }
        else if(check_city.equalsIgnoreCase("")||check_city.isEmpty()){
            Toast.makeText(RegisterActivity.this, "please select City", LENGTH_SHORT).show();
        }
        else if(Zib_address_et.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "please Enter Zipcode  ", LENGTH_SHORT).show();
        }
        else if(myuri==null){
            Toast.makeText(RegisterActivity.this, "Please upload Incorporation Documents", LENGTH_SHORT).show();
        }
        else if(myuri2==null){
            Toast.makeText(RegisterActivity.this, "Please upload Tax id certificate", LENGTH_SHORT).show();
        }
        else if(!checkbox_btn.isChecked()){
            Toast.makeText(RegisterActivity.this, "please accept agree Termas and conditions", LENGTH_SHORT).show();
        }
        else{
            String user_name = business_reg_first_name_et.getText().toString().trim() +
                    business_last_reg_name_et.getText().toString().trim();
            String user_mobile = b_reg_mobile_et.getText().toString().trim();
            String user_email = b_reg_email_et.getText().toString().trim();
            String address = reg_address_et.getText().toString().trim();

            if(myuri3==null){
                postImageToServer_optional(myuri, myuri2, business_reg_name_et.getText().toString(), user_name, user_mobile, user_email,
                        "124354", address, type, "yes",reg_address_et.getText().toString(),
                        reg_address_et1.getText().toString(),state_spinner.getText().toString(),city_spinner.getText().toString(),Zib_address_et.getText().toString());
            }
            else{
                postImageToServer(myuri, myuri2, myuri3, business_reg_name_et.getText().toString(), user_name, user_mobile, user_email,
                        "124354", address, type, "yes",reg_address_et.getText().toString(),
                        reg_address_et1.getText().toString(),state_spinner.getText().toString(),city_spinner.getText().toString(),Zib_address_et.getText().toString());
            }
        }
    }
      //File picker
    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }
    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new com.twitter.sdk.android.core.Callback<String>() {
            @Override
            public void success(Result<String> result) {
                //here it will give u only email and rest of other information u can get from TwitterSession
//                Log.e(TAG, result.data);
                /*Toast.makeText(LoginActivity.this, "User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"User Id : " + twitterSession.getUserId()+ "\nUser Name : " + twitterSession.getUserName() + "\nEmail Id : " +  result.data);*/
                fetchTwitterImage();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(RegisterActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getToken() {

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        device_token = task.getResult().getToken();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.e("device_token", ""+ device_token);
                        //  Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }



    private void sentdevicetoken(final String type) {
        int status=iDonateSharedPreference.getNotificationstatus(getApplicationContext());
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("device_id", device_token);
        jsonObject1.addProperty("enable_notification", status);
        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        final String image_url = "";
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.device_update(jsonObject1);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "" + response.body());
                    try {
                        JSONObject jsonObject=new JSONObject(String.valueOf(response.body()));
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        String data=jsonObject.getString("data");
                        iDonateSharedPreference.settoken(getApplicationContext(),device_token);
                        if (status.equalsIgnoreCase("1")){
                            final JSONObject jsonObject2=new JSONObject(data);
                            if(type.equalsIgnoreCase("social")){
                                ChangeActivity.changeActivity(RegisterActivity.this, UpdateActivity.class);
                                finish();
                            }else {
                                finish();
                            }


                        }else {
                            // ConstantFunctions.showSnackbar(reg_email_et,message,ForgotActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }

    public void fetchTwitterImage() {
        //check if user is already authenticated or not
        if (getTwitterSession() != null) {

            //fetch twitter image with other information if user is already authenticated

            //initialize twitter api client
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

            //Link for Help : https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials

            //pass includeEmail : true if you want to fetch Email as well
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    Log.e(TAG,"User Id : " + user.id + "\nUser Name : " + user.name + "\nEmail Id : " + user.email + "\nScreen Name : " + user.screenName);

                    String imageProfileUrl = user.profileImageUrl;
                    Log.e(TAG, "Data : " + imageProfileUrl);
                    if(user.email == null){
                        gmailfacebookloginAPI(user.name, "", "twitter", user.profileImageUrl);
                    } else gmailfacebookloginAPI(user.name, user.email, "twitter", user.profileImageUrl);
                    //NOTE : User profile provided by twitter is very small in size i.e 48*48
                    //Link : https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners
                    //so if you want to get bigger size image then do the following:
                    imageProfileUrl = imageProfileUrl.replace("_normal", "");

                    ///load image using Picasso
                   /* Picasso.with(MainActivity.this)
                            .load(imageProfileUrl)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .into(userProfileImageView);*/
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(RegisterActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }
    private void defaultLoginTwitter() {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            Log.e(TAG, "getTwitterSession is null");

            //if user is not authenticated start authenticating
            twitter_login_btn.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {


                @Override
                public void success(Result<TwitterSession> result) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = result.data;

                    Log.e(TAG, "Success : "+result.data);

                    //call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession);

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    Toast.makeText(RegisterActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
            twitter_login_btn.performClick();
        } else {

            //if user is already authenticated direct call fetch twitter email api
//            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("acct", "" + acct);
            Log.e(TAG, "display name: " + acct.getDisplayName());
            try {
                String personName = acct.getDisplayName();
                String personPhotoUrl = "";
                try {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                } catch (Exception e) {
                    e.printStackTrace();

                }

                String email = acct.getEmail();
                Log.e(TAG, "Name: " + personName + ", email: " + email
                        + ", Image: " + "" + personPhotoUrl);
                String socialmedia = "email";
                gmailfacebookloginAPI(personName, email, socialmedia, personPhotoUrl);


            } catch (Exception e) {

                e.printStackTrace();
            }


        } else {

            // Signed out, show unauthenticated UI.

        }
    }

    private void gmailfacebookloginAPI(final String name, final String personName, final String socialmedia, final String image_url) {
        Log.e("socialmedia", "" + image_url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", name);
        jsonObject1.addProperty("email", personName);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(RegisterActivity.this));
        jsonObject1.addProperty("login_type", socialmedia);
        jsonObject1.addProperty("photo", image_url);
        jsonObject1.addProperty("terms", "");

        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.socialmedialogin(jsonObject1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();

                    Log.e(TAG, "" + response.body());
                    Log.e("response", "" + response);
                    iDonateSharedPreference.setsocialmedia(getApplicationContext(), socialmedia);
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String message = jsonObject.getString("message");
                        Log.e("response", "" + message);
                        if (jsonObject.getString("status").equals("1")) {
                            Log.e("socialmedia11", "" + socialmedia);
                            iDonateSharedPreference.setName(getApplicationContext(), name);
                            iDonateSharedPreference.setMail(getApplicationContext(), personName);
                            iDonateSharedPreference.setsocialmedia(getApplicationContext(), socialmedia);
                            iDonateSharedPreference.setSocialProfileimg(getApplicationContext(), image_url);
                            String data = jsonObject.getString("data");
                            Log.e("data232", "" + data);
                            JSONObject jsonObject1 = new JSONObject(data);
                            Log.e("data232", "" + jsonObject1);
                            iDonateSharedPreference.seteditprofile(getApplicationContext(), "2");
                            iDonateSharedPreference.setprofiledata(getApplicationContext(), data);
                            iDonateSharedPreference.setlogintype(getApplicationContext(),"sociallogin");
                            sessionManager.createLoginSession(jsonObject1.getString("user_id"), jsonObject1.getString("email"), jsonObject1.getString("name"), jsonObject1.getString("phone_number"), jsonObject1.getString("photo"), jsonObject1.getString("token"), jsonObject1.getString("business_name"), jsonObject1.getString("country"), jsonObject1.getString("gender"),jsonObject1.getString("type"));
                            user_id= Integer.parseInt(jsonObject1.getString("user_id"));
                            Log.e("response_name", "" + iDonateSharedPreference.getName(getApplicationContext()));
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            // iDonateSharedPreference.seteditprofile(getApplicationContext(), "0");
                            sentdevicetoken("social");
                        } else if (jsonObject.getString("status").equals("0")) {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            ConstantFunctions.showSnackbar(reg_name_et,message,RegisterActivity.this);
                            if (iDonateSharedPreference.getsocialMedia(getApplicationContext()).equalsIgnoreCase("email")) {
                                Log.e("data232email", "" + "email");
                                signOut();
                                // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else if (iDonateSharedPreference.getsocialMedia(getApplicationContext()).equalsIgnoreCase("facebook")) {
                                Log.e("data232facebook", "" + "facebook");
//                                FacebookSdk.sdkInitialize(getApplicationContext());
//                                LoginManager.getInstance().logOut();

                            }

                        }
                    } catch (JSONException e) {


                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG, "error" + t.toString());
                    if (iDonateSharedPreference.getsocialMedia(getApplicationContext()).equalsIgnoreCase("email")) {
                        Log.e("data232email", "" + "email");
                        signOut();
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (iDonateSharedPreference.getsocialMedia(getApplicationContext()).equalsIgnoreCase("facebook")) {
                        Log.e("data232facebook", "" + "facebook");
//                        FacebookSdk.sdkInitialize(getApplicationContext());
//                        LoginManager.getInstance().logOut();

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("logout", "logout");
                        ChangeActivity.changeActivity(RegisterActivity.this, RegisterActivity.class);
                        finish();
                    }
                });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//    }

//    private void getUserProfile(AccessToken currentAccessToken) {
//        GraphRequest request = GraphRequest.newMeRequest(
//                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.e("objectresponse", "" + object.toString());
//                        try {
//                            String personName = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String email = object.getString("email");
//                            String id = object.getString("id");
//                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//                            Log.e("image_url", "" + image_url);
//                            Log.e("first_name", "" + personName);
//                            String socialname = "facebook";
//                            gmailfacebookloginAPI(personName, email, socialname, image_url);
//
//                            //   txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            //  txtEmail.setText(email);
//                            // Picasso.with(MainActivity.this).load(image_url).into(imageView);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    private void radiobussi() {
        if (radio_btn_yes.isChecked()) {
            radi_business = "yes";
        } else if (radio_btn_no.isChecked()) {
            radi_business = "no";
        }else {

            radi_business = "";
        }
    }
    public static String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    private void RegisterAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String user_name = reg_name_et.getText().toString().trim();
        String user_password = reg_password_et.getText().toString().trim();
        String user_mobile = reg_mobile_et.getText().toString().trim();
        String user_email = reg_email_et.getText().toString().trim();
        if(type.equalsIgnoreCase("business")){
            business_name=business_reg_name_et.getText().toString();
        }
        else {
            business_name="";
        }
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", user_name);
        jsonObject1.addProperty("password", user_password);
        jsonObject1.addProperty("phone", user_mobile);
        jsonObject1.addProperty("email", user_email);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(RegisterActivity.this));
        jsonObject1.addProperty("gender", radi_gender);
        jsonObject1.addProperty("country", country_symbol);
        jsonObject1.addProperty("business_name", business_name);
        jsonObject1.addProperty("type", type);
        jsonObject1.addProperty("terms", "Yes");
        jsonObject1.addProperty("addr1", reg_address_et.getText().toString());
        jsonObject1.addProperty("addr2", reg_address_et1.getText().toString());
        jsonObject1.addProperty("state", state_spinner.getText().toString());
        jsonObject1.addProperty("city", city_spinner.getText().toString());

        jsonObject1.addProperty("zip", Zib_address_et.getText().toString());
        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        final String image_url = "";
        apiService = ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.newuserregister(jsonObject1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    Log.e(TAG, "" + response.body());
                    Log.e("success", "" + response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String message = jsonObject.getString("message");
                        if (jsonObject.getString("status").equals("1")) {
                            String data = jsonObject.getString("data");
                            Log.e("data232", "" + data);
                            JSONObject jsonObject1 = new JSONObject(data);
                            Log.e("data232", "" + jsonObject1);
                            // sessionManager.createLoginSession(jsonObject1.getString("user_id"), jsonObject1.getString("email"), jsonObject1.getString("name"), jsonObject1.getString("phone_number"),jsonObject1.getString("photo"),jsonObject1.getString("token"),jsonObject1.getString("status"),jsonObject1.getString("country"),jsonObject1.getString("gender"));
                            // iDonateSharedPreference.setprofiledata(getApplicationContext(), data);
                            // sessionManager.createLoginSession(jsonObject1.getString("user_id"), jsonObject1.getString("email"), jsonObject1.getString("name"),jsonObject1.getString("phone_number"));
                            //  iDonateSharedPreference.setSocialProfileimg(getApplicationContext(), image_url);
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            ChangeActivity.changeActivity(RegisterActivity.this, LoginActivity.class);
                            finish();
                        } else if (jsonObject.getString("status").equals("0")) {
                            ConstantFunctions.showSnackbar(reg_mobile_et,message,RegisterActivity.this);
                            // Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG, t.toString());
                    Log.e("Error", t.toString());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }

    private void postImageToServer_optional(Uri uri,Uri uri1,String Bname,String u_name,String mobiles,String emails,String d_ids,
                                   String countrys,String types,String termss,String add1,String add2,String state,String city,String zibcode) {

        RequestBody B_name = null;
        RequestBody uname = null;
        RequestBody mobile = null;
        RequestBody email = null;
        RequestBody d_id = null;
        RequestBody country = null;
        RequestBody type = null;
        RequestBody terms = null;
        RequestBody adds1 = null;
        RequestBody adds2 = null;
        RequestBody citys = null;
        RequestBody states = null;
        RequestBody zibcodes = null;

//        dialog=new ProgressDialog ( getActivity () );
        MultipartBody.Part body;
        MultipartBody.Part body1;


        if (TextUtils.isEmpty(uri.getPath())) {
            body = null;
            body1=null;


//            Toast.makeText(getActivity(), "test", LENGTH_SHORT).show();
        } else {
             file = new File(uri.getPath());
             file1 = new File(uri1.getPath());

            dialog = ProgressDialog.show(RegisterActivity.this, "",
                    "Uploading. Please wait...", true);

          //  Log.d("tag", "postImageToServer: " + uri.getPath());
            B_name = RequestBody.create(MediaType.parse("text/plain"), Bname);
            uname = RequestBody.create(MediaType.parse("text/plain"), u_name);
            mobile = RequestBody.create(MediaType.parse("text/plain"), mobiles);
            email = RequestBody.create(MediaType.parse("text/plain"), emails);
            d_id = RequestBody.create(MediaType.parse("text/plain"), d_ids);
            country = RequestBody.create(MediaType.parse("text/plain"), countrys);
            type = RequestBody.create(MediaType.parse("text/plain"), types);
            terms = RequestBody.create(MediaType.parse("text/plain"), termss);
            adds1 = RequestBody.create(MediaType.parse("text/plain"), add1);
            adds2 = RequestBody.create(MediaType.parse("text/plain"), add2);
            states = RequestBody.create(MediaType.parse("text/plain"), state);
            citys = RequestBody.create(MediaType.parse("text/plain"), city);
            zibcodes = RequestBody.create(MediaType.parse("text/plain"), zibcode);


            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("incorp", file.getName(), requestFile);

            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            body1 = MultipartBody.Part.createFormData("tax", file1.getName(), requestFile1);
        }
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CommonResponseModel> call = apiService.Business_Register_optional(B_name, uname, mobile, email, d_id,
                country,type,terms,adds1,adds2,states,citys,zibcodes,body,body1);
        call.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                try {

                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(response.body());
                        Log.e("Business_Responce", jsonInString);
                        CommonResponseModel commonResponseModel = gson.fromJson(jsonInString, CommonResponseModel.class);
                        if (commonResponseModel.status==1) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog();
                                }
                            }, 1000);
                        }
                        else {
//                            Toast.makeText(RegisterActivity.this, ""+commonResponseModel.message, LENGTH_SHORT).show();
                        }
                        //showToast("Successfully Uploaded" + response.message());
                    }
                    Log.d("tag", "success " + response.code());
                } catch (Exception e) {
                    dialog.dismiss();
                    Log.e("error", e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                //showToast("Failed Uploading image" + " " + t.getMessage());
                Log.e("error_responce", t.getMessage());
                //     mElasticDownloadView.fail();
                Toast.makeText(RegisterActivity.this, "Failed..Try again", LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }


    private void postImageToServer(Uri uri,Uri uri1,Uri uri2,String Bname,String u_name,String mobiles,String emails,String d_ids,
                                            String countrys,String types,String termss,String add1,String add2,String state,String city,String zibcode) {

        RequestBody B_name = null;
        RequestBody uname = null;
        RequestBody mobile = null;
        RequestBody email = null;
        RequestBody d_id = null;
        RequestBody country = null;
        RequestBody type = null;
        RequestBody terms = null;
        RequestBody adds1 = null;
        RequestBody adds2 = null;
        RequestBody citys = null;
        RequestBody states = null;
        RequestBody zibcodes = null;

//        dialog=new ProgressDialog ( getActivity () );
        MultipartBody.Part body;
        MultipartBody.Part body1;
        MultipartBody.Part body2;

        if (TextUtils.isEmpty(uri.getPath())) {
            body = null;
            body1=null;
            body2=null;

//            Toast.makeText(getActivity(), "test", LENGTH_SHORT).show();
        } else {
            file = new File(uri.getPath());
            file1 = new File(uri1.getPath());
            file2= new File(uri2.getPath());

            dialog = ProgressDialog.show(RegisterActivity.this, "", "Uploading. Please wait...", true);

            //  Log.d("tag", "postImageToServer: " + uri.getPath());

            B_name = RequestBody.create(MediaType.parse("text/plain"), Bname);
            uname = RequestBody.create(MediaType.parse("text/plain"), u_name);


            mobile = RequestBody.create(MediaType.parse("text/plain"), mobiles);
            email = RequestBody.create(MediaType.parse("text/plain"), emails);


            d_id = RequestBody.create(MediaType.parse("text/plain"), d_ids);
            country = RequestBody.create(MediaType.parse("text/plain"), countrys);

            type = RequestBody.create(MediaType.parse("text/plain"), types);
            terms = RequestBody.create(MediaType.parse("text/plain"), termss);
            adds1 = RequestBody.create(MediaType.parse("text/plain"), add1);
            adds2 = RequestBody.create(MediaType.parse("text/plain"), add2);
            states = RequestBody.create(MediaType.parse("text/plain"), state);
            citys = RequestBody.create(MediaType.parse("text/plain"), city);
            zibcodes = RequestBody.create(MediaType.parse("text/plain"), zibcode);



            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("incorp", file.getName(), requestFile);

            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            body1 = MultipartBody.Part.createFormData("tax", file1.getName(), requestFile1);

            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            body2 = MultipartBody.Part.createFormData("dba",file2.getName(), requestFile2);

        }
        apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<CommonResponseModel> call = apiService.Business_Register(B_name, uname, mobile, email, d_id,
                country,type,terms,adds1,adds2,states,citys,zibcodes, body,body1,body2);
        call.enqueue(new Callback<CommonResponseModel>() {
            @Override
            public void onResponse(Call<CommonResponseModel> call, Response<CommonResponseModel> response) {
                try {

                    if (response.isSuccessful()) {

                        dialog.dismiss();

                        Gson gson = new Gson();

                        String jsonInString = gson.toJson(response.body());
                        Log.e("Business_Responce", jsonInString);
                        CommonResponseModel commonResponseModel = gson.fromJson(jsonInString, CommonResponseModel.class);
                        if (commonResponseModel.status==1) {
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "Business Register Success", LENGTH_SHORT).show();
                                    dialog();
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(RegisterActivity.this, ""+commonResponseModel.message, LENGTH_SHORT).show();
                        }


                        //showToast("Successfully Uploaded" + response.message());
                    }
                    Log.d("tag", "success " + response.code());
                } catch (Exception e) {
                    dialog.dismiss();
                    Log.e("error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponseModel> call, Throwable t) {
                //showToast("Failed Uploading image" + " " + t.getMessage());
                Log.e("error_responce", t.getMessage());
                //     mElasticDownloadView.fail();
                Toast.makeText(RegisterActivity.this, "Failed..Try again", LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }

    private void dialog( ){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.popup_registerpage, null);
        PopupWindow popupWindow=new PopupWindow();
         Button ok=view.findViewById(R.id.ok);
        popupWindow.setContentView(view);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void radiofn() {
        if (radio_btn_male.isChecked()) {
            radi_gender = "M";
        } else if (radio_btn_female.isChecked()) {
            radi_gender = "F";
        } else if (radio_btn_orthers.isChecked()) {
            radi_gender = "O";
        } else {

            radi_gender = "";
        }
    }

    @Override
    public void onBackPressed() {
        ChangeActivity.changeActivity(RegisterActivity.this, LoginActivity.class);
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

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}