package com.angel.lda.AccessControlMethods;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import org.joda.time.DateTime;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Angel on 1/28/2018.
 */
@Component
public class AccessControl {

//    A1 already implemented in the controllers

    //    A2 Users’ mobile and emergency phones are private
    public User A2(User user){
        user.setPassword(null);
        user.setPhoneNumber(null);
        user.setEmergencyPhone(null);
        return user;
    }

//    A3

//    U1 The users can access their own properties and the direct properties of the resources connected with them
//    Имплементирано преку findByEmail методот во UserService

//    U2 The users can manage their name, phone and emergency phone or email.
    public User U2(User user, User updateUser) {

        if(user.getName() != null) {
            updateUser.setName(user.getName());
        }

        if(user.getPhoneNumber() != null) {
            updateUser.setPhoneNumber(user.getPhoneNumber());
        }

        if(user.getEmergencyPhone() != null) {
            updateUser.setEmergencyPhone(user.getEmergencyPhone());
        }

        return updateUser;
    }

//    P1 The patients can access everything about the doctors from ex:hospital
    @PostFilter("filterObject.worksAtHospital.name.equals('hospital')")
    public List<User> P1(List<User> doctors) {
        for(User doctor: doctors) {
            doctor.setPassword(null);
        }

        return doctors;
    }

//    D1 The doctors can modify their patients’ measurements from their hospital’s network during office hours.
    public boolean D1(String doctorIpAddress, User doctor){
        DateTime dateTime = new DateTime();
        int hour = dateTime.getHourOfDay();
        System.out.println(hour);
        String[] doctorIP = null;
        String hospitalIPAddress = null;

        if(!doctorIpAddress.equals("0:0:0:0:0:0:0:1")) {
            doctorIP = doctorIpAddress.split(".");
            doctorIpAddress = doctorIP[0] + "." + doctorIP[1] + "." + doctorIP[2];
            hospitalIPAddress = doctor.getWorksAtHospital().getNetworkAddress();
        }


        if((doctorIpAddress.equals("0:0:0:0:0:0:0:1") || doctorIpAddress.equals(hospitalIPAddress)) && (hour >= 8 && hour <16)) {
            return true;
        }

        return false;
    }

//    D2 The doctors can not modify the measurements out of their treatment timespan
    public boolean D2(Treatment treatment) {
        return treatment.getDiagnosis() != null;
    }

//    TS1 Technical Staff can manage applications only for his/hers hospital.
    @PostFilter("filterObject.providedByHospital.id == #worksAtHospitalId")
    public List<SensorSyncApplication> TS1(List<SensorSyncApplication> sensorSyncApplication, int worksAtHospitalId){
        return sensorSyncApplication;
    }

//    SU1: The user ex:ben can generate reports
    @PreAuthorize("authentication.name.equals('ben@yahoo.com')")
    public boolean SU1(){
        return true;
    }

//    Doctor's can not set a diagnosis for treatment that doesn't belong to them
    public boolean checkConditionsForDiagnosis(Treatment treatment, String email) {
        return treatment.getHasDoctor().getEmail().equals(email);

    }

//    EM1 The doctor can access their patients emergency phone number during abnormal measurements.

}
