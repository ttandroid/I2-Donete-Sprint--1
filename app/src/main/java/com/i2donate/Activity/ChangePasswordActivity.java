package com.i2donate.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.i2donate.Commonmethod.ConstantFunctions;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;
import com.i2donate.RetrofitAPI.ApiClient;
import com.i2donate.RetrofitAPI.ApiInterface;
import com.i2donate.Session.SessionManager;
import com.i2donate.Validation.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = ChangePasswordActivity.class.getSimpleName();
    @BindView(R.id.old_password_layout_input)
    TextInputLayout old_password_layout_input;
    @BindView(R.id.new_password_layout_input)
    TextInputLayout new_password_layout_input;
    EditText old_password, new_password;
    Button submit_btn;
    ImageView back_icon_change_img;
    ApiInterface apiService;
    static SessionManager session;
    static HashMap<String, String> userDetails;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        listioner();
    }


    private void init() {
        session = new SessionManager(getApplicationContext());
        old_password_layout_input = (TextInputLayout) findViewById(R.id.old_password_layout_input);
        new_password_layout_input = (TextInputLayout) findViewById(R.id.new_password_layout_input);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        back_icon_change_img = (ImageView) findViewById(R.id.back_icon_change_img);
        builder = new AlertDialog.Builder(this);

        AlertreChangepasword("", "", "");
    }

    private void listioner() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!old_password.getText().toString().trim().matches(Validation.PASSWORD_PATTERN)) {
                    ConstantFunctions.showSnackbar(old_password, "Enter correct new password", ChangePasswordActivity.this);
                } else if (!new_password.getText().toString().equals(old_password.getText().toString())){
                    ConstantFunctions.showSnackbar(old_password, "Password is not match", ChangePasswordActivity.this);
                }else {
                    changepasswordAPI();
                }
            }
        });
        old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().startsWith(" ")) {
                    old_password.setText("");
                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
                    old_password_layout_input.setError("Required new password");
                    //disableButton(...)
                } else {
                    if (old_password.getText().toString().trim().length() <= 0) {
                        old_password_layout_input.setError("Required new password");
                    } else {
                        if (old_password.getText().toString().trim().matches(Validation.PASSWORD_PATTERN)) {
                            old_password_layout_input.setError("");
                        } else {
                            old_password_layout_input.setError("Required new password");
                        }
                    }
                    //Toast.makeText(getApplicationContext(), " allowed", Toast.LENGTH_LONG).show();
                    //enableButton(...)
                }
                if (!new_password.getText().toString().equals(old_password.getText().toString())) {
                    new_password_layout_input.setError("Password is not match");
                } else {
                    new_password_layout_input.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passwordvalidation = s.toString();
                if (passwordvalidation.length() >= 8 && passwordvalidation.matches(Validation.PASSWORD_PATTERN)) {
                    old_password_layout_input.setError("");
                } else {
                    old_password_layout_input.setError("Password is invalid");
                }
            }
        });
        /*new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (old_password.getText().toString().trim().length() <= 0) {
                    old_password_layout_input.setError("Required old password");
                } else {
                    if (old_password.getText().toString().trim().matches(Validation.PASSWORD_PATTERN)) {
                        old_password_layout_input.setError("");
                    } else {
                        old_password_layout_input.setError("Required old password");
                    }
                }
                return false;
            }
        });*/

        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                /*if (charSequence.toString().startsWith(" ")) {
                    new_password.setText("");
                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
                    new_password_layout_input.setError("Required new password");

                } else {
                    new_password_layout_input.setError("");
                    if (old_password.getText().toString().trim().length() <= 0) {
                        old_password_layout_input.setError("Required old password");
                    } else {
                        if (old_password.getText().toString().trim().matches(Validation.PASSWORD_PATTERN)) {
                            old_password_layout_input.setError("");
                        } else {
                            old_password_layout_input.setError("Required old password");
                        }
                    }

                }*/

                if (charSequence.toString().startsWith(" ")) {
                    new_password.setText("");
                    Toast.makeText(getApplicationContext(), "Space Not allowed", Toast.LENGTH_LONG).show();
                    new_password_layout_input.setError("Password is not match");

                } else {
                    new_password_layout_input.setError("");
                    if (!new_password.getText().toString().equals(old_password.getText().toString())) {
                        new_password_layout_input.setError("Password is not match");
                    } else {
                        new_password_layout_input.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*String passwordvalidation = s.toString();
                if (passwordvalidation.length() >= 8 && passwordvalidation.matches(Validation.PASSWORD_PATTERN)) {
                    new_password_layout_input.setError("");
                } else {
                    new_password_layout_input.setError("Password is invalid");
                }*/
            }
        });

        back_icon_change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public static String getDeviceUniqueID(Activity activity) {
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }


    private void AlertreChangepasword(final String position, final String title, final String alert) {
        final Dialog dialog = new Dialog(ChangePasswordActivity.this);
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

                //ChangeActivity.clearAllPreviousActivity(ChangePasswordActivity.this, ChangePasswordActivity.class);
                dialog.dismiss();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // LoginAPI("business");
                ChangeActivity.clearAllPreviousActivity(ChangePasswordActivity.this, BrowseActivity.class);

                dialog.dismiss();

            }
        });

    }




    private void changepasswordAPI() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        userDetails = session.getUserDetails();
        Log.e("userDetails", "" + userDetails);
        Log.e("KEY_UID", "" + userDetails.get(SessionManager.KEY_UID));
        String user_id = "";
        String token = "";

        if (session.isLoggedIn()) {
            user_id = userDetails.get(SessionManager.KEY_UID);
            token = userDetails.get(SessionManager.KEY_token);
        }
        String old_password1 = old_password.getText().toString().trim();
        String new_password1 = new_password.getText().toString().trim();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("user_id", user_id);
        jsonObject1.addProperty("token", token);
        jsonObject1.addProperty("password", old_password1);
//        jsonObject1.addProperty("new_password", new_password1);
        jsonObject1.addProperty("device_id", getDeviceUniqueID(ChangePasswordActivity.this));
        Log.e("jsonObject1", "" + jsonObject1);
        /*   ApiInterface jsonPostService = ApiClient.createService(ApiInterface.class, "http://project975.website/i2-donate/api/");*/
        final String image_url = "";
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        try {
            Call<JsonObject> call = apiService.change_password(jsonObject1);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressDialog.dismiss();
                    Log.e(TAG, "" + response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equalsIgnoreCase("1")) {
                            ChangeActivity.clearAllPreviousActivity(ChangePasswordActivity.this, BrowseActivity.class);
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            ConstantFunctions.showSnackbar(old_password, message, ChangePasswordActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG, t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }

    private void dailogue() {
        builder.setMessage(R.string.dialog_message);

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.dialog_message_mysetting)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeActivity.clearAllPreviousActivity(ChangePasswordActivity.this, BrowseActivity.class);
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
        dailogue();
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
