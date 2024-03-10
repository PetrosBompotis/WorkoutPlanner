package com.example.workoutplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SignUpFragment extends Fragment {
    private RequestQueue requestQueue;
    private EditText usernameEditText, emailEditText, passwordEditText, genderEditText, ageEditText;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        usernameEditText = view.findViewById(R.id.signup_name);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_password);
        genderEditText = view.findViewById(R.id.signup_gender);
        ageEditText = view.findViewById(R.id.signup_age);

        view.findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        view.findViewById(R.id.loginRedirectText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.redirectToSignIn();
            }
        });
        return view;
    }

    private void signUp(){
        String url = "http://10.0.2.2:8080/api/v1/customers";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", usernameEditText.getText().toString().trim());
            requestBody.put("email", emailEditText.getText().toString().trim());
            requestBody.put("gender", genderEditText.getText().toString().trim());
            requestBody.put("age", Integer.parseInt(ageEditText.getText().toString().trim()));
            requestBody.put("password", passwordEditText.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                requestBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Signup", "User profile created!");
                        mainActivity.redirectToSignIn();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Signup", "failure block");
                    }
                }){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        Log.d("Signup", "User profile created!2");
                        return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

        requestQueue.add(jsonObjectRequest);
    }
}