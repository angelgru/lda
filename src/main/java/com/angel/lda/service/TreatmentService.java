package com.angel.lda.service;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface TreatmentService {

    public List<Treatment> getAllNonTakenTreatments() throws IOException;

    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor();

    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor();

    public Treatment getTreatment(int id);

    public Treatment createTreatment(Treatment treatment);

    public Treatment updateTreatment(Treatment treatment, int treatmentId, String doctorIPAddress);
}
