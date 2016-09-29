package com.example.thefe.newsmartkedex;

public class Item {
    int imageId;
    int soundId;
    private int soundID;

    Item(int imageId, int soundId) {
        this.imageId = imageId;
        this.soundId = soundId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSoundID() {
        return soundID;
    }

    public void setSoundID(int soundID) {
        this.soundID = soundID;
    }

}
