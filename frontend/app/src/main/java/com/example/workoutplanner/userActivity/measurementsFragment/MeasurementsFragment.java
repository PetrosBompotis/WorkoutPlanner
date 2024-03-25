package com.example.workoutplanner.userActivity.measurementsFragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import com.example.workoutplanner.R;
import com.example.workoutplanner.userActivity.UserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurementsFragment extends Fragment {
    private RequestQueue requestQueue;
    private UserActivity userActivity;
    private List<Measurement> measurementList;
    private MeasurementAdapter measurementAdapter;
    private RecyclerView measurementRecyclerView;
    private EditText bodyFatEditText, bodyWeightEditText;
    private Button createMeasurementButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        userActivity = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measurements, container, false);
        measurementRecyclerView = view.findViewById(R.id.measurementsRecyclerView);
        measurementRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bodyFatEditText = view.findViewById(R.id.bodyFatEditText);
        bodyWeightEditText = view.findViewById(R.id.bodyWeightEditText);
        createMeasurementButton = view.findViewById(R.id.createMeasurementButton);

        createMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeasurement();
            }
        });
        loadMeasurements();

        return view;
    }

    private void loadMeasurements(){
        long customerId = userActivity.sharedPreferences.getLong("id", 1);
        measurementList = new ArrayList<>();
        String url = "http://10.0.2.2:8080/api/v1/customers/"+customerId+"/measurements";
        String accessToken = userActivity.sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject measurementJson = response.getJSONObject(i);
                                Long measurementId = measurementJson.getLong("id");
                                Double bodyFatPercentage = measurementJson.getDouble("bodyFatPercentage");
                                Double bodyWeight = measurementJson.getDouble("bodyWeight");
                                String createdAtString = measurementJson.getString("createdAt");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                Date createdAt = null;
                                try {
                                    createdAt = dateFormat.parse(createdAtString);
                                } catch (Exception e) {
                                    e.printStackTrace(); // Handle parsing exception
                                }


                                measurementList.add(new Measurement(measurementId,bodyFatPercentage,bodyWeight,createdAt));
                            }
                            measurementAdapter = new MeasurementAdapter(measurementList);
                            measurementRecyclerView.setAdapter(measurementAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void createMeasurement(){
        long customerId = userActivity.sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/" + customerId + "/measurements";

        String bodyFatString = bodyFatEditText.getText().toString();
        String bodyWeightString = bodyWeightEditText.getText().toString();

        if (bodyFatString.isEmpty() || bodyWeightString.isEmpty()) {
            showMessage("error", "One or more fields are empty");
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            Double bodyFatPercentage = Double.valueOf(bodyFatString);
            Double bodyWeight = Double.valueOf(bodyWeightString);

            requestBody.put("bodyFatPercentage", bodyFatPercentage);
            requestBody.put("bodyWeight", bodyWeight);

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String formattedDate = dateFormat.format(currentDate);

            requestBody.put("createdAt", formattedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadMeasurements();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("requireContext()", "Error creating workout plan: ");
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return Response.success(new JSONObject(), HttpHeaderParser.parseCacheHeaders(response));
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + userActivity.sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void deleteMeasurement(Long measurementId){
        String url = "http://10.0.2.2:8080/api/v1/measurements/"+measurementId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadMeasurements();
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
                headers.put("Authorization", "Bearer " + userActivity.sharedPreferences.getString("accessToken", ""));
                return headers;
            }
        };

        requestQueue.add(deleteRequest);
    }

    private void showMessage(String title, String message){
        new AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}