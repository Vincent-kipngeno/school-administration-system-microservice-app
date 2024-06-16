package com.sas.fees.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeePaymentReportDTO {
    private String studId;
    private Integer gradeId;
    private Long yearId;
    private Integer termId;
    private double amount;
    private double fee;
    private double cumulativeTotalAmount;
    private double cumulativeTotalFee;
    private double cumulativeBalance;
}
