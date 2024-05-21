package com.sas.academic_terms.service;

import com.sas.academic_terms.dto.AcademicYearRequest;
import com.sas.academic_terms.dto.AcademicYearTermRequest;
import com.sas.academic_terms.entity.A_YearTerm;
import com.sas.academic_terms.entity.A_YearTermId;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.repository.A_YearTermRepository;
import com.sas.academic_terms.repository.AcademicTermRepository;
import com.sas.academic_terms.repository.AcademicYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AcademicYearService {

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private AcademicTermRepository academicTermRepository;

    @Autowired
    private A_YearTermRepository a_yearTermRepository;

    public Long addAcademicYear(AcademicYearRequest request) {
        // Create AcademicYear entity
        AcademicYear academicYear = AcademicYear.builder()
                .year(getAcademicYear(request.terms()))
                .startDate(request.terms().get(0).startDate())
                .endDate(request.terms().get(2).endDate())
                .build();
        academicYearRepository.saveAndFlush(academicYear);

        // Save the terms for the academic year
        for (AcademicYearTermRequest termRequest : request.terms()) {
            AcademicTerm academicTerm = academicTermRepository.findById(termRequest.termId()).orElseThrow(() -> new RuntimeException("Term not found"));
            A_YearTerm aYearTerm = A_YearTerm.builder()
                    .year(academicYear)
                    .term(academicTerm)
                    .startDate(termRequest.startDate())
                    .endDate(termRequest.endDate())
                    .build();
            a_yearTermRepository.saveAndFlush(aYearTerm);
        }

        return academicYear.getId();
    }

    private String getAcademicYear(List<AcademicYearTermRequest> terms) {
        // Assuming the terms are ordered by termId (1, 2, 3)
        Date startDateTerm1 = terms.get(0).startDate();
        Date endDateTerm3 = terms.get(2).endDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTerm1);
        int startYear = cal.get(Calendar.YEAR);

        cal.setTime(endDateTerm3);
        int endYear = cal.get(Calendar.YEAR);

        if (startYear == endYear) {
            return String.valueOf(startYear);
        } else {
            return startYear + "/" + endYear;
        }
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

    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    public List<A_YearTerm> getAllA_YearTerms() {
        return a_yearTermRepository.findAll();
    }

    public A_YearTerm getA_YearTermById(Integer yearId, Integer termId) {
        A_YearTermId id = new A_YearTermId(yearId, termId);
        return a_yearTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("A_YearTerm not found"));
    }

    public AcademicYear getAcademicYearById(Integer id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicYear not found"));
    }

    public AcademicTerm getAcademicTermById(Integer id) {
        return academicTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicTerm not found"));
    }

}

