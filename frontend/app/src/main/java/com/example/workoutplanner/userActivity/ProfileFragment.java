package com.example.workoutplanner.userActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workoutplanner.R;
import com.example.workoutplanner.mainActivity.MainActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private TextView usernameTextView, genderTextView, ageTextView, emailTextView, nameTextView;
    private ShapeableImageView profileImageView;
    private UserActivity userActivity;
    private Button signOutButton, deleteAccountButton;
    private RequestQueue requestQueue;
    private Dialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActivity = (UserActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        initViews(view);
        setupListeners();
        populateUserData();

        return view;
    }

    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.usernameTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        profileImageView = view.findViewById(R.id.profileImageView);
        signOutButton = view.findViewById(R.id.signOutButton);
        deleteAccountButton = view.findViewById(R.id.deleteAccountButton);
    }

    private void setupListeners() {
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    private void populateUserData() {
        String gender = userActivity.sharedPreferences.getString("gender", "d");

        usernameTextView.setText(userActivity.sharedPreferences.getString("name", "d"));
        nameTextView.setText(userActivity.sharedPreferences.getString("name", "d"));
        genderTextView.setText(gender);
        ageTextView.setText(String.valueOf(userActivity.sharedPreferences.getInt("age", 4)));
        emailTextView.setText(userActivity.sharedPreferences.getString("email", "d"));

        if (gender.equals("MALE")) {
            profileImageView.setImageResource(R.drawable.man_profile);
        } else {
            profileImageView.setImageResource(R.drawable.woman_profile);
        }
    }

    private void deleteAccount(){
        long customerId = userActivity.sharedPreferences.getLong("id", 1);
        String url = "http://10.0.2.2:8080/api/v1/customers/"+customerId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showToastLong(requireContext(), "Account deleted successfully");
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastLong(requireContext(), "Error deleting your account");
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

    private void signOut(){
        SharedPreferences.Editor editor = userActivity.sharedPreferences.edit();
        editor.putString("accessToken", "");
        editor.putString("refreshToken", "");
        editor.apply();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }

    private void showCustomDialog(){
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button cancelDialogButton = dialog.findViewById(R.id.button_cancel);
        Button submitDialogButton = dialog.findViewById(R.id.button_submit);
        TextView customDialogTextView = dialog.findViewById(R.id.customDialogTextView);
        EditText customDialogEditText = dialog.findViewById(R.id.customDialogEditText);

        customDialogTextView.setText("Are you sure you want to delete your account? If yes, type 'Of course!'");

        cancelDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submitDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customDialogEditText.getText().toString().trim().equals("Of course!")) {
                    deleteAccount();
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}