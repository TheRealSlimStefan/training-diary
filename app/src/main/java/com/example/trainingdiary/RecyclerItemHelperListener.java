package com.example.trainingdiary;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerItemHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
