package com.example.trainingdiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trainingdiary.fragments.AddAPIExerciseFragment;
import com.example.trainingdiary.fragments.AddExerciseFragment;
import com.example.trainingdiary.fragments.AddUserExerciseFragment;
import com.example.trainingdiary.fragments.AddWorkoutFragment;
import com.example.trainingdiary.fragments.AllExercisesFragment;
import com.example.trainingdiary.fragments.AllWorkoutsFragment;

import com.example.trainingdiary.fragments.ImproveTechniqueFragment;
import com.example.trainingdiary.objectsClasses.Exercise;
import com.example.trainingdiary.objectsClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText edtExerciseName, autoCompleteTextView;
    BottomNavigationView bottomNav;
    DatabaseReference reff;
    FirebaseAuth mAuth;
    int currentMenuItem = R.id.nav_improve_technique;
    String currentLocation;
    User user = new User();
    AddWorkoutFragment addWorkoutFragment  = new AddWorkoutFragment();
    private static final String CURRENT_LOCATION = "current_location";
    private static final String CURRENT_MENU_ITEM = "current_menu_item";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_LOCATION, currentLocation);
        outState.putInt(CURRENT_MENU_ITEM, currentMenuItem);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentMenuItem = savedInstanceState.getInt(CURRENT_MENU_ITEM);
        bottomNav.setSelectedItemId(currentMenuItem);
        if(savedInstanceState.getString(CURRENT_LOCATION) != null){
            Bundle result = new Bundle();
            result.putString("bundleKey", savedInstanceState.getString(CURRENT_LOCATION));
            getSupportFragmentManager().setFragmentResult("requestKey", result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        user.setId(mAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(user.getId())){
                        User value = dataSnapshot.getValue(User.class);
                        user.setEmail(value.getEmail());
                        user.setName(value.getName());
                        user.setSurname(value.getSurname());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reff.child("Exercises").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercise.setId(dataSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomNav = findViewById(R.id.bottom_navigation_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(currentMenuItem);

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("bundleKey");
                switch(result){
                    case "addExerciseButtonClicked":
                        currentLocation = "addExerciseButtonClicked";
                        addExerciseToDatabase();
                        break;
                    case "addUserExerciseButtonClicked":
                        currentLocation = "addUserExerciseButtonClicked";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUserExerciseFragment()).commit();
                        break;
                    case "addNewWorkoutButtonClicked":
                        currentLocation = "addNewWorkoutButtonClicked";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addWorkoutFragment).commit();
                        break;
                    case "addAPIExerciseButtonClicked":
                        currentLocation = "addAPIExerciseButtonClicked";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddAPIExerciseFragment()).commit();
                        break;
                    case "addWorkoutButtonClicked":
                        currentLocation = "addWorkoutButtonClicked";
                        addWorkoutFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllWorkoutsFragment()).commit();
                        break;
                    case "addExerciseActionButtonClicked":
                        currentLocation = "addExerciseActionButtonClicked";
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddExerciseFragment()).commit();
                        break;
                    case "addAPIExerciseToWorkoutButtonClicked":
                        currentLocation = "addAPIExerciseToWorkoutButtonClicked";
                        addWorkoutFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addWorkoutFragment).commit();
                        break;
                }
            }
        });



    }




    private void addExerciseToDatabase() {

        edtExerciseName = (EditText) findViewById(R.id.edtExerciseName);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        String exerciseName = edtExerciseName.getText().toString().trim();
        String musclePart = autoCompleteTextView.getText().toString().trim();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("Exercises");

        if(exerciseName.isEmpty()){
            edtExerciseName.setError("Pole nazwa nie może być puste!");
            edtExerciseName.requestFocus();
            return;
        }
        if(musclePart.isEmpty()){
            autoCompleteTextView.setError("Pole główna partia mięśniowa nie może być puste!");
            autoCompleteTextView.requestFocus();
            return;
        }

        Exercise exercise = new Exercise(exerciseName, musclePart);

        reff.push().setValue(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Dodano ćwiczenie", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllExercisesFragment()).commit();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_improve_technique:
                            currentLocation = null;
                            currentMenuItem =  R.id.nav_improve_technique;
                            selectedFragment = new ImproveTechniqueFragment();
                            break;
                        case R.id.nav_workouts:
                            currentLocation = null;
                            currentMenuItem =  R.id.nav_workouts;
                            selectedFragment = new AllWorkoutsFragment();
                            break;
                        case R.id.nav_exercises:
                            currentLocation = null;
                            currentMenuItem =  R.id.nav_exercises;
                            selectedFragment = new AllExercisesFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;}
            };

}