package com.example.trainingdiary.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainingdiary.R;
import com.example.trainingdiary.objectsClasses.Technique;

import java.util.ArrayList;

public class TechniqueAdapter extends RecyclerView.Adapter<TechniqueAdapter.TechniqueViewHolder> {

    private Context context;
    private ArrayList<Technique> techniques;
    private boolean flag = false;

    public TechniqueAdapter(Context context, ArrayList<Technique> techniques){
        this.techniques = techniques;
        this.context = context;
    }


    @NonNull
    @Override
    public TechniqueAdapter.TechniqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout_technique, parent, false);
        return new TechniqueAdapter.TechniqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TechniqueAdapter.TechniqueViewHolder holder, int position) {
        if(!flag){
            String techniqueTitle = techniques.get(position).getTitle();
            holder.txtTechniqueTitle.setText((techniqueTitle));

            String techniqueDescription = techniques.get(position).getDescription();
            holder.txtTechniqueDescription.setText((techniqueDescription));

            String url = techniques.get(position).getUrl();
            holder.imgYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoUrl(url);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(techniques.size() == 0) {
            flag = true;
            return 1;
        }else{
            flag = false;
            return techniques.size();
        }
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    public class TechniqueViewHolder extends RecyclerView.ViewHolder{
        TextView txtTechniqueTitle, txtTechniqueDescription;
        ImageView imgYoutube;

        public TechniqueViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTechniqueTitle = itemView.findViewById(R.id.txt_technique_title);
            txtTechniqueDescription = itemView.findViewById(R.id.txt_technique_description);
            imgYoutube = itemView.findViewById(R.id.icon_youtube);
        }
    }
}