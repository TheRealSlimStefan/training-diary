package com.example.trainingdiary;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trainingdiary.fragments.menu.AddExerciseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExercisesDataService {

    Context context;

    public ExercisesDataService(Context context) {
        this.context = context;
    }

    public String[] getExercises(){
        String url ="https://wger.de/api/v2/exercise/?format=json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("JESTEM JESTEM");
                JSONArray exercises =null;
                JSONObject exercise = null;
                String exerciseName = "";
                try {
                    exercises = response.getJSONArray("results");
                    for (int i = 0; i < exercises.length(); i++) {
                        exerciseName = exercises.getJSONObject(i).getString("name");
                        System.out.println(exerciseName);

                    }
                    exerciseName = exercises.getJSONObject(0).getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(AddExerciseFragment.this.getContext(), exerciseName, Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddExerciseFragment.this.getContext(), "ERROR", Toast.LENGTH_LONG).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);
        return null;
    }

//    public String getExerciseID(String name){
//        String url ="https://wger.de/api/v2/exercise/?format=json";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                JSONArray exercises =null;
//                JSONObject exercise = null;
//                String exerciseName = "";
//                try {
//                    exercises = response.getJSONArray("results");
//                    exerciseName = exercises.getJSONObject(0).getString("name");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(AddExerciseFragment.this.getContext(), exerciseName, Toast.LENGTH_LONG).show();
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AddExerciseFragment.this.getContext(), "ERROR", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        MySingleton.getInstance(AddExerciseFragment.this.getContext()).addToRequestQueue(request);
//
//    }

//    public List<ExerciseReportModel> getExerciseByID(String exerciseId){
//
//    }
//
//    public List<ExerciseReportModel> getExerciseByName(String exerciseName){
//
//    }
}
