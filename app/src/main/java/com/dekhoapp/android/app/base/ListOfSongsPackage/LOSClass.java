package com.dekhoapp.android.app.base.ListOfSongsPackage;

public class LOSClass {

    private String name,number;
    int song_image;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LOSClass() {
    }

    public LOSClass(String name, int song_image) {
        this.name = name;
        this.song_image = song_image;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getSong_image() {
        return song_image;
    }

    public void setSong_image(int song_image) {
        this.song_image = song_image;
    }
}
