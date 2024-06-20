package com.sas.fees.service;

import com.sas.fees.dto.FeePaymentReportDTO;
import com.sas.fees.dto.FeePaymentRequestDTO;
import com.sas.fees.entity.FeePayment;
import com.sas.fees.repository.FeePaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FeePaymentService {
    private final FeePaymentRepository feePaymentRepository;

    @Transactional
    public FeePayment createFeePayment(FeePaymentRequestDTO feePaymentRequestDTO) {
        FeePayment feePayment = mapDtoToEntity(feePaymentRequestDTO);
        return feePaymentRepository.save(feePayment);
    }

    public List<FeePaymentReportDTO> getFeePaymentReport(){
        return feePaymentRepository.getFeePaymentReport();
    }

    private FeePayment mapDtoToEntity(FeePaymentRequestDTO dto) {
        return FeePayment.builder()
                .studId(dto.getStudId())
                .gradeId(dto.getGradeId())
                .yearId(dto.getYearId())
                .termId(dto.getTermId())
                .paidBy(dto.getPaidBy())
                .paymentMode(dto.getPaymentMode())
                .paymentDate(dto.getPaymentDate())
                .paymentCode(dto.getPaymentCode())
                .amount(dto.getAmount())
                .build();
    }
}
