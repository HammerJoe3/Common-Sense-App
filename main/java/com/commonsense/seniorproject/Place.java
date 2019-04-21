package com.commonsense.seniorproject;

public class Place {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String type;
    private String treatDate;
    private String renewalDate;

    public Place(String name, String address, double latitude, double longitude, String type, String treatDate, String renewalDate){
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.treatDate = treatDate;
        this.renewalDate = renewalDate;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRenewalDate() {
        return renewalDate;
    }

    public String getTreatDate() {
        return treatDate;
    }

    public String getType() {
        return type;
    }
}
