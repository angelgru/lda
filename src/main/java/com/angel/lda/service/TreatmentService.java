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

    public List<Treatment> getAllNonTakenTreatments() throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException;

    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public Treatment getTreatment(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public Treatment createTreatment(Treatment treatment) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public Treatment updateTreatment(Treatment treatment, int treatmentId, String doctorIPAddress) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
