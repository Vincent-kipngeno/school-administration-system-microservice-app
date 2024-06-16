package com.sas.fees.repository;

import com.sas.fees.dto.FeePaymentReportDTO;
import com.sas.fees.entity.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {

    @Query("SELECT new FeePaymentReportDTO(" +
            "f.studId, f.gradeId, f.yearId, f.termId, f.amount, g.fee, " +
            "SUM(f.amount) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId), " +
            "SUM(g.fee) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId), " +
            "SUM(g.fee) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId) - " +
            "SUM(f.amount) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId)) " +
            "FROM FeePayment f " +
            "JOIN GradeTermFee g ON f.gradeId = g.id.gradeId AND f.yearId = g.id.yearId AND f.termId = g.id.termId " +
            "ORDER BY f.studId, f.gradeId, f.yearId, f.termId")
    List<FeePaymentReportDTO> getFeePaymentReport();
}

/*
WITH CumulativeTotals AS (
    SELECT
        f.studId,
        f.gradeId,
        f.yearId,
        f.termId,
        f.amount,
        g.fee,
        SUM(f.amount) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId) AS cumulativeTotalAmount,
        SUM(g.fee) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId) AS cumulativeTotalFee,
        SUM(g.fee) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId) -
        SUM(f.amount) OVER (PARTITION BY f.studId ORDER BY f.yearId, f.termId) AS cumulativeBalance,
        SUM(f.amount) OVER (ORDER BY f.yearId, f.termId ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS overallCumulativeTotalAmount,
        SUM(g.fee) OVER (ORDER BY f.yearId, f.termId ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS overallCumulativeTotalFee,
        SUM(g.fee) OVER (ORDER BY f.yearId, f.termId ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) -
        SUM(f.amount) OVER (ORDER BY f.yearId, f.termId ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS overallCumulativeBalance
    FROM
        FeePayment f
    JOIN
        GradeTermFee g ON f.gradeId = g.gradeId AND f.yearId = g.yearId AND f.termId = g.termId
    ORDER BY
        f.studId, f.gradeId, f.yearId, f.termId
)
SELECT
    studId,
    gradeId,
    yearId,
    termId,
    amount,
    fee,
    cumulativeTotalAmount,
    cumulativeTotalFee,
    cumulativeBalance,
    overallCumulativeTotalAmount,
    overallCumulativeTotalFee,
    overallCumulativeBalance
FROM
    CumulativeTotals;

 */
