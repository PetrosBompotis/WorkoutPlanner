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
import com.example.workoutplanner.userActivity.UserActivity;

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
    private Long workoutPlanId, duplicatedWorkoutPlanId;

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
    }

    private void updateSelectedWorkoutPlan() {
        String url = "http://10.0.2.2:8080/api/v1/workoutPlans/" + workoutPlanId;
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        String gender = genderSpinner.getSelectedItem().toString();
        String difficulty = difficultySpinner.getSelectedItem().toString();

        if (gender.equals("any") || difficulty.equals("any")){
            showToastLong(ShareWorkoutActivity.this, "Please select both gender and difficulty");
            return;
        }

        String postName = postNameEditText.getText().toString();
        if (postName.isEmpty()){
            showToastLong(ShareWorkoutActivity.this, "Please enter a post name");
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("gender", gender);
            requestBody.put("difficulty", difficulty);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showToastLong(ShareWorkoutActivity.this, "Workout plan updated successfully");
                        duplicatePost();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String errorResponse = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errorObject = new JSONObject(errorResponse);
                        if (errorObject.has("message") && errorObject.getString("message").equals("no data changes found")) {
                            //showToastLong(ShareWorkoutActivity.this, "Workout plan updated successfully");
                            duplicatePost();
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
                        try {
                            duplicatedWorkoutPlanId = response.getLong("id");
                            //showToastLong(ShareWorkoutActivity.this, "Workout id: " + duplicatedWorkoutPlanId);
                            createPost(duplicatedWorkoutPlanId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //showToastLong(ShareWorkoutActivity.this, "Workout plan duplicated successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(ShareWorkoutActivity.this, "Error duplicating workout plan: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void createPost(Long duplicatedWorkoutPlanId){
        long customerId = sharedPreferences.getLong("id", 1);
        String name = sharedPreferences.getString("name", "name");
        String url = "http://10.0.2.2:8080/api/v1/customers/" + customerId + "/workoutPlans/" + duplicatedWorkoutPlanId + "/posts";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("postName", postNameEditText.getText().toString());
            requestBody.put("createdBy", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(ShareWorkoutActivity.this, "Post created successfully");
                        Intent intent = new Intent(ShareWorkoutActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(ShareWorkoutActivity.this, "Error creating post: " + error.getMessage());
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

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}