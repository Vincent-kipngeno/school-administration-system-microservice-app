package com.sas.academic_terms.service;

import com.sas.academic_terms.dto.AcademicYearRequest;
import com.sas.academic_terms.dto.AcademicYearTermRequest;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.repository.AcademicYearRepository;
import com.sas.clients.academic_terms.AcademicYearResponse;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final AcademicTermService academicTermService;
    private final AcademicYearTermService aYearTermService;


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
            AcademicYearTerm aYearTerm = AcademicYearTerm.builder()
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

    public List<AcademicYearResponse> getAllAcademicYears() {
        return academicYearRepository.findAll()
                .stream()
                .map(this::buildAcademicYearResponse)
                .collect(Collectors.toList());
    }

    public AcademicYearResponse getAcademicYearById(Integer id) {
        return buildAcademicYearResponse(
                academicYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicYear not found"))
        );
    }

    public AcademicYearResponse buildAcademicYearResponse(AcademicYear academicYear) {
        return AcademicYearResponse.builder()
                .id(academicYear.getId())
                .startDate(academicYear.getStartDate())
                .endDate(academicYear.getEndDate())
                .year(academicYear.getYear())
                .yearTerms(
                        academicYear.getYearTerms()
                        .stream()
                        .map(this::buildAcademicYearTermResponse)
                        .collect(Collectors.toList())
                ).build();
    }

    public AcademicYearTermResponse buildAcademicYearTermResponse(AcademicYearTerm academicYearTerm) {
        return AcademicYearTermResponse.builder()
                .termId(academicYearTerm.getId().getTermId())
                .yearId(academicYearTerm.getId().getYearId())
                .term(academicYearTerm.getTerm().getName())
                .year(academicYearTerm.getYear().getYear())
                .startDate(academicYearTerm.getStartDate())
                .endDate(academicYearTerm.getEndDate())
                .isCurrent(academicYearTerm.getIsCurrent())
                .build();
    }

}

