package com.example.workoutplanner.mainActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.workoutplanner.R;
import com.example.workoutplanner.mainActivity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpFragment extends Fragment {
    private RequestQueue requestQueue;
    private EditText usernameEditText, emailEditText, passwordEditText;
    private MainActivity mainActivity;
    private Spinner genderSpinner, ageSpinner;
    private Button signUpButton;
    private TextView loginRedirectTextView;

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
        initViews(view);
        populateSpinners();
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        usernameEditText = view.findViewById(R.id.signup_name);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_password);
        genderSpinner = view.findViewById(R.id.signup_gender_spinner);
        ageSpinner = view.findViewById(R.id.signup_age_spinner);
        signUpButton = view.findViewById(R.id.signup_button);
        loginRedirectTextView = view.findViewById(R.id.loginRedirectText);
    }

    private void populateSpinners() {
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, new String[]{"MALE", "FEMALE"});
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, generateNumbersArray());
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);
    }

    private void setupListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginRedirectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.redirectToSignIn();
            }
        });
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
            Log.e("SignUp", "Error creating JSON request body: " + e.getMessage());
            return;
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
                Log.e("SignUp", "Error signing up: " + error.getMessage());
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.d("Signup", "Success block");
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