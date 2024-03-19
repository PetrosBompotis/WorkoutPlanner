package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseDetailActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    Button exerciseDoneButton, exerciseDeleteButton, addSetButton;
    TextView muscleTextView, setTextView;
    ImageView exerciseGifImageView;
    RecyclerView setRecyclerView;
    EditText instructionsEditText, exerciseNameEditText;
    Long routineId, exerciseId;
    String exerciseName, muscle, equipment, gifUrl, instructions;
    Boolean isNew;
    private List<Set> setList;
    private SetAdapter setAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        exerciseDoneButton = findViewById(R.id.exerciseDoneButton);
        exerciseDeleteButton = findViewById(R.id.exerciseDeleteButton);
        exerciseNameEditText = findViewById(R.id.exerciseNameEditText);
        muscleTextView = findViewById(R.id.muscleTextView2);
        exerciseGifImageView = findViewById(R.id.exerciseGifImageView);
        setTextView = findViewById(R.id.setTextView);
        setRecyclerView = findViewById(R.id.setRecyclerView);
        setRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addSetButton = findViewById(R.id.addSetButton);
        instructionsEditText = findViewById(R.id.instructionsEditText);

        setUpListeners();
        initializeExtras();
        loadSets();
    }

    private void initializeExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            exerciseName = extras.getString("exerciseName");
            muscle = extras.getString("muscle");
            equipment = extras.getString("equipment");
            gifUrl = extras.getString("gifUrl");
            routineId = extras.getLong("routineId");
            exerciseId = extras.getLong("exerciseId");
            instructions = extras.getString("instructions");
            isNew = extras.getBoolean("isNew");

            if (isNew){
                exerciseDeleteButton.setVisibility(View.GONE);
                addSetButton.setVisibility(View.GONE);
            }

            exerciseNameEditText.setText(exerciseName);
            muscleTextView.setText(muscle);
            Glide.with(this).load(gifUrl).into(exerciseGifImageView);
            instructionsEditText.setText(instructions);
        }
    }

    private void setUpListeners() {
        exerciseDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNew){
                    createNewExercise();
                }else {
                    updateSelectedExercise();
                }
            }
        });

        exerciseDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteExercise();
            }
        });

        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSet();
            }
        });
    }

    private void createNewExercise(){
        String url = "http://10.0.2.2:8080/api/v1/routines/"+routineId+"/exercises";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("exerciseName", exerciseNameEditText.getText().toString());
            requestBody.put("muscle", muscle);
            requestBody.put("equipment", equipment);
            requestBody.put("gifUrl", gifUrl);
            requestBody.put("instructions", instructionsEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(ExerciseDetailActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    private void updateSelectedExercise(){
        String url = "http://10.0.2.2:8080/api/v1/exercises/" + exerciseId;
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("exerciseName", exerciseNameEditText.getText().toString());
            requestBody.put("instructions", instructionsEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(ExerciseDetailActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Parse the error response
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String errorResponse = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errorObject = new JSONObject(errorResponse);
                        if (errorObject.has("message") && errorObject.getString("message").equals("no data changes found")) {
                            // Redirect to UserActivity
                            Intent intent = new Intent(ExerciseDetailActivity.this, UserActivity.class);
                            startActivity(intent);
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        })  {
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

    private void deleteExercise() {
        String url = "http://10.0.2.2:8080/api/v1/exercises/"+exerciseId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(ExerciseDetailActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(deleteRequest);
    }

    private void loadSets() {
        setList = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/v1/exercises/"+exerciseId+"/sets";
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject exerciseJson = response.getJSONObject(i);
                                Long setId = exerciseJson.getLong("id");
                                Integer reps = exerciseJson.getInt("reps");
                                Integer numberOfSets = exerciseJson.getInt("numberOfSets");
                                Double weight = exerciseJson.getDouble("weight");

                                setList.add(new Set(setId,reps,numberOfSets,weight));
                            }
                            setAdapter = new SetAdapter(setList);
                            setRecyclerView.setAdapter(setAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void createNewSet(){
        String url = "http://10.0.2.2:8080/api/v1/exercises/"+exerciseId+"/sets";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("reps", 0);
            requestBody.put("numberOfSets", 0);
            requestBody.put("weight", 0.0);
            requestBody.put("kilometers", null);
            requestBody.put("time", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadSets();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    public void deleteSet(Long setId){
        String url = "http://10.0.2.2:8080/api/v1/sets/"+setId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadSets();
                        Log.d("delete set success", "set deleted");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(deleteRequest);
    }

    public void updateSet(Set set){
        String url = "http://10.0.2.2:8080/api/v1/sets/"+set.getId();
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("reps", set.getReps());
            requestBody.put("numberOfSets", set.getNumberOfSets());
            requestBody.put("weight", set.getWeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("update set success", "set updated");
                        loadSets();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        String errorResponse = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errorObject = new JSONObject(errorResponse);
                        if (errorObject.has("message") && errorObject.getString("message").equals("no data changes found")) {
                            Log.d("update set no data changes", "set not updated");
                        }
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        })  {
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
}