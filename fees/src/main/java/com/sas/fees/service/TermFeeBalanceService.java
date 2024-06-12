package com.sas.fees.service;

import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.clients.users.UserClient;
import com.sas.clients.users.UserResponse;
import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import com.sas.fees.repository.TermFeeBalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TermFeeBalanceService {

    private final TermFeeBalanceRepository termFeeBalanceRepository;
    private final UserClient userClient;

    public Optional<TermFeeBalance> getTermFeeBalance(String studId, int termId, int yearId) {
        Optional<TermFeeBalance> termFeeBalanceOpt = termFeeBalanceRepository.findById(new TermFeeBalanceId(studId, termId, yearId));
        if (termFeeBalanceOpt.isPresent()) {
            TermFeeBalance termFeeBalance = termFeeBalanceOpt.get();
            double totalFeePaid = termFeeBalanceRepository.findStudentTotalFeePaidByTermAndYear(studId, termId, yearId);
            termFeeBalance.setTotalFeePaid(totalFeePaid);
            return Optional.of(termFeeBalance);
        }
        return Optional.empty();
    }

    public void createCurrentTermFeeStudentFeeRecords(List<AcademicYearTermResponse> academicYearTerms) {
        try {
            List<UserResponse> students = Objects.requireNonNull(userClient.getStudents().getBody()).getBody();

            int prevTermId = academicYearTerms.get(0).getTermId();
            int prevYearId = academicYearTerms.get(0).getYearId();
            int curTermId = academicYearTerms.get(1).getTermId();
            int curYearId = academicYearTerms.get(1).getYearId();

            for (UserResponse student : students) {
                String studId = student.getId();

                Optional<TermFeeBalance> termFeeBalanceOpt = getTermFeeBalance(studId, prevTermId, prevYearId);

                TermFeeBalance termFeeBalance = termFeeBalanceOpt.orElse(TermFeeBalance.builder().arrears(0).totalFeePaid(0).build());
                double newArrears = termFeeBalance.getArrears() - termFeeBalance.getTotalFeePaid();

                TermFeeBalance newTermFeeBalance = TermFeeBalance.builder()
                        .id(new TermFeeBalanceId(studId, curTermId, curYearId))
                        .arrears(newArrears)
                        .build();

                termFeeBalanceRepository.save(newTermFeeBalance);
            }
        } catch (Exception exception) {

        }
    }
}
