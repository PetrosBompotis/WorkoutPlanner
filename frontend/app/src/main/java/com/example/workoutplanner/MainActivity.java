package com.example.workoutplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_NAME = "MyPreferences";
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        refreshLoggedUserToken();
    }

    private void refreshLoggedUserToken(){
        String savedRefreshToken = sharedPreferences.getString("refreshToken", null);

        String url = "http://10.0.2.2:8080/api/v1/auth/refreshtoken";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("refreshToken", savedRefreshToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accessToken;
                        String refreshToken;
                        try {
                            accessToken = response.getString("accessToken");
                            refreshToken = response.getString("refreshToken");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        saveTokensToSharedPreferences(accessToken, refreshToken);
                        Log.d("RefreshToken",  "Success block");
                        Intent intent = new Intent(
                                MainActivity.this,
                                Objects.equals(sharedPreferences.getString("role", null), "ROLE_ADMIN")
                                        ? AdminActivity.class
                                        : UserActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RefreshToken", "failure block");
                        redirectToSignUp();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }

    public void redirectToSignUp() {
        replaceFragment(new SignUpFragment());
    }

    public void redirectToSignIn(){
        replaceFragment(new SignInFragment());
    }

    public void redirectToUserActivity(){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    public void redirectToAdminActivity(){
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    public void saveTokensToSharedPreferences(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }

    public void saveLoginDataToSharedPreferences(Long id, String role, String name, String email, String gender, Integer age) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("id", id);
        editor.putString("role", role);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("gender", gender);
        editor.putInt("age", age);
        editor.apply();
    }
}