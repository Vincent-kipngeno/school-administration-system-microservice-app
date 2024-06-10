package com.sas.fees.repository;

import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TermFeeBalanceRepository extends JpaRepository<TermFeeBalance, TermFeeBalanceId> {
    @Query("SELECT SUM(f.amount) FROM Fee f WHERE f.studId = :studId AND f.ayTermId = :ayTermId")
    double findTotalFeePaid(@Param("studId") int studId, @Param("ayTermId") int ayTermId);
}
