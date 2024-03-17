package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExerciseDetailActivity extends AppCompatActivity {
    Button exerciseDoneButton, exerciseDeleteButton, addSetButton;
    TextView exerciseNameTextView, muscleTextView, setTextView;
    ImageView exerciseGifImageView;
    RecyclerView setRecyclerView;
    EditText instructionsEditText;
    Long routineId, exerciseId;
    String exerciseName, muscle, equipment, gifUrl, instructions;
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    Boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        exerciseDoneButton = findViewById(R.id.exerciseDoneButton);
        exerciseDeleteButton = findViewById(R.id.exerciseDeleteButton);
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView2);
        muscleTextView = findViewById(R.id.muscleTextView2);
        exerciseGifImageView = findViewById(R.id.exerciseGifImageView);
        setTextView = findViewById(R.id.setTextView);
        setRecyclerView = findViewById(R.id.setRecyclerView);
        addSetButton = findViewById(R.id.sddSetButton);
        instructionsEditText = findViewById(R.id.instructionsEditText);

        setUpListeners();
        initializeExtras();
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
            }

            exerciseNameTextView.setText(routineId.toString());
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
                // Handle click for addSetButton
            }
        });
    }

    private void createNewExercise(){
        String url = "http://10.0.2.2:8080/api/v1/routines/"+routineId+"/exercises";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("exerciseName", exerciseName);
            requestBody.put("muscle", muscle);
            requestBody.put("equipment", equipment);
            requestBody.put("gifUrl", gifUrl);
            requestBody.put("instructions", instructions);
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
            requestBody.put("exerciseName", "newExerciseName");
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
}