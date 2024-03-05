package com.example.prototype2;

import java.io.Serializable;

public class Events implements Serializable {

    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String encodedImage;
    private String eventTip;
    private String eventDetail;


    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String  getEventTip(){
        return eventTip;
    }

    public String getEventDetail(){
        return eventDetail;
    }
    public String getEncodedImage() {
        return encodedImage;
    }


    public Events(String eventTitle, String eventDate, String eventTime, String encodedImage, String eventTip, String eventDetail) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.encodedImage = encodedImage;
        this.eventTip = eventTip;
        this.eventDetail = eventDetail;
    }


}
