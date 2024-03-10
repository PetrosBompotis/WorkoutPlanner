package com.example.workoutplanner;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
    private EditText usernameEditText, emailEditText, passwordEditText;
    private MainActivity mainActivity;
    private Spinner genderSpinner, ageSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        usernameEditText = view.findViewById(R.id.signup_name);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_password);
        genderSpinner = view.findViewById(R.id.signup_gender_spinner);
        ageSpinner = view.findViewById(R.id.signup_age_spinner);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, new String[]{"MALE", "FEMALE"});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, generateNumbersArray());
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);

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
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String age = ageSpinner.getSelectedItem().toString();

        if (username.isEmpty()) {
            showMessage("Error", "Username cannot be empty");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Error", "Invalid email address");
            return;
        }

        if (password.length() < 6) {
            showMessage("Error", "Password must be at least 6 characters long");
            return;
        }

        String url = "http://10.0.2.2:8080/api/v1/customers";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", username);
            requestBody.put("email", email);
            requestBody.put("gender", gender);
            requestBody.put("age", Integer.parseInt(age));
            requestBody.put("password", password);
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

    private String[] generateNumbersArray() {
        String[] numbers = new String[100];
        for (int i = 0; i < 100; i++) {
            numbers[i] = String.valueOf(i);
        }
        return numbers;
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}