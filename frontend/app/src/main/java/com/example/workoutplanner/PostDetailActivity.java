package com.example.workoutplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> routineNamesList;
    private ArrayList<Long> routineIdsList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RoutinePagerAdapterPost pagerAdapter;
    Long postId, workoutPlanId;
    String postName, createdBy, difficulty, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        routineNamesList = new ArrayList<>();
        routineIdsList = new ArrayList<>();
        tabLayout = findViewById(R.id.tabLayoutRoutines2);
        viewPager = findViewById(R.id.viewPagerRoutines2);

        initializeExtras();
        loadRoutines();
    }

    private void initializeExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postId = extras.getLong("postId");
            postName = extras.getString("postName");
            createdBy = extras.getString("createdBy");
            difficulty = extras.getString("difficulty");
            gender = extras.getString("gender");
            workoutPlanId = extras.getLong("workoutPlanId");
        }
    }

    private void loadRoutines(){
        String url = "http://10.0.2.2:8080/api/v1/workoutPlans/"+workoutPlanId+"/routines";
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            routineIdsList.clear();
                            routineNamesList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject routineJson = response.getJSONObject(i);
                                Long id = routineJson.getLong("id");
                                String routineName = routineJson.getString("routineName");
                                routineNamesList.add(routineName);
                                routineIdsList.add(id);
                            }
                            // Create a new adapter for the ViewPager
                            pagerAdapter = new RoutinePagerAdapterPost(getSupportFragmentManager());
                            pagerAdapter.setRoutineIdsList(routineIdsList);
                            pagerAdapter.setRoutineNamesList(routineNamesList);
                            viewPager.setAdapter(pagerAdapter);

                            // Link the TabLayout and ViewPager
                            tabLayout.setupWithViewPager(viewPager);

                            // Notify the adapter that the data set has changed
                            pagerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("routine error", "routine error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }
}