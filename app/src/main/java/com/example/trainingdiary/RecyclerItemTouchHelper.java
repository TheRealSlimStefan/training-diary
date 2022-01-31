package com.example.trainingdiary;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.adapters.ExerciseAdapter;
import com.example.trainingdiary.adapters.WorkoutAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if(listener != null){
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition() );
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        String[] parts = viewHolder.toString().split("V");
        System.out.println(parts[0]);
        if(viewHolder != null && parts[0].equals("Exercise")) {
            View foregroundView = ((ExerciseAdapter.ExerciseViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }else
        if(viewHolder != null && parts[0].equals("Workout")){
            View foregroundView = ((WorkoutAdapter.WorkoutViewHolder)viewHolder).viewWorkoutForeground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            String[] parts = viewHolder.toString().split("V");
            System.out.println(parts[0]);
            if(parts[0].equals("Exercise")){
                View foregroundView = ((ExerciseAdapter.ExerciseViewHolder)viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }else
            if(parts[0].equals("Workout")){
                View foregroundView = ((WorkoutAdapter.WorkoutViewHolder)viewHolder).viewWorkoutForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
        }


    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        String[] parts = viewHolder.toString().split("V");
        System.out.println(parts[0]);
        if(viewHolder != null && parts[0].equals("Exercise")){
            View foregroundView = ((ExerciseAdapter.ExerciseViewHolder)viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);}
        else if(viewHolder != null && parts[0].equals("Workout")){
            View foregroundView = ((WorkoutAdapter.WorkoutViewHolder)viewHolder).viewWorkoutForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);}
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        String[] parts = viewHolder.toString().split("V");
        System.out.println(parts[0]);
        if(viewHolder != null && parts[0].equals("Exercise")){
            View foregroundView = ((ExerciseAdapter.ExerciseViewHolder)viewHolder).viewForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }else if(viewHolder != null && parts[0].equals("Workout")){
            View foregroundView = ((WorkoutAdapter.WorkoutViewHolder)viewHolder).viewWorkoutForeground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);}

    }
}
