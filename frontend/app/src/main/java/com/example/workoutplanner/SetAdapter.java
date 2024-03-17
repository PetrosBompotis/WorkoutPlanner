package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {

    private List<Set> sets;

    public SetAdapter(List<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.repsEditText.setText(set.getReps().toString());
        holder.setsEditText.setText(set.getNumberOfSets().toString());
        holder.weightEditText.setText(set.getWeight().toString());
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    class SetViewHolder extends RecyclerView.ViewHolder {
        TextView repsTextView, setsTextView, weightTextView;
        EditText repsEditText, setsEditText, weightEditText;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            repsTextView = itemView.findViewById(R.id.repsTextView);
            setsTextView = itemView.findViewById(R.id.setsTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);

            repsEditText = itemView.findViewById(R.id.repsEditText);
            setsEditText = itemView.findViewById(R.id.setsEditText);
            weightEditText = itemView.findViewById(R.id.weightEditText);
        }

    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }
}
