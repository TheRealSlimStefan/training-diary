package com.example.trainingdiary.fragments;

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
import com.example.trainingdiary.objectsClasses.Technique;
import com.example.trainingdiary.adapters.TechniqueAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ImproveTechniqueFragment extends Fragment {

    RecyclerView techniquesList;
    TechniqueAdapter techniqueAdapter;
    ArrayList<Technique> techniques = new ArrayList<Technique>();
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.improve_technique_title
        ));
        return inflater.inflate(R.layout.fragment_improve_technique, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        techniquesList = (RecyclerView) getView().findViewById(R.id.techniques_list);
        techniquesList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        techniques.add(new Technique(getString(R.string.technique_1_title),getString(R.string.technique_1_description), "https://www.youtube.com/watch?v=bbGuHx07EDc"));
        techniques.add(new Technique(getString(R.string.technique_2_title),getString(R.string.technique_2_description), "https://www.youtube.com/watch?v=NEduXlZ8zSk"));
        techniques.add(new Technique(getString(R.string.technique_3_title),getString(R.string.technique_3_description), "https://www.youtube.com/watch?v=9GYH8PJ6xA0&list=PLHgc7ECD-8nLQhky4NisgGULXgRd53elM&index=3"));

        techniqueAdapter = new TechniqueAdapter(this.getContext(), techniques);
        techniquesList.setAdapter(techniqueAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

}
