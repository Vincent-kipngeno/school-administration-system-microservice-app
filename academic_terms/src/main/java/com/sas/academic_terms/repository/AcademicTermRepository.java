package com.sas.academic_terms.repository;

import com.sas.academic_terms.entity.AcademicTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicTermRepository extends JpaRepository<AcademicTerm, Integer> {
}

