package com.example.prototype2;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class PhotosItem {



    private ArrayList<Bitmap> bitmapList;

    public PhotosItem(ArrayList<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public ArrayList<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(ArrayList<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }
}
