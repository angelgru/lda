package com.angel.lda.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Angel on 1/1/2018.
 */
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "latitude")
    @NotNull
    private double lat;
    @Column(name = "longitude")
    @NotNull
    private double longitude;

    public Location() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
