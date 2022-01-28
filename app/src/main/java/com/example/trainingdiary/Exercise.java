package com.example.trainingdiary;

public class Exercise {
    String id;
    String name;
    String musclePart;

    public Exercise() {
    }

    public Exercise(String name, String musclePart) {
        this.name = name;
        this.musclePart = musclePart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMusclePart() {
        return musclePart;
    }

    public void setMusclePart(String musclePart) {
        this.musclePart = musclePart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
