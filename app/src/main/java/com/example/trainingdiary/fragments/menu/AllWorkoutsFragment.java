package com.example.trainingdiary.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.R;
import com.example.trainingdiary.adapters.WorkoutAdapter;

public class AllWorkoutsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_workouts, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView workoutsList = (RecyclerView) getView().findViewById(R.id.workoutsList);
        workoutsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        String[] languages = {"Polski", "Angielski", "Portugalski", "Włoski", "Angielski", "Portugalski", "Włoski", "Angielski", "Portugalski", "Włoski"};
        //String[] languages = {};
        workoutsList.setAdapter(new WorkoutAdapter(languages));


    }
}