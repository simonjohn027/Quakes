package com.driven.quakes;

/**
 *
 * Author Simon John Sanga
 * S1803451
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Quake implements Parcelable {

    private Date date;
    private String name;
    private String description;
    private String location;
    private float magnitude;
    private String depth;
    private String category;
    private String source;
    private String lon;
    private String lat;



    public Quake(Date date, String name, String description, String location, float magnitude, String depth, String source, String lon, String lat, String category) {
        this.date = date;
        this.name = name;
        this.description = description;
        this.location = location;
        this.magnitude = magnitude;
        this.depth = depth;
        this.source = source;
        this.lon = lon;
        this.lat = lat;
        this.category = category;
    }

    public Quake(){
        this.date = null;
        this.name = "";
        this.description = "";
        this.location = "";
        this.magnitude = 0 ;
        this.depth = "";
        this.source = "";
        this.lon = "";
        this.category = "";
        this.lat = "";

    }




    public void setDate(String date) {
        try {
            Date aDate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(date);
            this.date = aDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMagnitude(float magnitude) {

        this.magnitude = magnitude;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public String getDepth() {
        return this.depth;
    }

    public String getSource() {
        return source;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Quake{" +
                "date=" + date +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", magnitude=" + magnitude +
                ", depth='" + depth + '\'' +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }

    public Quake(Parcel parcel){
        this.date = new Date(parcel.readLong());
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.location = parcel.readString();
        this.magnitude = parcel.readFloat();
        this.depth = parcel.readString();
        this.source = parcel.readString();
        this.lon = parcel.readString();
        this.lat = parcel.readString();
        this.category = parcel.readString();

    }

    public static final Parcelable.Creator<Quake> CREATOR = new Parcelable.Creator<Quake>() {
        @Override
        public Quake createFromParcel(Parcel parcel) {
            return new Quake(parcel);
        }

        @Override
        public Quake[] newArray(int size) {
            return new Quake[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.location);
        dest.writeFloat(this.magnitude);
        dest.writeString(this.depth);
        dest.writeString(this.source);
        dest.writeString(this.lon);
        dest.writeString(this.lat);
        dest.writeString(this.category);
    }
}