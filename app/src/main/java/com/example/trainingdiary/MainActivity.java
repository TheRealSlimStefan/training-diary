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
import com.example.trainingdiary.fragments.AddWorkoutFragment;
import com.example.trainingdiary.fragments.AllExercisesFragment;
import com.example.trainingdiary.fragments.AllWorkoutsFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText edtExerciseName, autoCompleteTextView;
    DatabaseReference reff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Firebase connection Succes", Toast.LENGTH_LONG).show();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationMenu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_add_workout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddWorkoutFragment()).commit();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                switch(result){
//                    case "result":
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddExerciseFragment()).commit();
//                        break;
                    case "addAPIExerciseButtonClicked":
                        System.out.println("TAK JESTEM KLIKNIETY");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddAPIExerciseFragment()).commit();
                        break;
                    case "addExerciseButtonClicked":
                        addExerciseToDatabase();
                        break;
                    case "addExerciseActionButtonClicked":
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddExerciseFragment()).commit();
                        break;

                }
                // Do something with the result
            }
        });

    }

    private void addExerciseToDatabase() {

        edtExerciseName = (EditText) findViewById(R.id.edtExerciseName);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        String exerciseName = edtExerciseName.getText().toString().trim();
        String musclePart = autoCompleteTextView.getText().toString().trim();

        reff = FirebaseDatabase.getInstance().getReference().child("Exercises");

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
                        case R.id.nav_add_workout:
                            selectedFragment = new AddWorkoutFragment();
                            break;
                        case R.id.nav_workouts:
                            selectedFragment = new AllWorkoutsFragment();
                            break;
                        case R.id.nav_exercises:
                            selectedFragment = new AllExercisesFragment();
                            break;
                        default:
                            selectedFragment = new AddWorkoutFragment();
                            break;
                    }
getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;}
            };

}