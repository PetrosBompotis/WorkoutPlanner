package com.example.workoutplanner;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SetViewHolder extends RecyclerView.ViewHolder {
    TextView repsTextView, setsTextView, weightTextView;
    EditText repsEditText, setsEditText, weightEditText;
    ImageButton deleteSetImageButton, updateSetImageButton;

    public SetViewHolder(@NonNull View itemView) {
        super(itemView);
        repsTextView = itemView.findViewById(R.id.repsTextView);
        setsTextView = itemView.findViewById(R.id.setsTextView);
        weightTextView = itemView.findViewById(R.id.weightTextView);

        repsEditText = itemView.findViewById(R.id.repsEditText);
        setsEditText = itemView.findViewById(R.id.setsEditText);
        weightEditText = itemView.findViewById(R.id.weightEditText);

        deleteSetImageButton = itemView.findViewById(R.id.deleteSetButton);
        updateSetImageButton = itemView.findViewById(R.id.updateSetButton);
        deleteSetImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setId = (String) v.getTag();
                ((ExerciseDetailActivity)itemView.getContext()).deleteSet(Long.parseLong(setId));
            }
        });
        updateSetImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setId = (String) v.getTag();
                ((ExerciseDetailActivity)itemView.getContext()).updateSet(Long.parseLong(setId));
            }
        });
    }
}
