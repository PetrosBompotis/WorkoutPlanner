package com.example.workoutplanner.userActivity.communityFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.workoutplanner.R;
import com.example.workoutplanner.UserSharedPostsActivity;
import com.example.workoutplanner.userActivity.UserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends Fragment {
    private UserActivity userActivity;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewPosts;
    private List<PostResponse> postList;
    private PostAdapter adapter;
    private SearchView searchViewPost;
    private Spinner difficultySpinner;
    private Spinner genderSpinner;
    private Button mySharedWorkoutsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActivity = (UserActivity) getActivity();
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);
        mySharedWorkoutsButton = view.findViewById(R.id.mySharedWorkoutsButton);
        difficultySpinner = view.findViewById(R.id.difficultySpinner);
        genderSpinner = view.findViewById(R.id.genderSpinner);
        recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchViewPost = view.findViewById(R.id.searchPost);
        searchViewPost.clearFocus();

        instantiateSpinners();
        setupListeners();
        loadPosts();

        return view;
    }

    private void instantiateSpinners() {
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.difficulty_array, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
    }

    private void setupListeners() {
        searchViewPost.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFiltering(searchViewPost.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFiltering(searchViewPost.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        mySharedWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), UserSharedPostsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadPosts(){
        postList = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/v1/posts";
        String accessToken = userActivity.sharedPreferences.getString("accessToken", "");

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
                                Integer downloadCounter = postJson.getInt("downloadCounter");

                                JSONObject workoutPlanJson = postJson.getJSONObject("workoutPlan");
                                Long workoutPlanId = workoutPlanJson.getLong("id");
                                String difficulty = workoutPlanJson.getString("difficulty");
                                String gender = workoutPlanJson.getString("gender");

                                postList.add(new PostResponse(postId, postName, createdBy, difficulty, gender, workoutPlanId, downloadCounter));
                            }
                            adapter = new PostAdapter(postList, false);
                            recyclerViewPosts.setAdapter(adapter);
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

    private void handleFiltering(String query) {
        List<PostResponse> filteredPosts = new ArrayList<>();
        for (PostResponse post : postList) {
            if ((post.getPostName().toLowerCase().contains(query.toLowerCase()) || query.isEmpty()) &&
                    (difficultySpinner.getSelectedItem().toString().equalsIgnoreCase("any") || post.getDifficulty().equalsIgnoreCase(difficultySpinner.getSelectedItem().toString())) &&
                    (genderSpinner.getSelectedItem().toString().equalsIgnoreCase("any") || post.getGender().equalsIgnoreCase(genderSpinner.getSelectedItem().toString()))) {
                filteredPosts.add(post);
            }
        }
        adapter.filterList(filteredPosts);
    }
}