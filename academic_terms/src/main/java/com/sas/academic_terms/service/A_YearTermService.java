package com.sas.academic_terms.service;

import com.sas.academic_terms.entity.A_YearTerm;
import com.sas.academic_terms.entity.A_YearTermId;
import com.sas.academic_terms.repository.A_YearTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class A_YearTermService {

    @Autowired
    private A_YearTermRepository a_yearTermRepository;

    public void save(A_YearTerm aYearTerm) {
        a_yearTermRepository.saveAndFlush(aYearTerm);
    }

    public A_YearTerm getCurrentA_YearTerm() {
        A_YearTerm currentTerm = a_yearTermRepository.findByIsCurrentTrue()
                .orElseThrow(() -> new RuntimeException("Current term not set"));

        Date currentDate = new Date();

        // Check if the current term's end date is greater than the current date
        if (currentTerm.getEndDate().after(currentDate)) {
            return currentTerm;
        }

        // Find the next earliest academic term with start date greater than the end date of the current term
        A_YearTerm nextTerm = a_yearTermRepository.findFirstByStartDateGreaterThanOrderByStartDateAsc(currentTerm.getEndDate())
                .orElse(currentTerm); // If there is no next term, return the current term

        // Update the retrieved term to be isCurrent = true
        nextTerm.setIsCurrent(true);
        a_yearTermRepository.save(nextTerm);

        // Update the previously retrieved term to isCurrent = false
        currentTerm.setIsCurrent(false);
        a_yearTermRepository.save(currentTerm);

        //TODO: Send message to fees service to update fee arrears for new term

        return nextTerm;
    }

    public List<A_YearTerm> getAllA_YearTerms() {
        return a_yearTermRepository.findAll();
    }

    public A_YearTerm getA_YearTermById(Integer yearId, Integer termId) {
        A_YearTermId id = new A_YearTermId(yearId, termId);
        return a_yearTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("A_YearTerm not found"));
    }
}
