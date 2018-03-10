package com.angel.lda.accesscontrolmethods;

import com.angel.lda.exceptions.UserValuesExposed;
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

//    A1 The hospital’s and the application’s properties are publicly available for everyone
//    Имплементирано преку config->SecurityConfiguration

//    A2 Users’ mobile and emergency phones are private
    public boolean A2(User user){

        if(user.getPhoneNumber() != null) {
            throw new UserValuesExposed("User mobile and emergency phone values exposed publicly");
        }

        if(user.getEmergencyPhone() != null) {
            throw new UserValuesExposed("User mobile and emergency phone values exposed publicly");
        }

        if(user.getPassword() != null) {
            throw new UserValuesExposed("User password exposed!!!");
        }

        return true;
    }

//    U1 The users can access their own properties and the direct properties of the resources connected with them
//    Имплементирано преку findByEmail методот во UserService

//    U2 The users can manage their name, phone and emergency phone.
//    Ако е направена промена на недозволени полиња, истата нема да се зачува во базата и ќе се фрли грешка
    public boolean U2(User modifiedUser, User originalUser) {
        if(!originalUser.getPassword().equals(modifiedUser.getPassword()))
            return false;

        if(!originalUser.getEmail().equals(modifiedUser.getEmail()))
            return false;

        return true;
    }

//    P1 The patients can access everything about the doctors from ex:hospital
//    Филтрирање на листата од доктори при што ги враќам само оние кои работат во болницата со име Saint P.
    @PostFilter("filterObject.worksAtHospital.name.equals('Saint P.')")
    public List<User>  P1(List<User> doctors) {
        return doctors;
    }

//    D1 The doctors can modify their patients’ measurements from their hospital’s network during office hours.
//    Поставување на дијагноза за лекување може да се изврши само од 8 до 16 часот кога докторот е најавен на системот
//    од мрежата на болницата
//    Дополнителната проверка doctorIpAddress.equals("0:0:0:0:0:0:0:1") ми е бидејќи тестирам на localhost па ми ја дава IPv6 локалната адреса
    public boolean D1(String doctorIpAddress, User doctor){
        DateTime dateTime = new DateTime();
        int hour = dateTime.getHourOfDay();
        String[] doctorIP;
        String hospitalIPAddress = null;

        if(!doctorIpAddress.equals("0:0:0:0:0:0:0:1")) {
            doctorIP = doctorIpAddress.split(".");
            doctorIpAddress = doctorIP[0] + "." + doctorIP[1] + "." + doctorIP[2];
            hospitalIPAddress = doctor.getWorksAtHospital().getNetworkAddress();
        }

        if((doctorIpAddress.equals("0:0:0:0:0:0:0:1") || doctorIpAddress.equals(hospitalIPAddress)) && (hour >= 8 && hour <24)) {
            return true;
        }

        return false;
    }

//    D2 The doctors can not modify the measurements out of their treatment timespan
//    Овде исто така не бев сигурен за кое мерења, па исто го направив за поставување дијагноза,
//    ако веќе има поставено дијагноза за лекувањето, да неможе да се постави повторно
    public boolean D2(Treatment treatment) {
        return treatment.getDiagnosis() == null;
    }

//    TS1 Technical Staff can manage applications only for his/hers hospital.
//    Филтрирање на SensorSyncApplication објектите за да ги врати само оние кои припаѓаат на болницата каде работи вработениот
    @PostFilter("filterObject.providedByHospital.id == #worksAtHospitalId")
    public List<SensorSyncApplication> TS1(List<SensorSyncApplication> sensorSyncApplication, int worksAtHospitalId){
        return sensorSyncApplication;
    }

//    SU1: The user ex:ben can generate reports
//    Ако најавениот корисник е ben@mail.com тогаш може да генерира извештаии
    @PreAuthorize("authentication.name.equals('ben@mail.com')")
    public boolean SU1(){
        return true;
    }

//    Doctor's can not set a diagnosis for treatment that doesn't belong to them
//    Дополнителна полиса каде проверувам дали лекувањето е земено од докторот пред да може истиот да постави дијагноза
    public boolean isTreatmentTakenByDoctor(Treatment treatment, User user) {
        if(treatment == null)
            System.out.println("Treatment is null");
        return treatment.getHasDoctor().getEmail().equals(user.getEmail());
    }

//    Can take treatments
//    Only doctors are allowed to claim treatments
    public boolean canTakeTreatments(User user) {
        return (user.getDoctor() == 1);
    }

//    Users are now allowed to get treatments not for them by requesting an individual treatment,
//    using http://localhost:8090/api/treatment/{treatmentId}
    @PostFilter("filterObject.forPatient.id == #loggedInUser.id")
    public List<Treatment> filterTreatments(List<Treatment> treatments, User loggedInUser){
        return treatments;
    }

}
