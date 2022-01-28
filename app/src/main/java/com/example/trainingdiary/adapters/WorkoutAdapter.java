package com.example.trainingdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.R;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private String[] data;
    private boolean flag = false;

    public WorkoutAdapter(String[] data){
        this.data = data;
    }


    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.list_item_layout_workout, parent, false);

        if(flag){
           TextView txtView = (TextView) view.findViewById(R.id.txtExerciseItem);
           txtView.setText("Brak stworzonych treningów");
        }

        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        String title = data[position];
        holder.txtView.setText((title));

    }

    @Override
    public int getItemCount() {
        if(data.length == 0) {
            flag = true;
            return 1;
        }else{
            flag = false;
            return data.length;
        }
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView txtView, txtViewNoTraining;

       public WorkoutViewHolder(@NonNull View itemView) {
           super(itemView);
           txtView = (TextView) itemView.findViewById(R.id.txtExerciseItem);
       }
   }
}
