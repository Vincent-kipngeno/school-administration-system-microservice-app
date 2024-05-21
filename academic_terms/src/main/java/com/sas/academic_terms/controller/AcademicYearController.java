package com.sas.academic_terms.controller;

import com.sas.academic_terms.dto.AcademicYearRequest;
import com.sas.academic_terms.entity.A_YearTerm;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.service.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-years")
public class AcademicYearController {

    @Autowired
    private AcademicYearService academicYearService;

    @PostMapping
    public ResponseEntity<String> addAcademicYear(@RequestBody AcademicYearRequest request) {
        // Call the service method to add the academic year and its terms
        Long academicYearId = academicYearService.addAcademicYear(request);
        return ResponseEntity.ok("Academic year added with ID: " + academicYearId);
    }

    @GetMapping("/currentA_YearTerm")
    public ResponseEntity<A_YearTerm> getCurrentA_YearTerm() {
        A_YearTerm currentA_YearTerm = academicYearService.getCurrentA_YearTerm();
        return ResponseEntity.ok(currentA_YearTerm);
    }

    @GetMapping("/academicYears")
    public ResponseEntity<List<AcademicYear>> getAllAcademicYears() {
        List<AcademicYear> academicYears = academicYearService.getAllAcademicYears();
        return ResponseEntity.ok(academicYears);
    }

    @GetMapping("/a_YearTerms")
    public ResponseEntity<List<A_YearTerm>> getAllA_YearTerms() {
        List<A_YearTerm> a_YearTerms = academicYearService.getAllA_YearTerms();
        return ResponseEntity.ok(a_YearTerms);
    }

    @GetMapping("/a_YearTerm/{yearId}/{termId}")
    public ResponseEntity<A_YearTerm> getA_YearTermById(@PathVariable Integer yearId, @PathVariable Integer termId) {
        A_YearTerm a_YearTerm = academicYearService.getA_YearTermById(yearId, termId);
        return ResponseEntity.ok(a_YearTerm);
    }

    @GetMapping("/academicYear/{id}")
    public ResponseEntity<AcademicYear> getAcademicYearById(@PathVariable Integer id) {
        AcademicYear academicYear = academicYearService.getAcademicYearById(id);
        return ResponseEntity.ok(academicYear);
    }

    @GetMapping("/academicTerm/{id}")
    public ResponseEntity<AcademicTerm> getAcademicTermById(@PathVariable Integer id) {
        AcademicTerm academicTerm = academicYearService.getAcademicTermById(id);
        return ResponseEntity.ok(academicTerm);
    }
}

