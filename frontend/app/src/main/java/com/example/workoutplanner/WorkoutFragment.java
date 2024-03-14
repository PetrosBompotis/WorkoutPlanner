package com.example.workoutplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutFragment extends Fragment {
    private RequestQueue requestQueue;
    private UserActivity userActivity;
    private Spinner workoutPlanSpinner;
    private ArrayList<String> programNamesList, routineNamesList;
    private ArrayList<Long> programIdsList, routineIdsList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RoutinePagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        userActivity = (UserActivity) getActivity();
        programNamesList = new ArrayList<>();
        programIdsList = new ArrayList<>();
        routineNamesList = new ArrayList<>();
        routineIdsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        workoutPlanSpinner = view.findViewById(R.id.workout_plan_spinner);
        tabLayout = view.findViewById(R.id.tabLayoutRoutines);
        viewPager = view.findViewById(R.id.viewPagerRoutines);

        loadWorkoutPlans(); // Load workout plans when fragment is created

        // Button to create a new workout plan
        view.findViewById(R.id.workout_plan_create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewWorkoutPlan();
            }
        });

        view.findViewById(R.id.workout_plan_rename_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = workoutPlanSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    Long workoutPlanId = programIdsList.get(selectedPosition);
                    updateSelectedWorkoutPlan(workoutPlanId);
                } else {
                    showToastLong(requireContext(), "Please select a workout plan to delete.");
                }
            }
        });

        view.findViewById(R.id.workout_plan_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = workoutPlanSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    Long workoutPlanId = programIdsList.get(selectedPosition);
                    deleteWorkoutPlan(workoutPlanId);
                } else {
                    showToastLong(requireContext(), "Please select a workout plan to delete.");
                }
            }
        });

        workoutPlanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadRoutines();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        return view;
    }

    private void loadWorkoutPlans(){
        Long id = userActivity.sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/"+id+"/workoutPlans";
        String accessToken = userActivity.sharedPreferences.getString("accessToken", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            programNamesList.clear(); // Clear existing lists
                            programIdsList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject workoutPlanJson = response.getJSONObject(i);
                                Long id = workoutPlanJson.getLong("id");
                                String programName = workoutPlanJson.getString("programName");
                                programNamesList.add(programName);
                                programIdsList.add(id);
                            }
                            // Populate spinner after getting all program names
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, programNamesList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            workoutPlanSpinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToastLong(requireContext(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    private void deleteWorkoutPlan(Long workoutPlanId) {
        String url = "http://10.0.2.2:8080/api/v1/workoutPlans/" + workoutPlanId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Workout plan deleted successfully
                        showToastLong(requireContext(), "Workout plan deleted successfully");
                        // Optionally, you can reload the workout plans after deletion
                        loadWorkoutPlans();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastLong(requireContext(), "Error deleting workout plan: " + error.getMessage());
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

    private void createNewWorkoutPlan() {
        Long customerId = userActivity.sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/" + customerId + "/workoutPlans";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("programName", "Program DDD");
            requestBody.put("difficulty", "Beginner");
            requestBody.put("gender", "MALE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showToastLong(requireContext(), "Workout plan created successfully");
                        loadWorkoutPlans();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage("requireContext()", "Error creating workout plan: " + error.getMessage());
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

    private void updateSelectedWorkoutPlan(Long workoutPlanId) {
            String url = "http://10.0.2.2:8080/api/v1/workoutPlans/" + workoutPlanId;
            String accessToken = userActivity.sharedPreferences.getString("accessToken", "");

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("programName", "newName");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            showToastLong(requireContext(), "Workout plan updated successfully");
                            loadWorkoutPlans();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToastLong(requireContext(), "Error updating workout plan: " + error.getMessage());
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

    private void loadRoutines(){
        int selectedPosition = workoutPlanSpinner.getSelectedItemPosition();
        Long workoutPlanId = 1L;
        if (selectedPosition != AdapterView.INVALID_POSITION) {
            workoutPlanId = programIdsList.get(selectedPosition);
        } else {
            showToastLong(requireContext(), "Please select a workout plan to delete.");
        }
        String url = "http://10.0.2.2:8080/api/v1/workoutPlans/"+workoutPlanId+"/routines";
        String accessToken = userActivity.sharedPreferences.getString("accessToken", "");

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
                            pagerAdapter = new RoutinePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
                showToastLong(requireContext(), "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }


    private void deleteRoutine(Long routineId) {
        if (routineId == 0L){
            showToastLong(requireContext(), "no routine selected");
            return;
        }
        String url = "http://10.0.2.2:8080/api/v1/routines/" + routineId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToastLong(requireContext(), "Routine deleted successfully");
                        loadRoutines();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastLong(requireContext(), "Error deleting routine: " + error.getMessage());
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

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(requireContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}