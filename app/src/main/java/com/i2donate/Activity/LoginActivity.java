package com.i2donate.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

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
import com.google.gson.JsonObject;
import com.i2donate.Commonmethod.ConstantFunctions;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.Session.SessionManager;
import com.i2donate.Validation.Validation;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    //CallbackManager callbackManager;
    CoordinatorLayout coordinator_layout;
    //LoginButton facebook_login_btn;
    TwitterLoginButton twitter_login_btn;
    Button login_btn;
    TextView register_btn_tv;
    ApiInterface apiService;
    TextInputLayout email_layout_input, password_layout_input;
    EditText login_username, login_password;
    ImageView back_icon_login_img, google_sign_btn, facebook_login, twitter_login;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;
    IDonateSharedPreference iDonateSharedPreference;
    SessionManager sessionManager;
    private final String PACKAGE = "com.i2donate";
    TextView forgot_btn_tv;
    private TwitterAuthClient client;
    String device_token;
    int user_id;
    private final int STORAGE_PERMISSION_CODE = 1;
    /*private final String host = "api.linkedin.com";
    private final String topCardUrl = "https://" + host + "/v1/people/~:(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_API_key), getResources().getString(R.string.twitter_secret_key)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
        //finally initialize twitter with created configs
        Twitter.initialize(config);
        setContentView(R.layout.activity_login);
        //  getWindow().setBackgroundDrawableResource(R.drawable.dashbord_background);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sessionManager = new SessionManager(getApplicationContext());

        init();
        listioner();
        /*generateHashkey();facebook_login*/

      /*  if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestStoragePermission();
        }*/

        String profileUrl = "https://api.linkedin.com/v2/me";

        // Access token for the r_liteprofile permission
//        String accessToken = "vZn+i17vbXU1IpoP9Vw7xeyvsD4=";
        String accessToken = "7w1b1EG4ZAFTlnxpf0AlsjB/RSE=";
/*
        try {
            javax.json.JsonObject profileData = ProfileExample.sendGetRequest(profileUrl, accessToken);
            System.out.println(profileData.toString());
            Log.e("profileData", "" + profileData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission Alert");
            builder.setMessage("Camera permission is required to in order to provide photo capture features in profile update and donation pages.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),
                            android.R.string.no, Toast.LENGTH_SHORT).show();
                }
            });

            builder.setCancelable(false);
            builder.show();

          /*  new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();*/
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission Alert");
            builder.setMessage("Camera permission is required to in order to provide photo capture features in profile update and donation pages.");
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),
                                    android.R.string.no, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        iDonateSharedPreference = new IDonateSharedPreference();
        login_btn = (Button) findViewById(R.id.login_btn);
        register_btn_tv = (TextView) findViewById(R.id.register_btn_tv);
        back_icon_login_img = (ImageView) findViewById(R.id.back_icon_login_img);
        google_sign_btn = (ImageView) findViewById(R.id.google_sign_btn);
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        //facebook_login_btn = (LoginButton) findViewById(R.id.facebook_login_btn);
        email_layout_input = (TextInputLayout) findViewById(R.id.email_layout_input);
        twitter_login = (ImageView) findViewById(R.id.twitter_login);
        twitter_login_btn = (TwitterLoginButton) findViewById(R.id.twitter_login_btn);
        password_layout_input = (TextInputLayout) findViewById(R.id.password_layout_input);
        forgot_btn_tv = (TextView) findViewById(R.id.forgot_btn_tv);
        mProgressDialog = new ProgressDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        LoginManager.getInstance().logOut();

        getToken();
        /*Facebook login*/

        //   boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

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
//                getUserProfile(AccessToken.getCurrentAccessToken());
//                Log.e("click12", "click");
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

    private void listioner() {

        login_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().startsWith(" ")) {
                    login_username.setText("");
                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
                    email_layout_input.setError("Required email/phone number");
                    //disableButton(...)
                } else {
                    if (login_username.getText().toString().trim().length() <= 9) {
                        email_layout_input.setError("Required email/phone number");
                    } else {
                        email_layout_input.setError("");
                    }
                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
                    //enableButton(...)
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*login_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (login_username.getText().toString().trim().length() <= 0) {
                    email_layout_input.setError("Required password");
                } else {
                    if (login_username.getText().toString().trim().matches(Validation.emailPattern)) {
                        email_layout_input.setError("");
                    } else {
                        email_layout_input.setError("Required email");
                    }
                }
                return false;
            }
        });
*/
        login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().startsWith(" ")) {
                    login_password.setText("");
                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
                    password_layout_input.setError("Required password");
                } else {
                    password_layout_input.setError("");
                    if (login_password.getText().toString().trim().length() <= 0) {
                        password_layout_input.setError("Required password");
                    }/* else {
                        if (login_username.getText().toString().trim().matches(Validation.emailPattern)) {
                            email_layout_input.setError("");
                        } else {
                            email_layout_input.setError("Required email");
                        }
                    }*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordvalidation = s.toString();
                if (passwordvalidation.length() >= 8 /*&& passwordvalidation.matches(Validation.PASSWORD_PATTERN)*/) {
                    password_layout_input.setError("");
                } else {
                    password_layout_input.setError("Password is invalid");
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(login_username) && !isEmpty(login_password)) {
                    Log.i("TESTNAME", "TESTNAME" + "1");
                    if (login_username.getText().toString().trim().matches(Validation.emailPattern) || login_username.getText().toString().trim().matches(Validation.mobilepattern)||login_username.getText().toString().trim().length()>9) {
                        Log.i("TESTNAME", "TESTNAME" + "2");
                        if (login_password.getText().toString() != null) {
                            Log.i("TESTNAME", "TESTNAME" + "3");
                            if (isOnline()) {

                                LoginAPI("");

                            } else {

                                ConstantFunctions.showSnackbar(login_username, "Please check internet connection", LoginActivity.this);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Enter correct password", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "Enter correct mail", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        register_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity.changeActivity(LoginActivity.this, RegisterActivity.class);
                finish();
            }
        });
        back_icon_login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        forgot_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivity.changeActivity(LoginActivity.this, ForgotActivity.class);
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

        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    defaultLoginTwitter();
                    Log.e("click", "click243");
                } else {
                    //Toast.makeText(LoginActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    ConstantFunctions.showSnakBar("Please check internet connection", v);
                }
            }
        });


    }


    private void Alertrefund(final String position, final String title, final String alert) {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.refund_alert_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final String title1 = title;
        final String alert1 = alert;
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_username = (TextView) dialog.findViewById(R.id.tv_username);
        TextView estimate_amounts = (TextView) dialog.findViewById(R.id.estimate_amounts);
        tv_username.setAllCaps(false);
        tv_no.setAllCaps(false);
        tv_yes.setAllCaps(false);
//        tv_no.setText(db.getvalue("chancel"));
//        tv_yes.setText(db.getvalue("upload"));
//        tv_username.setText(db.getvalue("please_upload_pop"));
//        estimate_amounts.setText(db.getvalue("pop_delivery"));


        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                typeofimage  =0;
                //  images/dispatch_proof
                //     startTripViewModel.uploadpohoto(0);

                LoginAPI("individual");


                dialog.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginAPI("business");
                dialog.dismiss();

            }
        });

    }


    private void AlertreChangepasword(final String position, final String title, final String alert) {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cahnge_pasword_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final String title1 = title;
        final String alert1 = alert;
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_username = (TextView) dialog.findViewById(R.id.tv_username);
        TextView estimate_amounts = (TextView) dialog.findViewById(R.id.estimate_amounts);
        tv_username.setAllCaps(false);
        tv_no.setAllCaps(false);
        tv_yes.setAllCaps(false);
//        tv_no.setText(db.getvalue("chancel"));
//        tv_yes.setText(db.getvalue("upload"));
//        tv_username.setText(db.getvalue("please_upload_pop"));
//        estimate_amounts.setText(db.getvalue("pop_delivery"));


        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                typeofimage  =0;
                //  images/dispatch_proof
                //     startTripViewModel.uploadpohoto(0);

                //LoginAPI( "individual");

                ChangeActivity.clearAllPreviousActivity(LoginActivity.this, ChangePasswordActivity.class);
                dialog.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // LoginAPI("business");
                ChangeActivity.clearAllPreviousActivity(LoginActivity.this, BrowseActivity.class);

                dialog.dismiss();

            }
        });

    }


    private void defaultLoginTwitter() {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            Log.e(TAG, "getTwitterSession is null");

            //if user is not authenticated start authenticating
            twitter_login_btn.setCallback(new Callback<TwitterSession>() {


                @Override
                public void success(Result<TwitterSession> result) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = result.data;

                    Log.e(TAG, "Success : " + result.data);

                    //call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession);

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
            twitter_login_btn.performClick();
        } else {

            //if user is already authenticated direct call fetch twitter email api
//            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }

    }
    /*public void customLoginTwitter(View view) {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            client.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = result.data;

                    //call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession);
                }

                @Override
                public void failure(TwitterException e) {
                    // Do something on failure
                    Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is already authenticated direct call fetch twitter email api
            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }
    }
*/

    /**
     * Before using this feature, ensure that “Request email addresses from users” is checked for your Twitter app.
     *
     * @param twitterSession user logged in twitter session
     */
    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
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
                Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * call Verify Credentials API when Twitter Auth is successful else it will go in exception block
     * this metod will provide you User model which contain all user information
     */
    public void fetchTwitterImage() {
        //check if user is already authenticated or not
        if (getTwitterSession() != null) {
            //fetch twitter image with other information if user is already authenticated

            //initialize twitter api client
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

            //Link for Help : https://developer.twitter.com/en/docs/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials

            //pass includeEmail : true if you want to fetch Email as well
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    Log.e(TAG, "User Id : " + user.id + "\nUser Name : " + user.name + "\nEmail Id : " + user.email + "\nScreen Name : " + user.screenName);

                    String imageProfileUrl = user.profileImageUrl;
                    Log.e(TAG, "Data : " + imageProfileUrl);
                    if (user.email == null) {
                        gmailfacebookloginAPI(user.name, "", "twitter", user.profileImageUrl);
                    } else
                        gmailfacebookloginAPI(user.name, user.email, "twitter", user.profileImageUrl);
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
                    Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * get authenticates user session
     *
     * @return twitter session
     */
    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                //___________set package name
                //tvPackage.setText(PACKAGE);

                //___________set keyhash
                //tvKeyHash.setText(Base64.encodeToString(md.digest(), Base64.NO_WRAP));

                //___________add package name and key hash into log
                Log.e(TAG, "_____________________________________________");
                Log.e("Package", PACKAGE);
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                Log.e(TAG, "_____________________________________________");

                Toast.makeText(this, "Check your logcate for keyhash", Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public static String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    private void LoginAPI(final String type) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String user_name = login_username.getText().toString().trim();
        String user_password = login_password.getText().toString().trim();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("email", user_name);
        jsonObject1.addProperty("password", user_password);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(LoginActivity.this));

        if (!type.equals("")) {
            jsonObject1.addProperty("type", type);
        }

        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        final String image_url = "";
        apiService = ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.userLogin(jsonObject1);
            Log.i("TESTSTATE", "TESTSTATE" + "Loginbefore");
            call.enqueue(new retrofit2.Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.i("TESTSTATE", "TESTSTATE" + "Loginbefore  1");
                    progressDialog.dismiss();
                    Log.e(TAG, "" + response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        Log.e(TAG, "123" + jsonObject);
                        String message = jsonObject.getString("message");
                        String first = "";
                        Log.e(TAG, "" + message);
                        if (jsonObject.getString("status").equals("1")) {
                            if (message.equals("Multiple")) {
                                Alertrefund("", "", "");
                            } else {
                                String data = jsonObject.getString("data");
                                Log.e("data232", "" + data);
                                JSONObject jsonObject1 = new JSONObject(data);
                                Log.e("data232", "" + jsonObject1);
                                user_id = Integer.parseInt(jsonObject1.getString("user_id"));

                                first = jsonObject1.getString("first_time_login");
                                String type = jsonObject1.getString("type");
                                sessionManager.createLoginSession(jsonObject1.getString("user_id"), jsonObject1.getString("email"), jsonObject1.getString("name"), jsonObject1.getString("phone_number"), jsonObject1.getString("photo"), jsonObject1.getString("token"), jsonObject1.getString("business_name"), jsonObject1.getString("country"), jsonObject1.getString("gender"), jsonObject1.getString("type"));

                                if (first.equals("0") && type.equals("business")) {

                                    ChangeActivity.clearAllPreviousActivity(LoginActivity.this, ChangePasswordActivity.class);

//                                    AlertreChangepasword("", "", "");
                                } else {
                                    iDonateSharedPreference.setprofiledata(getApplicationContext(), data);
                                    Log.e("updatedata", "" + iDonateSharedPreference.getprofiledata(getApplicationContext()));
                                    iDonateSharedPreference.setSocialProfileimg(getApplicationContext(), image_url);
                                    iDonateSharedPreference.setlogintype(getApplicationContext(), "registerlogin");
                                    ChangeActivity.clearAllPreviousActivity(LoginActivity.this, BrowseActivity.class);
                                    sentdevicetoken("login");
                                    // iDonateSharedPreference.seteditprofile(getApplicationContext(), "0");
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    JSONArray jsonArray = jsonObject1.getJSONArray("type");
                                    String types = jsonArray.getString(0);
                                    //  c

                                }
                            }

                        } else if (jsonObject.getString("status").equals("0")) {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            ConstantFunctions.showSnackbar(login_username, message, LoginActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i("TESTSTATE", "TESTSTATE" + "Loginbefore  onFailure" + t.toString());
                    progressDialog.dismiss();
                    Log.e(TAG, t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }

    private void GetKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("keyhash", "keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
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
                        Log.e("device_token", "" + device_token);
                        //  Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sentdevicetoken(final String type) {
        int status = iDonateSharedPreference.getNotificationstatus(getApplicationContext());
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
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        iDonateSharedPreference.settoken(getApplicationContext(), device_token);
                        if (status.equalsIgnoreCase("1")) {
                            final JSONObject jsonObject2 = new JSONObject(data);
                            if (type.equalsIgnoreCase("social")) {
                                ChangeActivity.changeActivity(LoginActivity.this, UpdateActivity.class);
                                finish();
                            } else {
                                finish();
                            }
                        } else {
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

    private void gmailfacebookloginAPI(final String name, final String personName, final String socialmedia, final String image_url) {
        Log.e("socialmedia", "" + image_url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name", name);
        jsonObject1.addProperty("email", personName);
        jsonObject1.addProperty("login_type", socialmedia);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(LoginActivity.this));
        jsonObject1.addProperty("photo", image_url);
        jsonObject1.addProperty("terms", "");

        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.socialmedialogin(jsonObject1);
            call.enqueue(new retrofit2.Callback<JsonObject>() {
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
                            user_id = Integer.parseInt(jsonObject1.getString("user_id"));
                            iDonateSharedPreference.seteditprofile(getApplicationContext(), "2");
                            iDonateSharedPreference.setprofiledata(getApplicationContext(), data);
                            iDonateSharedPreference.setlogintype(getApplicationContext(), "sociallogin");
                            sessionManager.createLoginSession(jsonObject1.getString("user_id"), jsonObject1.getString("email"), jsonObject1.getString("name"), jsonObject1.getString("phone_number"), jsonObject1.getString("photo"), jsonObject1.getString("token"), jsonObject1.getString("business_name"), jsonObject1.getString("country"), jsonObject1.getString("gender"), jsonObject1.getString("type"));
                            Log.e("response_name", "" + iDonateSharedPreference.getName(getApplicationContext()));
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            sentdevicetoken("social");
                            // iDonateSharedPreference.seteditprofile(getApplicationContext(), "0");
                          /*  ChangeActivity.changeActivity(LoginActivity.this, UpdateActivity.class);
                            finish();*/
                        } else if (jsonObject.getString("status").equals("0")) {
                            // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            ConstantFunctions.showSnackbar(login_username, message, LoginActivity.this);
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
                        ChangeActivity.changeActivity(LoginActivity.this, LoginActivity.class);
                        finish();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.e(TAG, "Got cached sign-in1");
        }

        // Pass the activity result to the twitterAuthClient.
        if (client != null)
            client.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitter_login_btn.onActivityResult(requestCode, resultCode, data);
    }


    /*@Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            Log.e(TAG, "Got cached sign-in1");
        } else {

            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                    Log.e(TAG, "Got cached sign-in1");

                }
            });
        }
    }*/
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
//                            Toast.makeText(LoginActivity.this, "Please give email permission", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {

            mProgressDialog.setMessage(getString(R.string.login_loading));
            //mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    boolean isEmpty(EditText text) {
        CharSequence charSequence = text.getText().toString();
        return TextUtils.isEmpty(charSequence);
    }

    @Override
    public void onBackPressed() {
        iDonateSharedPreference.setlogintype(getApplicationContext(), "non");
        //  ChangeActivity.changeActivity(LoginActivity.this, BrowseActivity.class);
        finish();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
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
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}