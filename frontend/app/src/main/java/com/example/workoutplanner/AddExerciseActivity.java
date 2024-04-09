package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.workoutplanner.exerciseDetailActivity.ExerciseDetailActivity;
import com.example.workoutplanner.userActivity.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddExerciseActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    private EditText exerciseNameEditText, gifUrlEditText, instructionsEditText;
    private Spinner equipmentSpinner, muscleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        exerciseNameEditText = findViewById(R.id.exercise_name_editText);
        gifUrlEditText = findViewById(R.id.gifUrlEditText);
        instructionsEditText = findViewById(R.id.adminInstructionsEditText);
        equipmentSpinner = findViewById(R.id.adminEquipmentSpinner);
        muscleSpinner = findViewById(R.id.adminMuscleSpinner);

        instantiateSpinners();
    }

    private void instantiateSpinners() {
        ArrayAdapter<CharSequence> equipmentAdapter = ArrayAdapter.createFromResource(this, R.array.equipment_array, android.R.layout.simple_spinner_item);
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipmentSpinner.setAdapter(equipmentAdapter);

        ArrayAdapter<CharSequence> muscleAdapter = ArrayAdapter.createFromResource(this, R.array.muscle_array, android.R.layout.simple_spinner_item);
        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(muscleAdapter);
    }

    public void addExercise(View view){
        createNewExercise();
    }

    private void createNewExercise(){
        String exerciseName = exerciseNameEditText.getText().toString();
        String muscle = muscleSpinner.getSelectedItem().toString();
        String equipment = equipmentSpinner.getSelectedItem().toString();
        String gifUrl = gifUrlEditText.getText().toString();
        String instructions = instructionsEditText.getText().toString();

        String url = "http://10.0.2.2:8080/api/v1/routines/1/exercises";

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
                        showToastLong(AddExerciseActivity.this, "created successfully");
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

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}