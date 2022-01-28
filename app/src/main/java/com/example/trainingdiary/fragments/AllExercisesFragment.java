package com.example.trainingdiary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trainingdiary.Exercise;
import com.example.trainingdiary.RecyclerItemHelperListener;
import com.example.trainingdiary.RecyclerItemTouchHelper;
import com.example.trainingdiary.adapters.ExerciseAdapter;
import com.example.trainingdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllExercisesFragment extends Fragment implements RecyclerItemHelperListener {
    RecyclerView exercisesList;
    DatabaseReference reff;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ExerciseAdapter exerciseAdapter;
    Exercise exercise;
    FloatingActionButton addExerciseButton;
    View rootLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.your_exercises_title
        ));
        return inflater.inflate(R.layout.fragment_all_exercises, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addExerciseButton = (FloatingActionButton) getView().findViewById(R.id.addExerciseActionButton);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("bundleKey", "addExerciseActionButtonClicked");
                getParentFragmentManager().setFragmentResult("requestKey", result);
            }
        });


        exercisesList = (RecyclerView) getView().findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        exercisesList.setItemAnimator(new DefaultItemAnimator());
        exercisesList.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        reff = FirebaseDatabase.getInstance().getReference("Exercises");

        exerciseAdapter = new ExerciseAdapter(exercises);
        exercisesList.setAdapter(exerciseAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(exercisesList);



        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    exercise = dataSnapshot.getValue(Exercise.class);
                    exercise.setId(dataSnapshot.getKey());
                    exercises.add(exercise);
                }
                exerciseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ExerciseAdapter.ExerciseViewHolder){

            String title = exercises.get(viewHolder.getAdapterPosition()).getName();
            final boolean[] clickedUndo = {false};

            Exercise deletedExercise = exercises.get(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();

            exerciseAdapter.removeItem(deleteIndex);

            rootLayout = getView();

            Snackbar snackbar = Snackbar.make(rootLayout, title + " removed ", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Cofnij", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedUndo[0] = true;
                    exerciseAdapter.restoreItem(deletedExercise, deleteIndex);
                }

            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.addCallback( new Snackbar.Callback(){
                                      @Override
                                      public void onDismissed(Snackbar transientBottomBar, int event) {
                                          if(!clickedUndo[0]){

                                              reff.child(deletedExercise.getId()).removeValue();

                                          }
                                          super.onDismissed(transientBottomBar, event);
                                      }
                                  }

            );

        }

    }
}