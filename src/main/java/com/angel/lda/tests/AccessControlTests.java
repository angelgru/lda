package com.angel.lda.tests;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.service.impl.AuthenticationService;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Created by Angel on 2/8/2018.
 */

// Тестирањето ми е со вистинска база, ако треба ќе пробам и преку Mockito да го направам
public class AccessControlTests {

    @Autowired
    AuthenticationService authenticationService;


    public AccessControlTests() {
    }

//    A2 Users’ mobile and emergency phones are private. The test should return null for phoneNumber and emergencyPhone
    @Test
    public void A2Test() {
        Response response = given().port(8090).auth().basic("smithjohnson@richmondcarecenter.com", "smithjohnson")
                .get("/api/treatment/");
        response.then().body("forPatient.phoneNumber", hasItems(nullValue())).body("forPatient.emergencyPhone", hasItems(nullValue()));
    }

//    U2 The users can manage their name, phone and emergency phone or email.
//    Because I am setting the password and that field is forbidden, the test should return error 403
    @Test
    public void U2Test() {
        User user = new User();
        user.setPassword("hello");
        Response response = given().contentType("application/json").port(8090).auth().basic("ben@yahoo.com", "wrongparameter").body(user).patch("/api/user/update");
        response.then().statusCode(403);
    }

//    Requires accepted treatment without diagnosis
    @Test
    public void checkConditionsForDiagnosisTest() {
        Treatment treatment = new Treatment();
        treatment.setDiagnosis("test diagnosis");
        Response response = given().contentType("application/json").port(8090).auth().basic("ben@yahoo.com", "wrongparameter").body(treatment).patch("/api/treatment/3");
        response.then().statusCode(403);
    }

//     P1 The patients can access everything about the doctors from ex:hospital
    @Test
    public void P1Test() {
        Response response = given().contentType("application/json").port(8090).auth().basic("angel@yahoo.com", "angel").get("/api/user/doctors");
        response.then().body("worksAtHospital.name", hasItems("hospital"));
    }

//    TS1 Technical Staff can manage applications only for his/hers hospital.
//    Бидејќи немам ентитет Technical Stuff го заменив ова со дозвола докторите да можат да ги гледаат само податоците за Sensor Sync Applications
//    од болницата во која работат
    @Test
    public void TS1Test() {
        Response response = given().port(8090).auth().basic("smithjohnson@richmondcarecenter.com", "smithjohnson").get("/api/sensorsyncapplication/");
        response.then().body("providedByHospital.id", hasItem(1));
    }

    @Test
    public void SU1Test() {
        Response response = given().port(8090).auth().basic("smithjohnson@richmondcarecenter.com", "smithjohnson").get("/api/general/generateReport");
        response.then().statusCode(403);
    }

//    SU1: The user ex:ben can generate reports
    @Test
    public void canTakeTreatmentsTest() {
        Response response = given().port(8090).auth().basic("angel@yahoo.com", "angel").get("/api/treatment/");
        response.then().statusCode(403);
    }

//    Users are now allowed to get treatments not for them using http://localhost:8090/api/treatment/{treatmentId}
    public void filterTreatmentsTest() {
        Response response = given().port(8090).auth().basic("smithjohnson@richmondcarecenter.com", "smithjohnson").get("/api/treatment/4");
        response.then().statusCode(403);
    }
}
