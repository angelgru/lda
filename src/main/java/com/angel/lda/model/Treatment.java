package com.angel.lda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Angel on 12/27/2017.
 */

@Entity
@Table(name = "treatment")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "treatment_from_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date from;
    @Column(name = "treatment_to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date to;
    @Column(name = "patient_request")
    @NotNull
    private String patientRequest;
    @Column(name = "diagnosis")
    private String diagnosis;
    @ManyToOne
    private User forPatient;
    @JsonIgnore
    @ManyToOne
    private User hasDoctor;

    @JsonIgnore
    private String tdbForPatientId;
    @JsonIgnore
    private String tdbHasDoctor;
    @JsonIgnore
    private String tdbFrom;
    @JsonIgnore
    private String tdbTo;


    public Treatment() {
    }

    public static Treatment copy(Treatment treatment) {
        Treatment copyTreatment = new Treatment();
        if(treatment.getDiagnosis() != null)
            copyTreatment.setDiagnosis(treatment.getDiagnosis());
        if(treatment.getForPatient() != null)
            copyTreatment.setForPatient(treatment.getForPatient());
        if(treatment.getPatientRequest() != null)
            copyTreatment.setPatientRequest(treatment.getPatientRequest());
        if(treatment.getFrom() != null)
            copyTreatment.setFrom(treatment.getFrom());
        if(treatment.getHasDoctor() != null)
            copyTreatment.setHasDoctor(treatment.getHasDoctor());
        if(treatment.getTo() != null)
            copyTreatment.setTo(treatment.getTo());
        if(treatment.getId() > -1)
            copyTreatment.setId(treatment.getId());
        return copyTreatment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getForPatient() {
        return forPatient;
    }

    public void setForPatient(User forPatient) {
        this.forPatient = forPatient;
    }

    public User getHasDoctor() {
        return hasDoctor;
    }

    public void setHasDoctor(User hasDoctor) {
        this.hasDoctor = hasDoctor;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getPatientRequest() {
        return patientRequest;
    }

    public void setPatientRequest(String patientRequest) {
        this.patientRequest = patientRequest;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTdbForPatientId() {
        return tdbForPatientId;
    }

    public void setTdbForPatientId(String tdbForPatientId) {
        this.tdbForPatientId = tdbForPatientId;
    }

    public String getTdbFrom() {
        return tdbFrom;
    }

    public void setTdbFrom(String tdbFrom) {
        this.tdbFrom = tdbFrom;
    }

    public String getTdbTo() {
        return tdbTo;
    }

    public void setTdbTo(String tdbTo) {
        this.tdbTo = tdbTo;
    }

    public String getTdbHasDoctor() {
        return tdbHasDoctor;
    }

    public void setTdbHasDoctor(String tdbHasDoctor) {
        this.tdbHasDoctor = tdbHasDoctor;
    }
}
