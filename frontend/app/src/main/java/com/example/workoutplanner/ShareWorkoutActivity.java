package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ShareWorkoutActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private EditText postNameEditText;
    private Spinner genderSpinner;
    private Spinner difficultySpinner;
    private Long workoutPlanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_workout);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        postNameEditText = findViewById(R.id.post_name);
        genderSpinner = findViewById(R.id.post_gender_spinner);
        difficultySpinner = findViewById(R.id.post_difficulty_spinner);

        instantiateSpinners();
        initializeExtras();
    }

    private void instantiateSpinners() {
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
    }

    private void initializeExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutPlanId = extras.getLong("workoutPlanId");
        }
    }

    public void shareWorkout(View view){
        updateSelectedWorkoutPlan();
        duplicatePost();
    }

    private void updateSelectedWorkoutPlan() {
        String url = "http://10.0.2.2:8080/api/v1/workoutPlans/" + workoutPlanId;
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("gender", genderSpinner.getSelectedItem().toString());
            requestBody.put("difficulty", difficultySpinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(ShareWorkoutActivity.this, "Workout plan updated successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String errorResponse = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errorObject = new JSONObject(errorResponse);
                        if (errorObject.has("message") && errorObject.getString("message").equals("no data changes found")) {
                            showToastLong(ShareWorkoutActivity.this, "Workout plan updated successfully");
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    showToastLong(ShareWorkoutActivity.this, "Error updating workout plan: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void duplicatePost() {
        String url = "http://10.0.2.2:8080/api/v1/customers/1/workoutPlans/" + workoutPlanId;

        JSONObject requestBody = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(ShareWorkoutActivity.this, "Workout plan duplicated successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(ShareWorkoutActivity.this, "Error duplicating workout plan: " + error.getMessage());
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void createPost(){

    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}