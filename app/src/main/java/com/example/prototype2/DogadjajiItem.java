package com.example.prototype2;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.util.Base64;

public class DogadjajiItem {
    private String dataTitle;

    private String dataDate;
    private String dataTime;
    private String eventTip;
    private String eventDetail;
    private Bitmap dataImage;


    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDate;
    }



    public String getDataLang() {
        return dataTime;
    }
    public String  getEventTip(){
        return eventTip;
    }

    public String getEventDetail(){
        return eventDetail;
    }


    public Bitmap getDataImage() {
        return dataImage;
    }

    public DogadjajiItem(String dataTitle, String dataDate, String dataTime, Bitmap dataImage, String eventTip, String eventDetail) {
        this.dataTitle = dataTitle;
        this.dataDate = dataDate;
        this.dataTime = dataTime;
        this.dataImage = dataImage;
        this.eventTip = eventTip;
        this.eventDetail = eventDetail;
    }

    public void setOpis(String updatedDetails) {
        this.eventDetail = updatedDetails;

    }

    public void setDataImage(Bitmap updatedBitmap) {
        this.dataImage = updatedBitmap;
    }

    public void setDataTitle(String updatedItem) {
        this.dataTitle = updatedItem;
    }
}


