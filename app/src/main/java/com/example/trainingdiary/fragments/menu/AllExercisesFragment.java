package com.example.trainingdiary.fragments.menu;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trainingdiary.Exercise;
import com.example.trainingdiary.adapters.ExerciseAdapter;
import com.example.trainingdiary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllExercisesFragment extends Fragment {
    //todo wynieść wszystkie deklaracje przed funkcje

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_exercises, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView exercisesList = (RecyclerView) getView().findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Exercises");

        ArrayList<Exercise> exercises = new ArrayList<>();
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercises);
        exercisesList.setAdapter(exerciseAdapter);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    System.out.println(exercise);
                    exercises.add(exercise);
                }
                exerciseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}