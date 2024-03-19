package com.example.workoutplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetViewHolder> {

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
        holder.deleteSetImageButton.setTag(set.getId().toString());
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }
}
