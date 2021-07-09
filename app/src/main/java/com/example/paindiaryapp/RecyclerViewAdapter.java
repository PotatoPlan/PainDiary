package com.example.paindiaryapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paindiaryapp.databinding.RecyclerviewLayoutBinding;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Activity context;
    private List<Records> records;

    // Get data that is passed from DailyRecordFragment (in a form of List)
    public RecyclerViewAdapter(Activity activity, List<Records> records) {
        this.context = activity;
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewLayoutBinding binding = RecyclerviewLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Set text views by data respectively
        final Records r = records.get(position);
        viewHolder.binding.d.setText(r.getDate());
        viewHolder.binding.pl.setText(r.getPainLocation());
        viewHolder.binding.ml.setText(r.getMoodLevel());
        viewHolder.binding.st.setText(r.getStepsTaken());
        viewHolder.binding.pil.setText(r.getPainIntensity());
    }

    @Override
    public int getItemCount() {
        // Return as much information as the size of the record
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewLayoutBinding binding;
        public ViewHolder(RecyclerviewLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
