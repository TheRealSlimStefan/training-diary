package com.example.trainingdiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.objectsClasses.Exercise;
import com.example.trainingdiary.R;
import com.example.trainingdiary.RecyclerItemHelperListener;
import com.example.trainingdiary.RecyclerItemTouchHelper;
import com.example.trainingdiary.objectsClasses.Workout;
import com.example.trainingdiary.adapters.WorkoutAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllWorkoutsFragment extends Fragment implements RecyclerItemHelperListener {
    FloatingActionButton btnAddNewWorkout;
    RecyclerView workoutsList;
    DatabaseReference reff;
    ArrayList<Workout> workouts = new ArrayList<>();
    WorkoutAdapter workoutAdapter;
    View rootLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddNewWorkout = (FloatingActionButton) getView().findViewById(R.id.btn_add_new_workout);
        btnAddNewWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "addNewWorkoutButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }});

        workoutsList = (RecyclerView) getView().findViewById(R.id.list_workouts);
        workoutsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        workoutsList.setItemAnimator(new DefaultItemAnimator());
        workoutsList.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Trainings");

        workoutAdapter = new WorkoutAdapter(workouts);
        workoutsList.setAdapter(workoutAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(workoutsList);



        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workouts.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Workout workout = new Workout();
                    for(DataSnapshot dataSnapshotExercise: dataSnapshot.getChildren()){
                        if(dataSnapshotExercise.getKey().equals("name")){
                            workout.setName(dataSnapshotExercise.getValue(String.class));
                        }else{
                            Exercise exercise = dataSnapshotExercise.getValue(Exercise.class);
                            workout.addExercise(exercise);
                        }
                    }
                    workout.setId(dataSnapshot.getKey());
                    workouts.add(workout);
                }
                workoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    final boolean[] clickedUndo = {false};
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof WorkoutAdapter.WorkoutViewHolder && (workouts.size()!=0)){

            String title = workouts.get(viewHolder.getAdapterPosition()).getName();

            Workout deletedWorkout = workouts.get(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();

            workoutAdapter.removeItem(deleteIndex);

            rootLayout = getView();

            Snackbar snackbar = Snackbar.make(rootLayout, title + " removed ", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Cofnij", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedUndo[0] = true;
                    workoutAdapter.restoreItem(deletedWorkout, deleteIndex);
                }

            });
            snackbar.setActionTextColor(Color.RED);
            if(workouts.size() != 0){
                snackbar.show();}
            else{

                reff.child(deletedWorkout.getId()).removeValue();
            }
            snackbar.addCallback( new Snackbar.Callback(){
                                      @Override
                                      public void onDismissed(Snackbar transientBottomBar, int event) {
                                          if(!clickedUndo[0]){
                                              reff.child(deletedWorkout.getId()).removeValue();
                                          }
                                          super.onDismissed(transientBottomBar, event);
                                      }
                                  }
            );
        }
    }
}