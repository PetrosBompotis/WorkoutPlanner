package com.example.workoutplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    List<Exercise> exerciseList;
    ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // Assuming you have a RecyclerView with id 'recyclerView' in your layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseList = new ArrayList<>();

        adapter = new ExerciseAdapter(exerciseList);
        recyclerView.setAdapter(adapter);
        load();


    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void load() {
        String url = "http://10.0.2.2:8080/api/v1/routines/1/exercises";
        String accessToken = sharedPreferences.getString("accessToken", "");

        // Create a Map to hold headers
        Map<String, String> headers = new HashMap<>();
        // Add the Authorization header with the bearer token
        headers.put("Authorization", "Bearer " + accessToken);

        // Create a JsonObjectRequest with headers
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject exerciseJson = response.getJSONObject(i);
                                String exerciseName = exerciseJson.getString("exerciseName");
                                String muscle = exerciseJson.getString("muscle");
                                String equipment = exerciseJson.getString("equipment");
                                String gifUrl = exerciseJson.getString("gifUrl");

                                // Create an Exercise object and add it to the list
                                exerciseList.add(new Exercise(exerciseName, muscle, equipment, gifUrl));
                            }
                            // Notify the adapter that the data set has changed
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(ExerciseActivity.this, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }
}