package com.i2donate.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.i2donate.Adapter.GooglePlacesAutocompleteAdapter;
import com.i2donate.Adapter.InternationalGooglePlacesAutocompleteAdapter;
import com.i2donate.Model.ChangeActivity;
import com.i2donate.R;
import com.i2donate.Session.IDonateSharedPreference;
import com.i2donate.listeners.RecyclerItemClickListener;
import com.i2donate.utility.PlacesAPI;

import java.util.ArrayList;

public class PlaceSearchActivity extends AppCompatActivity {
    EditText autoCompleteEditView;
    RecyclerView placesRecyclerView;
    PlacesAPI mPlaceAPI;
    ImageView back;
    String data;
    IDonateSharedPreference iDonateSharedPreference;
    private GooglePlacesAutocompleteAdapter mAutoCompleteAdapter;
    InternationalGooglePlacesAutocompleteAdapter internationalGooglePlacesAutocompleteAdapter;
    ArrayList<String> placeDataList = null;
    String inputText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        autoCompleteEditView = (EditText) findViewById(R.id.autocomplete_places);
        placesRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        iDonateSharedPreference = new IDonateSharedPreference();
        mAutoCompleteAdapter = new GooglePlacesAutocompleteAdapter(this, R.layout.view_search_list);
        internationalGooglePlacesAutocompleteAdapter = new InternationalGooglePlacesAutocompleteAdapter(this, R.layout.view_search_list);
        back = (ImageView) findViewById(R.id.back_icon_autoplace_img);

        data = getIntent().getStringExtra("data");
        if (data.equalsIgnoreCase("2")){
            autoCompleteEditView.setHint(R.string.united_state_internationlocation);
            LinearLayoutManager placelinearLayoutManager = new LinearLayoutManager(PlaceSearchActivity.this,LinearLayoutManager.VERTICAL,false);
            placesRecyclerView.setLayoutManager(placelinearLayoutManager);
            placesRecyclerView.setAdapter(internationalGooglePlacesAutocompleteAdapter);
        }else if (data.equalsIgnoreCase("3")){
            autoCompleteEditView.setHint(R.string.united_state_internationlocation);
            LinearLayoutManager placelinearLayoutManager = new LinearLayoutManager(PlaceSearchActivity.this,LinearLayoutManager.VERTICAL,false);
            placesRecyclerView.setLayoutManager(placelinearLayoutManager);
            placesRecyclerView.setAdapter(internationalGooglePlacesAutocompleteAdapter);
        }else {
            autoCompleteEditView.setHint(R.string.united_state_location);
            LinearLayoutManager placelinearLayoutManager = new LinearLayoutManager(PlaceSearchActivity.this,LinearLayoutManager.VERTICAL,false);
            placesRecyclerView.setLayoutManager(placelinearLayoutManager);
            placesRecyclerView.setAdapter(mAutoCompleteAdapter);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        autoCompleteEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")){
                    if (data.equalsIgnoreCase("2")){
                        if(s.length()>=3){
                            internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());
                        }else if (s.length()==0){
                            internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());
                        }

                    }else if (data.equalsIgnoreCase("3")){
                        if(s.length()>=3){
                            internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());
                        }else if (s.length()==0){
                            internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());
                        }

                    }else {
                        if(s.length()>=3){
                            mAutoCompleteAdapter.getFilter().filter(s.toString());
                        }else  if(s.length()==0){
                            mAutoCompleteAdapter.getFilter().filter(s.toString());
                        }

                    }
                }else {
                    if (data.equalsIgnoreCase("2")) {

                            internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());

                    } else if (data.equalsIgnoreCase("3")) {

                        internationalGooglePlacesAutocompleteAdapter.getFilter().filter(s.toString());

                    } else{
                            mAutoCompleteAdapter.getFilter().filter(s.toString());

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                /*inputText = autoCompleteEditView.getText().toString().trim();
                LinearLayoutManager placelinearLayoutManager = new LinearLayoutManager(PlaceSearchActivity.this,LinearLayoutManager.VERTICAL,false);
                placesRecyclerView.setLayoutManager(placelinearLayoutManager);
                placesRecyclerView.setAdapter(mAutoCompleteAdapter);*/

                /*DownloadNewTask downloadTask = new DownloadNewTask();
                //Start  downloading json data from Google Directions API
                downloadTask.execute(inputText);*/
            }
        });

        placesRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    final String item;
                    if (data.equalsIgnoreCase("2")) {
                      item = internationalGooglePlacesAutocompleteAdapter.getItem(position).trim();
                    }else if (data.equalsIgnoreCase("3")) {
                        item = internationalGooglePlacesAutocompleteAdapter.getItem(position).trim();
                    }else {
                        item = mAutoCompleteAdapter.getItem(position).trim();
                    }

                    iDonateSharedPreference.setLocation(getApplicationContext(), item);
                    Log.e("PlaceSearchActivity : ", item);
                    if(data.equalsIgnoreCase("1")){
                        ChangeActivity.changeActivityData(PlaceSearchActivity.this, UnitedStateActivity.class,"1");
                        finish();
                    } if(data.equalsIgnoreCase("2")){
                        ChangeActivity.changeActivityData(PlaceSearchActivity.this, InternationalCharitiesActivity.class,"1");
                        finish();
                    } else if(data.equalsIgnoreCase("3")){
                        ChangeActivity.changeActivityData(PlaceSearchActivity.this, InternationalCharitiesActivity.class,"3");
                        finish();
                    } else {
                        finish();
                    }
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }));

    }

    class DownloadNewTask extends AsyncTask<String, String, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... url) {

            // For storing data from web service
            ArrayList<String> data = null;

            try {
                // Fetching the data from web service
                mPlaceAPI = new PlacesAPI();
                data = mPlaceAPI.autocomplete(inputText);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("GooglePlaceAPI", String.valueOf(data));
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            Log.e("Result", result.toString());

           /* mAdapter = new PlacesAutoCompleteAdapter1(PlaceSearchActivity.this, result);
            placesRecyclerView.setAdapter(mAdapter);*/

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(data.equalsIgnoreCase("1")){
            ChangeActivity.changeActivityData(PlaceSearchActivity.this, UnitedStateActivity.class,"1");
            finish();
        } if(data.equalsIgnoreCase("2")){
            ChangeActivity.changeActivityData(PlaceSearchActivity.this, InternationalCharitiesActivity.class,"1");
            finish();
        } else {
            finish();
        }
    }
}
