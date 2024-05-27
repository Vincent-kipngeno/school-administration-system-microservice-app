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
    private AcademicTermService academicTermService;

    @Autowired
    private A_YearTermService aYearTermService;


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
            AcademicTerm academicTerm = academicTermService.getAcademicTermById(termRequest.termId());
            A_YearTerm aYearTerm = A_YearTerm.builder()
                    .year(academicYear)
                    .term(academicTerm)
                    .startDate(termRequest.startDate())
                    .endDate(termRequest.endDate())
                    .build();
            aYearTermService.save(aYearTerm);
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

    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

    public AcademicYear getAcademicYearById(Integer id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicYear not found"));
    }

}

