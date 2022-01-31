package com.example.trainingdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.objectsClasses.Exercise;
import com.example.trainingdiary.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private ArrayList<Exercise> data;
    private boolean flag = false;

    public ExerciseAdapter(ArrayList<Exercise> data){
        this.data = data;
    }


    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.list_item_layout_exercise, parent, false);

        if(flag){
            TextView title =  (TextView) view.findViewById(R.id.txt_exercise_item);
            title.setText(R.string.no_exercises);
        }

        return new ExerciseAdapter.ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        if(!flag){
            String exercieName = data.get(position).getName();
            holder.txtView.setText((exercieName));

            String musclePart = data.get(position).getMusclePart();
            holder.txtViewMusclePart.setText((musclePart));

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

    public void restoreItem(Exercise exercise, int position){
        data.add(position, exercise);
        notifyItemInserted(position);
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView txtView, txtViewMusclePart;
        public LinearLayout viewBackground, viewForeground;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.txt_exercise_item);
            txtViewMusclePart = (TextView) itemView.findViewById(R.id.txt_exercise_item_part);
            viewBackground = (LinearLayout) itemView.findViewById(R.id.view_background);
            viewForeground = (LinearLayout) itemView.findViewById(R.id.view_foreground);
        }
    }
}
