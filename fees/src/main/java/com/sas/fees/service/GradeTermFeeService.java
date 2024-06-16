package com.sas.fees.service;

import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.fees.entity.GradeTermFee;
import com.sas.fees.entity.GradeTermFeeId;
import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import com.sas.fees.repository.GradeTermFeeRepository;
import com.sas.fees.repository.TermFeeBalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GradeTermFeeService {
    private final GradeTermFeeRepository gradeTermFeeRepository;
    public Optional<GradeTermFee> getGradeTermFeeById(int gradeId, int termId, long yearId) {
        return gradeTermFeeRepository.findById(new GradeTermFeeId(gradeId, yearId, termId));
    }

    public void saveGradeTermFees(AcademicYearRequest academicYearRequest) {
        Long yearId = academicYearRequest.getYearId();

        List<GradeTermFee> gradeTermFees = academicYearRequest.getTerms().stream()
                .flatMap(termRequest -> termRequest.getGradeTermFees().stream()
                        .map(gradeTermFeeRequest -> GradeTermFee.builder()
                                .id(GradeTermFeeId.builder()
                                        .gradeId(gradeTermFeeRequest.getGradeId())
                                        .yearId(yearId)
                                        .termId(termRequest.getTermId())
                                        .build())
                                .fee(gradeTermFeeRequest.getFees())
                                .build()))
                .collect(Collectors.toList());

        gradeTermFeeRepository.saveAll(gradeTermFees);
    }
}
