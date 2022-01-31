package com.example.trainingdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.R;
import com.example.trainingdiary.objectsClasses.Workout;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private ArrayList<Workout> data;
    private boolean flag = false;

    public WorkoutAdapter( ArrayList<Workout> data){
        this.data = data;
    }


    @NonNull
    @Override
    public WorkoutAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.list_item_layout_workout, parent, false);

        if(flag){
            TextView title =  (TextView) view.findViewById(R.id.txt_workout_item);
            title.setText(R.string.no_workouts);
        }

        return new WorkoutAdapter.WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.WorkoutViewHolder holder, int position) {
        if(!flag){
            String exercieName = data.get(position).getName();
            holder.txtView.setText((exercieName));
        }

    }

    @Override
    public int getItemCount() {
        if(data.size() == 0) {
            flag = true;
            return 1;
        }else{
            flag = false;
            return data.size();
        }
    }

    public void removeItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Workout workout, int position){
        data.add(position, workout);
        notifyItemInserted(position);
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder{
        TextView txtView;
        public LinearLayout viewWorkoutBackground, viewWorkoutForeground;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.txt_workout_item);
            viewWorkoutBackground = (LinearLayout) itemView.findViewById(R.id.view_workout_background);
            viewWorkoutForeground = (LinearLayout) itemView.findViewById(R.id.view_workout_foreground);
        }
    }
}
