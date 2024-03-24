package com.example.workoutplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    private Boolean isEditable;
    private ImageButton downloadButton, deletePostButton, editPostButton;
    private Dialog dialog;
    private TextView postNameTextViewDetail, createdByTextViewDetail;

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
        downloadButton = findViewById(R.id.downloadButton);
        deletePostButton = findViewById(R.id.deletePostButton);
        editPostButton = findViewById(R.id.editPostButton);
        postNameTextViewDetail = findViewById(R.id.postNameTextViewDetail);
        createdByTextViewDetail = findViewById(R.id.createdByTextViewDetail);

        initializeExtras();
        setupListeners();
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
            isEditable = extras.getBoolean("isEditable");
        }

        if (isEditable){
            downloadButton.setVisibility(View.GONE);
        }else {
            deletePostButton.setVisibility(View.GONE);
            editPostButton.setVisibility(View.GONE);
        }

        postNameTextViewDetail.setText(postName);
        createdByTextViewDetail.setText(createdBy);
    }

    private void setupListeners() {
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPost();
            }
        });
        deletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });
        editPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
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

    private void downloadPost() {
        long customerId = sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/" + customerId + "/workoutPlans/" + workoutPlanId;

        JSONObject requestBody = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(PostDetailActivity.this, "Workout plan downloaded successfully");
                        Intent intent = new Intent(PostDetailActivity.this, UserActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(PostDetailActivity.this, "Error creating workout plan: " + error.getMessage());
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

    private void deletePost() {
        String url = "http://10.0.2.2:8080/api/v1/posts/" + postId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToastLong(PostDetailActivity.this, "Post deleted successfully");
                        Intent intent = new Intent(PostDetailActivity.this, UserSharedPostsActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastLong(PostDetailActivity.this, "Error deleting post: " + error.getMessage());
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

    private void showCustomDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button cancelDialogButton = dialog.findViewById(R.id.button_cancel);
        Button submitDialogButton = dialog.findViewById(R.id.button_submit);
        TextView customDialogTextView = dialog.findViewById(R.id.customDialogTextView);
        EditText customDialogEditText = dialog.findViewById(R.id.customDialogEditText);

        customDialogTextView.setText("Rename post");

        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submitDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customDialogEditText.getText() != null && !customDialogEditText.getText().toString().trim().isEmpty()) {
                    updatePost(customDialogEditText.getText().toString());
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void updatePost(String postName){
        String url = "http://10.0.2.2:8080/api/v1/posts/"+postId;
        String accessToken = sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("postName", postName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(PostDetailActivity.this, "Post updated successfully");
                        Intent intent = new Intent(PostDetailActivity.this, UserSharedPostsActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(PostDetailActivity.this, "Error updating post: " + error.getMessage());
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
}