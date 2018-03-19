package com.angel.lda.service;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface TreatmentService {

    List<Treatment> getAllNonTakenTreatments() throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException;

    List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    Treatment getTreatment(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    List<Treatment> createTreatment(Treatment treatment) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    List<Treatment> updateTreatment(Treatment treatment, int treatmentId, String doctorIPAddress) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;
}
