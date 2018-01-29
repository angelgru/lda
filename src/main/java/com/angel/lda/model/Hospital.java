package com.angel.lda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Angel on 12/27/2017.
 */

@Entity
@Table(name = "hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "network_address")
    @NotNull
    private String networkAddress;
    @OneToOne
    private Location location;
    @JsonIgnore
    @OneToMany(mappedBy = "providedByHospital", cascade = CascadeType.ALL)
    private Set<SensorSyncApplication> providesSensorSyncApplications;
    @JsonIgnore
    @OneToMany(mappedBy = "worksAtHospital", cascade = CascadeType.ALL)
    private Set<User> hasEmployeesUsers;

    public Hospital() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(String networkAddress) {
        this.networkAddress = networkAddress;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<SensorSyncApplication> getProvidesSensorSyncApplications() {
        return providesSensorSyncApplications;
    }

    public void setProvidesSensorSyncApplications(Set<SensorSyncApplication> providesSensorSyncApplications) {
        this.providesSensorSyncApplications = providesSensorSyncApplications;
    }

    public Set<User> getHasEmployeesUsers() {
        return hasEmployeesUsers;
    }

    public void setHasEmployeesUsers(Set<User> hasEmployeesUsers) {
        this.hasEmployeesUsers = hasEmployeesUsers;
    }
}
