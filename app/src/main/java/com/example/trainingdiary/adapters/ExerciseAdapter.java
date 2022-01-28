package com.example.trainingdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.Exercise;
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
            TextView title =  (TextView) view.findViewById(R.id.txtExerciseItem);
            title.setText("Brak stworzonych ćwiczeń");
        }

        return new ExerciseAdapter.ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseViewHolder holder, int position) {
        if(!flag){
            String exercieName = data.get(position).getName();
            System.out.println(exercieName);
            holder.txtView.setText((exercieName));

            String musclePart = data.get(position).getMusclePart();
            System.out.println(musclePart);
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

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView txtView, txtViewMusclePart;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = (TextView) itemView.findViewById(R.id.txtExerciseItem);
            txtViewMusclePart = (TextView) itemView.findViewById(R.id.txtExerciseItemPart);
        }
    }
}
