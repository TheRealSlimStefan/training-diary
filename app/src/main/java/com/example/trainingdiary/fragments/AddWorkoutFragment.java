package com.example.trainingdiary.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.objectsClasses.Exercise;
import com.example.trainingdiary.MainActivity;
import com.example.trainingdiary.objectsClasses.Workout;
import com.example.trainingdiary.adapters.ExerciseAdapter;
import com.example.trainingdiary.databinding.FragmentAddWorkoutBinding;

import com.example.trainingdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddWorkoutFragment extends Fragment implements View.OnClickListener{
    FragmentAddWorkoutBinding binding;
    Button btnAddUserExercise, btnAddAPIExercise, btnConfirm;
    EditText edtTxtTitle;
    RecyclerView addedExercisesList;
    DatabaseReference reff;
    FirebaseAuth mAuth;
    private static final String EXERCISE_KEY = "exercise_key";
    private static final String EXERCISES = "exercises_key";
    private static final String NEW_WORKOUT = "new";
    Exercise exercise;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ExerciseAdapter exerciseAdapter;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXERCISES, exercises);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            exercises.addAll((ArrayList<Exercise>)savedInstanceState.getSerializable(EXERCISES));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.add_training_title
        ));
        binding = FragmentAddWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtTxtTitle = binding.editTextWorkoutTitle;
        btnAddUserExercise = binding.btnAddUserExercise;
        btnAddAPIExercise = binding.btnAddApiExercise;
        btnConfirm = binding.btnAddWorkout;
        btnAddUserExercise.setOnClickListener(this);
        btnAddAPIExercise.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        if(getArguments() != null){
            if(getArguments().getBoolean(NEW_WORKOUT)){
                exercises.clear();
            }else{
                exercise = (Exercise) getArguments().getSerializable(EXERCISE_KEY);
                exercises.add(exercise);
            }

        }
        addedExercisesList = binding.addedToTrainingList;
        addedExercisesList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        exerciseAdapter = new ExerciseAdapter(exercises);
        addedExercisesList.setAdapter(exerciseAdapter);

    }

    @Override
    public void onClick(View v) {
        Bundle result = new Bundle();
        switch(v.getId()){
            case R.id.btn_add_user_exercise:
                result.putString("bundleKey", "addUserExerciseButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
                break;
            case R.id.btn_add_api_exercise:
                result.putString("bundleKey", "addAPIExerciseButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
                break;
            case R.id.btn_add_workout:
                addWorkoutToDatabase();
                showNotification();
                result.putString("bundleKey", "addWorkoutButtonClicked");
                result.putBoolean(NEW_WORKOUT, true);
                getParentFragmentManager().setFragmentResult("requestKey", result);
                break;
        }

    }



    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my notification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddWorkoutFragment.this.getContext(),"my notification")
                .setSmallIcon(R.drawable.ic_baseline_school_24)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_description))
                .setAutoCancel(true);

        Intent intent = new Intent(AddWorkoutFragment.this.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", "message");
        PendingIntent pendingIntent = PendingIntent.getActivity(AddWorkoutFragment.this.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void addWorkoutToDatabase() {
        String title = edtTxtTitle.getText().toString().trim();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getInstance().getCurrentUser().getUid()).child("Trainings");

        Workout workout = new Workout(title);
        DatabaseReference reffToPush = reff.push();

        reffToPush.setValue(workout);

        for(int i = 0; i < exercises.size(); i++) {
            reff.child(reffToPush.getKey()).push().setValue(exercises.get(i));
        }

        edtTxtTitle.setText("");
    }
}
