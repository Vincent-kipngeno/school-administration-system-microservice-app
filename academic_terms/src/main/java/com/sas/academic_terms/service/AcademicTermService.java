package com.sas.academic_terms.service;

import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.repository.AcademicTermRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AcademicTermService {
    private final AcademicTermRepository academicTermRepository;

    public AcademicTerm getAcademicTermById(Integer id) {
        return academicTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic Term not found"));
    }
}
