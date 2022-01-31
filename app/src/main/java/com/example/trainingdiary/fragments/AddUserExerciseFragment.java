package com.example.trainingdiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.trainingdiary.objectsClasses.Exercise;
import com.example.trainingdiary.R;
import com.example.trainingdiary.databinding.FragmentAddUserExerciseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUserExerciseFragment extends Fragment {

    FragmentAddUserExerciseBinding binding;
    ArrayList<String> exercisesNameList = new ArrayList<String>();
    ArrayList<Exercise> exercisesList = new ArrayList<Exercise>();
    TextView txtName, txtMusclePart, txtDescription;
    Button confirm;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    SearchView searchView;
    private static final String EXERCISE_KEY = "exercise_key";
    private Exercise exercise = new Exercise();
    DatabaseReference reff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.select_exercise_title
        ));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddUserExerciseBinding.inflate(inflater, container, false);
        listView = binding.listView;
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName = binding.txtUserTitle;
        txtMusclePart = binding.txtUserMusclePart;
        txtDescription = binding.txtUserDescription;
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Exercises");
        arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, exercisesNameList);
        listView.setAdapter(arrayAdapter);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercisesNameList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    exercise = dataSnapshot.getValue(Exercise.class);
                    exercise.setId(dataSnapshot.getKey());
                    exercisesNameList.add(exercise.getName());
                    exercisesList.add(exercise);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        confirm = binding.btnConfirmUserExercise;
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "addAPIExerciseToWorkoutButtonClicked");
                result.putSerializable(EXERCISE_KEY, exercise);
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }});

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                searchView = (SearchView) item.getActionView();

                searchView.setQueryHint(getString(R.string.search_exercise));
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        listView.setVisibility(View.VISIBLE);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        listView.setVisibility(View.GONE);
                        return true;
                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        arrayAdapter.getFilter().filter(newText);
                        return false;
                    }

                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int index = exercisesNameList.indexOf(arrayAdapter.getItem(position));
                        txtName.setText(exercisesList.get(index).getName());
                        txtMusclePart.setText(exercisesList.get(index).getMusclePart());
                        exercise.setName(exercisesList.get(index).getName());
                        exercise.setMusclePart(exercisesList.get(index).getMusclePart());

                        getActivity().setTitle(getString(R.string.add_exercise_title
                        ));
                        item.collapseActionView();
                    }
                });

                return true;
        }
        return false;
    }
}