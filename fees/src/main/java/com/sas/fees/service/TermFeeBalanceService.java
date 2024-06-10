package com.sas.fees.service;

import com.sas.fees.entity.TermFeeBalance;
import com.sas.fees.entity.TermFeeBalanceId;
import com.sas.fees.repository.TermFeeBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TermFeeBalanceService {
    @Autowired
    private TermFeeBalanceRepository termFeeBalanceRepository;

    public Optional<TermFeeBalance> getTermFeeBalance(int studId, int ayTermId) {
        Optional<TermFeeBalance> termFeeBalanceOpt = termFeeBalanceRepository.findById(new TermFeeBalanceId(studId, ayTermId));
        if (termFeeBalanceOpt.isPresent()) {
            TermFeeBalance termFeeBalance = termFeeBalanceOpt.get();
            double totalFeePaid = termFeeBalanceRepository.findTotalFeePaid(studId, ayTermId);
            termFeeBalance.setTotalFeePaid(totalFeePaid);
            return Optional.of(termFeeBalance);
        }
        return Optional.empty();
    }
}
