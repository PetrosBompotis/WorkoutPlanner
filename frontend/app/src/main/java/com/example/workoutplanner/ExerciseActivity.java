package com.example.workoutplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.SearchView;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private List<Exercise> exerciseList;
    private ExerciseAdapter adapter;
    private SearchView searchView;
    private Spinner equipmentSpinner;
    private Spinner muscleSpinner;
    private Long routineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        equipmentSpinner = findViewById(R.id.equipmentSpinner);
        muscleSpinner = findViewById(R.id.muscleSpinner);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupListeners();
        instantiateSpinners();
        initializeExtras();
        loadExercises();
    }

    private void instantiateSpinners() {
        ArrayAdapter<CharSequence> equipmentAdapter = ArrayAdapter.createFromResource(this, R.array.equipment_array, android.R.layout.simple_spinner_item);
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipmentSpinner.setAdapter(equipmentAdapter);

        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(this, R.array.muscle_array, android.R.layout.simple_spinner_item);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(muscleAdapter);
    }

    private void initializeExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            routineId = extras.getLong("routineId");
        }
    }

    private void setupListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleFiltering(newText);
                return true;
            }

        });

        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFiltering(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        muscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFiltering(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadExercises() {
        exerciseList = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/v1/routines/1/exercises";
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
                                Long exerciseId = exerciseJson.getLong("id");
                                String exerciseName = exerciseJson.getString("exerciseName");
                                String muscle = exerciseJson.getString("muscle");
                                String equipment = exerciseJson.getString("equipment");
                                String gifUrl = exerciseJson.getString("gifUrl");
                                String instructions = exerciseJson.getString("instructions");

                                exerciseList.add(new Exercise(exerciseName, muscle, equipment, gifUrl, instructions, exerciseId));
                            }
                            adapter = new ExerciseAdapter(exerciseList, routineId, true, true);
                            recyclerView.setAdapter(adapter);
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

        requestQueue.add(jsonArrayRequest);
    }

    private void handleFiltering(String query) {
        List<Exercise> filteredExercises = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            if ((exercise.getExerciseName().toLowerCase().contains(query.toLowerCase()) || query.isEmpty()) &&
                    (equipmentSpinner.getSelectedItem().toString().equalsIgnoreCase("any") || exercise.getEquipment().equalsIgnoreCase(equipmentSpinner.getSelectedItem().toString())) &&
                    (muscleSpinner.getSelectedItem().toString().equalsIgnoreCase("any") || exercise.getMuscle().equalsIgnoreCase(muscleSpinner.getSelectedItem().toString()))) {
                filteredExercises.add(exercise);
            }
        }
        adapter.filterList(filteredExercises);
    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
