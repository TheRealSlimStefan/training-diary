package com.example.trainingdiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.trainingdiary.ExercisesDataService;
import com.example.trainingdiary.R;
import com.example.trainingdiary.databinding.FragmentAddApiExerciseBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AddAPIExerciseFragment extends Fragment{

    FragmentAddApiExerciseBinding binding;
    ExercisesDataService exercisesDataService;
    ArrayList<String> exercisesList = new ArrayList<>();
    Button confirm;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddApiExerciseBinding.inflate(inflater, container, false);
        listView = binding.listView;

        exercisesDataService = new ExercisesDataService(AddAPIExerciseFragment.this.getContext());

        exercisesDataService.getExercises(new ExercisesDataService.VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(AddAPIExerciseFragment.this.getContext(), "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JSONArray[] response) {
                Toast.makeText(AddAPIExerciseFragment.this.getContext(), "on response", Toast.LENGTH_SHORT).show();
                String exerciseName = "";
                for (int i = 0; i < response[0].length() - 1 ; i++) {
                    try {
                        exerciseName = response[0].getJSONObject(i).getString("title");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    exercisesList.add(exerciseName);
                }
                arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, exercisesList);
                listView.setAdapter(arrayAdapter);
            }
        });
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        confirm = (Button)  getView().findViewById(R.id.confirmExerciseButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "addExerciseButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }});
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public void onOptionsMenuClosed(@NonNull Menu menu) {
        super.onOptionsMenuClosed(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        System.out.println(exercisesList);
                        arrayAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                return true;
        }
        return false;
    }
}