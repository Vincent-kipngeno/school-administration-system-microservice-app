package com.sas.academic_terms.service;

import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.repository.AcademicTermRepository;
import com.sas.clients.academic_terms.AcademicTermResponse;
import com.sas.clients.academic_terms.AcademicYearResponse;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcademicTermService {
    private final AcademicTermRepository academicTermRepository;

    public AcademicTermResponse getAcademicTermById(Integer id) {
        return buildAcademicTermResponse(
                academicTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic Term not found"))
        );
    }

    public AcademicTermResponse buildAcademicTermResponse(AcademicTerm academicTerm) {
        return AcademicTermResponse.builder()
                .id(academicTerm.getId())
                .name(academicTerm.getName())
                .terms(
                        academicTerm.getTerms()
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
