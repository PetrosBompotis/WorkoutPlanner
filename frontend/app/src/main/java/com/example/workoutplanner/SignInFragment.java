package com.example.workoutplanner;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInFragment extends Fragment {

    private RequestQueue requestQueue;
    private EditText emailEditText, passwordEditText;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailEditText = view.findViewById(R.id.login_email);
        passwordEditText = view.findViewById(R.id.login_password);

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        view.findViewById(R.id.signupRedirectText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.redirectToSignUp();
            }
        });
        return view;
    }

    public void signIn(){
        String username = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            showMessage("Error", "Invalid email address");
            return;
        }

        if (password.length() < 6) {
            showMessage("Error", "Password must be at least 6 characters long");
            return;
        }

        String url = "http://10.0.2.2:8080/api/v1/auth/login";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String accessToken = response.getString("token");
                            String refreshToken = response.getString("refreshToken");
                            JSONObject customerDTO = response.getJSONObject("customerDTO");
                            Long id = customerDTO.getLong("id");
                            JSONArray rolesArray = customerDTO.getJSONArray("roles");
                            String role = rolesArray.getString(0);
                            mainActivity.saveTokensToSharedPreferences(accessToken, refreshToken);
                            mainActivity.saveLoginDataToSharedPreferences(id, role);
                            Log.d("SignIn", "failure block2");
                            if (role.equals("ROLE_ADMIN")){
                                Log.d("SignIn", "failure block3");
                                mainActivity.redirectToAdminActivity();
                            }else {
                                mainActivity.redirectToUserActivity();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SignIn", "failure block");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}