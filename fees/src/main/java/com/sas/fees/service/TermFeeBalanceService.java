package com.sas.fees.service;

import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.clients.users.UserClient;
import com.sas.clients.users.UserResponse;
import com.sas.exception.dto.GradeTermFeeNotFoundException;
import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import com.sas.fees.repository.TermFeeBalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TermFeeBalanceService {

    private final TermFeeBalanceRepository termFeeBalanceRepository;
    private final GradeTermFeeService gradeTermFeeService;
    private final UserClient userClient;

    public Optional<TermFeeBalance> getTermFeeBalanceById(String studId, int termId, long yearId) {
        Optional<TermFeeBalance> termFeeBalanceOpt = termFeeBalanceRepository.findById(new TermFeeBalanceId(studId, yearId, termId));
        if (termFeeBalanceOpt.isPresent()) {
            TermFeeBalance termFeeBalance = termFeeBalanceOpt.get();
            double totalFeePaid = termFeeBalanceRepository.findStudentTotalFeePaidByTermAndYear(studId, termId, yearId);
            termFeeBalance.setTotalFeePaid(totalFeePaid);
            return Optional.of(termFeeBalance);
        }
        return Optional.empty();
    }

    public void createCurrentTermFeeStudentFeeRecords(List<AcademicYearTermResponse> academicYearTerms) {
        List<UserResponse> students = Objects.requireNonNull(userClient.getStudents().getBody()).getBody();

        int prevTermId = academicYearTerms.get(0).getTermId();
        long prevYearId = academicYearTerms.get(0).getYearId();
        int curTermId = academicYearTerms.get(1).getTermId();
        long curYearId = academicYearTerms.get(1).getYearId();

        for (UserResponse student : students) {
            String studId = student.getId();

            Optional<TermFeeBalance> termFeeBalanceOpt = getTermFeeBalanceById(studId, prevTermId, prevYearId);

            TermFeeBalance termFeeBalance = termFeeBalanceOpt.orElse(TermFeeBalance.builder().arrears(0).totalFeePaid(0).build());
            double prevTermFee = gradeTermFeeService.getGradeTermFeeById(student.getGradeId(), prevTermId, prevYearId)
                    .orElseThrow(
                            () -> new GradeTermFeeNotFoundException(
                                    String.format(
                                            "The fee for the year: %s and term: %s is missing",
                                            academicYearTerms.get(0).getYear(),
                                            academicYearTerms.get(0).getTerm())
                            )).getFee();
            double newArrears = (termFeeBalance.getArrears() + prevTermFee) - termFeeBalance.getTotalFeePaid();

            TermFeeBalance newTermFeeBalance = TermFeeBalance.builder()
                    .id(new TermFeeBalanceId(studId, curYearId, curTermId))
                    .arrears(newArrears)
                    .build();

            termFeeBalanceRepository.save(newTermFeeBalance);
        }
    }
}
