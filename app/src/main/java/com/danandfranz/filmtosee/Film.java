package com.danandfranz.filmtosee;


public class Film {

    private String name;
    private int imageSource;

    public Film(int imageSource, String name) {
        this.name = name;
        this.imageSource = imageSource;
    }

    public String getName() {
        return name;
    }

    public int getImageSource() {
        return imageSource;
    }
}