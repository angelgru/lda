package com.angel.lda.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Angel on 12/27/2017.
 */

@Entity
@Table(name = "sensor_sync_application")
public class SensorSyncApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name_of_application")
    @NotNull
    private String nameOfApplication;
    @ManyToOne
    private Hospital providedByHospital;
    @JsonIgnore
    @OneToMany(mappedBy = "usesSensorSyncApplication", cascade = CascadeType.ALL)
    private Set<User> usedByUsers;

    @JsonIgnore
    private String tdbProvidedByHospital;

    public SensorSyncApplication() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameOfApplication() {
        return nameOfApplication;
    }

    public void setNameOfApplication(String nameOfApplication) {
        this.nameOfApplication = nameOfApplication;
    }

    public Hospital getProvidedByHospital() {
        return providedByHospital;
    }

    public void setProvidedByHospital(Hospital providedByHospital) {
        this.providedByHospital = providedByHospital;
    }

    public Set<User> getUsedByUsers() {
        return usedByUsers;
    }

    public void setUsedByUsers(Set<User> usedByUsers) {
        this.usedByUsers = usedByUsers;
    }

    public String getTdbProvidedByHospital() {
        return tdbProvidedByHospital;
    }

    public void setTdbProvidedByHospital(String tdbProvidedByHospital) {
        this.tdbProvidedByHospital = tdbProvidedByHospital;
    }
}
