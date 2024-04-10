package com.example.workoutplanner.postDetailActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.workoutplanner.R;
import com.example.workoutplanner.exerciseActivity.ExerciseResponse;
import com.example.workoutplanner.exerciseActivity.ExerciseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineFragmentPost extends Fragment {

    private Long routineId;
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private List<ExerciseResponse> exerciseList;

    public RoutineFragmentPost(Long routineId) {
        this.routineId = routineId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        exerciseList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        recyclerView = view.findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadExercises(routineId);
        return view;
    }

    private void loadExercises(Long routineId) {
        String url = "http://10.0.2.2:8080/api/v1/routines/"+routineId+"/exercises";
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            exerciseList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject exerciseJson = response.getJSONObject(i);
                                Long exerciseId = exerciseJson.getLong("id");
                                String exerciseName = exerciseJson.getString("exerciseName");
                                String muscle = exerciseJson.getString("muscle");
                                String equipment = exerciseJson.getString("equipment");
                                String gifUrl = exerciseJson.getString("gifUrl");
                                String instructions = exerciseJson.getString("instructions");

                                exerciseList.add(new ExerciseResponse(exerciseName, muscle, equipment, gifUrl, instructions, exerciseId));
                            }
                            adapter = new ExerciseAdapter(exerciseList, routineId, false, false, false);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(requireContext(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
