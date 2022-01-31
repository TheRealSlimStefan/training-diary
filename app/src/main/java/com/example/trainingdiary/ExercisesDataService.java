package com.example.trainingdiary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExercisesDataService {

    Context context;

    public ExercisesDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        public void onError(String message);
        public void onResponse(JSONArray[] response);
    }

    public JSONArray getExercises(VolleyResponseListener volleyResponseListener){
        String url ="https://ancient-springs-50859.herokuapp.com/exercises";
        final JSONArray[] exercises = {null};
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    exercises[0] = response.getJSONArray("exercises");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(exercises);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseListener.onError("error");
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);
        return exercises[0];
    }
}
