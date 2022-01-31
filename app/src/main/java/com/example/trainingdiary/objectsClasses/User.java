package com.example.trainingdiary.objectsClasses;

import com.example.trainingdiary.objectsClasses.Exercise;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String surname;
    private String email;
    private ArrayList<Exercise> Exercises;

    public User() {
    }

    public User(String id, String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Exercise> getUserExercises() {
        return Exercises;
    }

    public void setUserExercises(ArrayList<Exercise> userExercises) {
        this.Exercises = userExercises;
    }

    public void addUserExercises(Exercise exercise) {
        this.Exercises.add(exercise);
    }

    public void removeUserExercises(Exercise exercise) {
        this.Exercises.remove(exercise);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
