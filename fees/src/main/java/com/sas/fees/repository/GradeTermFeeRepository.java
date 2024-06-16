package com.sas.fees.repository;

import com.sas.fees.entity.GradeTermFee;
import com.sas.fees.entity.GradeTermFeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeTermFeeRepository extends JpaRepository<GradeTermFee, GradeTermFeeId> {
}
