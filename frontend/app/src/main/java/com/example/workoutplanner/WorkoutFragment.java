package com.example.workoutplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private FloatingActionButton floatingActionButton;
    private Button workoutPlanManageButton;

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
        floatingActionButton = view.findViewById(R.id.fab_manage_routine);
        workoutPlanManageButton = view.findViewById(R.id.workout_plan_manage_button);

        loadWorkoutPlans();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoutineBottomDialog();
            }
        });

        workoutPlanManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = workoutPlanSpinner.getSelectedItemPosition();
                if (selectedPosition != AdapterView.INVALID_POSITION) {
                    Long workoutPlanId = programIdsList.get(selectedPosition);
                    showWorkoutBottomDialog(workoutPlanId);
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

    private void showRoutineBottomDialog() {

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout layoutCreateRoutine = dialog.findViewById(R.id.layoutCreateRoutine);
        LinearLayout layoutRenameRoutine = dialog.findViewById(R.id.layoutRenameRoutine);
        LinearLayout layoutDeleteRoutine = dialog.findViewById(R.id.layoutDeleteRoutine);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        layoutCreateRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(requireContext(),"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        layoutRenameRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(requireContext(),"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        layoutDeleteRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(requireContext(),"Go live is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showWorkoutBottomDialog(Long workoutPlanId) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_workout_layout);

        LinearLayout layoutCreateWorkout = dialog.findViewById(R.id.layoutCreateWorkout);
        LinearLayout layoutRenameWorkout = dialog.findViewById(R.id.layoutRenameWorkout);
        LinearLayout layoutDeleteWorkout = dialog.findViewById(R.id.layoutDeleteWorkout);
        ImageView cancelButton = dialog.findViewById(R.id.cancelWorkoutButton);

        layoutCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createNewWorkoutPlan();
            }
        });

        layoutRenameWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateSelectedWorkoutPlan(workoutPlanId);
            }
        });

        layoutDeleteWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteWorkoutPlan(workoutPlanId);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}