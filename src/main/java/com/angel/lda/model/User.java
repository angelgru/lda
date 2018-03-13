package com.angel.lda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Angel on 12/27/2017.
 */

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email")
    @NotNull
    private String email;
    @Column(name = "password")
    @NotNull
    private String password;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "emergency_phone_number")
    private String emergencyPhone;
    @Column(name = "active")
    private int active;
    @Column(name = "doctor")
    @NotNull
    private int doctor;
    @ManyToOne
    public Hospital worksAtHospital;
    @ManyToOne
    private SensorSyncApplication usesSensorSyncApplication;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Sensor> ownsSensors;
    @JsonIgnore
    @OneToMany(mappedBy = "forPatient", cascade = CascadeType.ALL)
    private Set<Treatment> userTreatments;
    @JsonIgnore
    @OneToMany(mappedBy = "hasDoctor", cascade = CascadeType.ALL)
    private Set<Treatment> isDoctorForTreatments;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public Hospital getWorksAtHospital() {
        return worksAtHospital;
    }

    public void setWorksAtHospital(Hospital worksAtHospital) {
        this.worksAtHospital = worksAtHospital;
    }

    public SensorSyncApplication getUsesSensorSyncApplication() {
        return usesSensorSyncApplication;
    }

    public void setUsesSensorSyncApplication(SensorSyncApplication usesSensorSyncApplication) {
        this.usesSensorSyncApplication = usesSensorSyncApplication;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDoctor() {
        return doctor;
    }

    public void setDoctor(int doctor) {
        this.doctor = doctor;
    }

    public Set<Sensor> getOwnsSensors() {
        return ownsSensors;
    }

    public void setOwnsSensors(Set<Sensor> ownsSensors) {
        this.ownsSensors = ownsSensors;
    }

    public Set<Treatment> getUserTreatments() {
        return userTreatments;
    }

    public void setUserTreatments(Set<Treatment> userTreatments) {
        this.userTreatments = userTreatments;
    }

    public Set<Treatment> getIsDoctorForTreatments() {
        return isDoctorForTreatments;
    }

    public void setIsDoctorForTreatments(Set<Treatment> isDoctorForTreatments) {
        this.isDoctorForTreatments = isDoctorForTreatments;
    }
}
