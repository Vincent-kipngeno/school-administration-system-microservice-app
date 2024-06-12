package com.sas.academic_terms.controller;

import com.sas.academic_terms.dto.AcademicYearRequest;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.service.AcademicYearTermService;
import com.sas.academic_terms.service.AcademicTermService;
import com.sas.academic_terms.service.AcademicYearService;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/academic-years")
public class AcademicYearController {

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private AcademicYearTermService academicYearTermService;

    @Autowired
    private AcademicTermService academicTermService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> addAcademicYear(@RequestBody AcademicYearRequest request) {
        Long academicYearId = academicYearService.addAcademicYear(request);
        return ResponseEntity.ok(
                responseDTO(null, "Academic year added with ID: " + academicYearId)
        );
    }

    @GetMapping("/currentA_YearTerm")
    public ResponseEntity<ResponseDTO<AcademicYearTermResponse>> getCurrentAcademicYearTerm() {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearTermService.getCurrentAcademicYearTerm(),
                        "Current academic year term returned successfully"
                )
        );
    }

    @GetMapping("/academicYears")
    public ResponseEntity<List<AcademicYear>> getAllAcademicYears() {
        List<AcademicYear> academicYears = academicYearService.getAllAcademicYears();
        return ResponseEntity.ok(academicYears);
    }

    @GetMapping("/a_YearTerms")
    public ResponseEntity<ResponseDTO<List<AcademicYearTermResponse>>> getAllAcademicYearTerms() {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearTermService.getAllAcademicYearTerms(),
                        "Academic year terms returned successfully"
                )
        );
    }

    @GetMapping("/a_YearTerm/{yearId}/{termId}")
    public ResponseEntity<ResponseDTO<AcademicYearTermResponse>> getAcademicYearTermById(@PathVariable Integer yearId, @PathVariable Integer termId) {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearTermService.getAcademicYearTermById(yearId, termId),
                        "Academic year term returned successfully."
                )
        );
    }

    @GetMapping("/academicYear/{id}")
    public ResponseEntity<AcademicYear> getAcademicYearById(@PathVariable Integer id) {
        AcademicYear academicYear = academicYearService.getAcademicYearById(id);
        return ResponseEntity.ok(academicYear);
    }

    @GetMapping("/academicTerm/{id}")
    public ResponseEntity<AcademicTerm> getAcademicTermById(@PathVariable Integer id) {
        AcademicTerm academicTerm = academicTermService.getAcademicTermById(id);
        return ResponseEntity.ok(academicTerm);
    }

    public <T> ResponseDTO<T> responseDTO(T response, String message) {
        return ResponseDTO.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .body(response)
                .build();
    }
}

