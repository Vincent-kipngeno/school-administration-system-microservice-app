package com.sas.academic_terms.repository;

import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.entity.AcademicYearTermId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AcademicYearTermRepository extends JpaRepository<AcademicYearTerm, AcademicYearTermId> {

    Optional<AcademicYearTerm> findByIsCurrentTrue();

    Optional<AcademicYearTerm> findFirstByStartDateGreaterThanOrderByStartDateAsc(Date startDate);


}
