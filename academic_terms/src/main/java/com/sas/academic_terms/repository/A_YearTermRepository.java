package com.sas.academic_terms.repository;

import com.sas.academic_terms.entity.A_YearTerm;
import com.sas.academic_terms.entity.A_YearTermId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface A_YearTermRepository extends JpaRepository<A_YearTerm, A_YearTermId> {
    @Override
    Optional<A_YearTerm> findById(A_YearTermId aYearTermId);

    Optional<A_YearTerm> findByIsCurrentTrue();

    Optional<A_YearTerm> findFirstByStartDateGreaterThanOrderByStartDateAsc(Date startDate);


}
