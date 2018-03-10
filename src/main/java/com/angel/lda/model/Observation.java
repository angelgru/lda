package com.angel.lda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Angel on 12/27/2017.
 */
@Entity
@Table(name = "observation")
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "value")
    @NotNull
    private String val;
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date time;
    @JsonIgnore
    @ManyToOne
    private Sensor sensor;

    @JsonIgnore
    private String tdbSensorId;

    public Observation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTdbSensorId() {
        return tdbSensorId;
    }

    public void setTdbSensorId(String tdbSensorId) {
        this.tdbSensorId = tdbSensorId;
    }
}
