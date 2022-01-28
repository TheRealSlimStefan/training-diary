package com.example.trainingdiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trainingdiary.R;
import com.example.trainingdiary.databinding.FragmentAddExerciseBinding;

public class AddExerciseFragment extends Fragment {

    Button confirm;
    FragmentAddExerciseBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddExerciseBinding.inflate(inflater, container, false);

        String[] muscleParts = getResources().getStringArray(R.array.parts);
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_muscle_parts_item, muscleParts);

       binding.autoCompleteTextView.setAdapter(arrayAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirm = (Button)  getView().findViewById(R.id.confirmExerciseButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "addExerciseButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }});
    }

}