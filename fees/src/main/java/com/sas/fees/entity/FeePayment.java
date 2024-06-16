package com.sas.fees.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "fee_payment")
public class FeePayment {
    @Id
    @SequenceGenerator(
            name = "fee_payment_id_sequence",
            sequenceName = "fee_payment_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fee_payment_id_sequence"
    )
    private Long id;

    @Column(name = "stud_id")
    private String studId;

    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "year_id")
    private Long yearId;

    @Column(name = "term_id")
    private Integer termId;

    @Column(name = "paid_by")
    private String paidBy;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "payment_code")
    private String paymentCode;

    private double amount;

}
