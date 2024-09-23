package com.sas.academic_terms.controller;

import com.sas.academic_terms.service.AcademicTermService;
import com.sas.academic_terms.service.AcademicYearService;
import com.sas.academic_terms.service.AcademicYearTermService;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicTermResponse;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearResponse;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseDTO<Long>> addAcademicYear(@RequestBody AcademicYearRequest request) {
        Long academicYearId = academicYearService.addAcademicYear(request);
        return ResponseEntity.ok(
                responseDTO(
                        academicYearId,
                        "Academic year added with ID: " + academicYearId
                )
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
    public ResponseEntity<ResponseDTO<List<AcademicYearResponse>>> getAllAcademicYears() {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearService.getAllAcademicYears(),
                        "List of academic years."
                )
        );
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
    public ResponseEntity<ResponseDTO<AcademicYearTermResponse>> getAcademicYearTermById(
            @PathVariable("yearId") Integer yearId,
            @PathVariable("termId") Integer termId
    ) {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearTermService.getAcademicYearTermById(Long.valueOf(yearId), termId),
                        "Academic year term returned successfully."
                )
        );
    }

    @GetMapping("/academicYear/{id}")
    public ResponseEntity<ResponseDTO<AcademicYearResponse>> getAcademicYearById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(
                responseDTO(
                        academicYearService.getAcademicYearById(Long.valueOf(id)),
                        "Academic year terms returned successfully"
                )
        );
    }

    @GetMapping("/academicTerm/{id}")
    public ResponseEntity<ResponseDTO<AcademicTermResponse>> getAcademicTermById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(
                responseDTO(
                        academicTermService.getAcademicTermById(id),
                        "Academic term returned successfully"
                )
        );
    }

    public <T> ResponseDTO<T> responseDTO(T response, String message) {
        return ResponseDTO.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message(message)
                //.timestamp(System.currentTimeMillis())
                .body(response)
                .build();
    }
}

