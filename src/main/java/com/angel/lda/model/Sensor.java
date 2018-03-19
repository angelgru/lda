package com.angel.lda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Angel on 12/27/2017.
 */

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type")
    @NotNull
    private String type;
    @Column(name = "unit")
    @NotNull
    private String unit;
    @Column(name = "regular_from")
    private int regularFrom;
    @Column(name = "regular_to")
    private int regularTo;
    @JsonIgnore
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    private Set<Observation> usedInObservations;

    public Sensor() {
    }

    public static Sensor copy(Sensor sensor) {
        Sensor newSensor = new Sensor();
        newSensor.setUnit(sensor.getUnit());
        newSensor.setType(sensor.getType());
        newSensor.setRegularTo(sensor.getRegularTo());
        newSensor.setRegularFrom(sensor.getRegularFrom());
        newSensor.setOwner(sensor.getOwner());
        newSensor.setId(sensor.getId());
        newSensor.setUsedInObservations(sensor.getUsedInObservations());
        return newSensor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getRegularFrom() {
        return regularFrom;
    }

    public void setRegularFrom(int regularFrom) {
        this.regularFrom = regularFrom;
    }

    public int getRegularTo() {
        return regularTo;
    }

    public void setRegularTo(int regularTo) {
        this.regularTo = regularTo;
    }

    public Set<Observation> getUsedInObservations() {
        return usedInObservations;
    }

    public void setUsedInObservations(Set<Observation> usedInObservations) {
        this.usedInObservations = usedInObservations;
    }
}
