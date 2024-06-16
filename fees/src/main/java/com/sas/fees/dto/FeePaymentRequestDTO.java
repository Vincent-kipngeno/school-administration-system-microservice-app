package com.sas.fees.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeePaymentRequestDTO {

    private String studId;
    private Integer gradeId;
    private Long yearId;
    private Integer termId;
    private String paidBy;
    private String paymentMode;
    private Date paymentDate;
    private String paymentCode;
    private double amount;

}
