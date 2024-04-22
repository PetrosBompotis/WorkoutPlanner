package com.example.workoutplanner.manageUsersActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workoutplanner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUsersActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<UserResponse> userResponseList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        searchView = findViewById(R.id.searchUser);
        searchView.clearFocus();
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();

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
    }

    private void loadUsers() {
        userResponseList = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/v1/customers";
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
                                Long userId = exerciseJson.getLong("id");
                                String userName = exerciseJson.getString("name");
                                String email = exerciseJson.getString("email");
                                String gender = exerciseJson.getString("gender");
                                Integer age = exerciseJson.getInt("age");

                                userResponseList.add(new UserResponse(userId, userName, email, gender, age));
                            }
                            userAdapter = new UserAdapter(userResponseList);
                            recyclerView.setAdapter(userAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(ManageUsersActivity.this, "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void deleteUser(Long userId){
        String url = "http://10.0.2.2:8080/api/v1/customers/"+userId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadUsers();
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

    private void handleFiltering(String query) {
        List<UserResponse> filteredUsers = new ArrayList<>();
        for (UserResponse user : userResponseList) {
            if ((user.getEmail().toLowerCase().contains(query.toLowerCase()) || query.isEmpty())) {
                filteredUsers.add(user);
            }
        }
        userAdapter.filterList(filteredUsers);
    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}