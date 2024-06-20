package com.sas.fees.controller;

import com.sas.clients.ResponseDTO;
import com.sas.clients.users.UserResponse;
import com.sas.fees.dto.FeePaymentReportDTO;
import com.sas.fees.dto.FeePaymentRequestDTO;
import com.sas.fees.entity.FeePayment;
import com.sas.fees.service.FeePaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/fees")
@AllArgsConstructor
public class FeesController {

    private final FeePaymentService feePaymentService;

    @PostMapping("/payments")
    public ResponseEntity<ResponseDTO<FeePayment>> createFeePayment(@RequestBody FeePaymentRequestDTO feePaymentRequestDTO) {
        FeePayment feePayment = feePaymentService.createFeePayment(feePaymentRequestDTO);
        return new ResponseEntity<>
                (
                        ResponseDTO.<FeePayment>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .status(HttpStatus.CREATED.getReasonPhrase())
                                .message("Fee Paid.")
                                .timestamp(System.currentTimeMillis())
                                .body(feePayment)
                                .build(),
                        HttpStatus.CREATED
                );
    }

    @GetMapping("/reports")
    public ResponseEntity<ResponseDTO<List<FeePaymentReportDTO>>> getStudentIds() {
        List<FeePaymentReportDTO> feeReports = feePaymentService.getFeePaymentReport();
        ResponseDTO<List<FeePaymentReportDTO>> response = ResponseDTO.<List<FeePaymentReportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Fee payments report.")
                .timestamp(System.currentTimeMillis())
                .body(feeReports)
                .build();
        return ResponseEntity.ok(response);
    }

}

