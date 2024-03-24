package com.example.workoutplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class UserSharedPostsActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerViewMyPosts;
    private List<PostResponse> postList;
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shared_posts);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        recyclerViewMyPosts = findViewById(R.id.recyclerViewMyPosts);
        recyclerViewMyPosts.setLayoutManager(new LinearLayoutManager(this));

        loadPosts();
    }

    private void loadPosts(){
        postList = new ArrayList<>();
        long id = sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/"+id+"/posts";
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postJson = response.getJSONObject(i);
                                Long postId = postJson.getLong("id");
                                String postName = postJson.getString("postName");
                                String createdBy = postJson.getString("createdBy");

                                JSONObject workoutPlanJson = postJson.getJSONObject("workoutPlan");
                                Long workoutPlanId = workoutPlanJson.getLong("id");
                                String difficulty = workoutPlanJson.getString("difficulty");
                                String gender = workoutPlanJson.getString("gender");

                                postList.add(new PostResponse(postId, postName, createdBy, difficulty, gender, workoutPlanId));
                            }
                            adapter = new PostAdapter(postList, true);
                            recyclerViewMyPosts.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("post error", "post error");
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