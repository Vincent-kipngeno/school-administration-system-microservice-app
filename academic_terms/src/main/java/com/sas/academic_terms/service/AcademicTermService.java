package com.sas.academic_terms.service;

import com.sas.academic_terms.entity.A_YearTerm;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.repository.AcademicTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcademicTermService {
    @Autowired
    private AcademicTermRepository academicTermRepository;

    public AcademicTerm getAcademicTermById(Integer id) {
        return academicTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic Term not found"));
    }
}
