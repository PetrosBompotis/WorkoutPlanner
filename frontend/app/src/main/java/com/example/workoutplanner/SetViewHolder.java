package com.example.workoutplanner;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SetViewHolder extends RecyclerView.ViewHolder {
    EditText repsEditText, setsEditText, weightEditText;
    ImageButton deleteSetImageButton, updateSetImageButton;

    public SetViewHolder(@NonNull View itemView) {
        super(itemView);
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
                // Retrieve the Set object associated with this view holder
                Set set = (Set) v.getTag();

                // Retrieve values from EditText fields
                String repsValue = "";
                String setsValue = "";
                String weightValue = "";

                if (repsEditText.getText() != null && !repsEditText.getText().toString().isEmpty()) {
                    repsValue = repsEditText.getText().toString();
                }

                if (setsEditText.getText() != null && !setsEditText.getText().toString().isEmpty()) {
                    setsValue = setsEditText.getText().toString();
                }

                if (weightEditText.getText() != null && !weightEditText.getText().toString().isEmpty()) {
                    weightValue = weightEditText.getText().toString();
                }

                // Convert values to appropriate types and update the Set object
                int reps = repsValue.isEmpty() ? 0 : Integer.parseInt(repsValue);
                int sets = setsValue.isEmpty() ? 0 : Integer.parseInt(setsValue);
                double weight = weightValue.isEmpty() ? 0.0 : Double.parseDouble(weightValue);

                // Update the Set object
                set.setReps(reps);
                set.setNumberOfSets(sets);
                set.setWeight(weight);

                // Call the updateSet method of ExerciseDetailActivity
                ((ExerciseDetailActivity)itemView.getContext()).updateSet(set);
            }
        });
    }
}
