package com.sas.academic_terms.service;

import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.entity.AcademicYearTermId;
import com.sas.academic_terms.repository.AcademicYearTermRepository;
import com.sas.amqp.RabbitMQMessageProducer;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcademicYearTermService {

    private final AcademicYearTermRepository academicYearTermRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.routing-keys.internal-fees}")
    private String internalFeesRoutingKey;

    public AcademicYearTerm save(AcademicYearTerm aYearTerm) {
        return academicYearTermRepository.saveAndFlush(aYearTerm);
    }

    public AcademicYearTermResponse getCurrentAcademicYearTerm() {
        AcademicYearTerm currentTerm = academicYearTermRepository.findByIsCurrentTrue()
                .orElseThrow(() -> new RuntimeException("Current term not set"));

        // Find the next earliest academic term with start date greater than the end date of the current term
        AcademicYearTerm nextTerm = academicYearTermRepository.findFirstByStartDateGreaterThanOrderByStartDateAsc(currentTerm.getEndDate())
                .orElse(currentTerm);

        AcademicYearTermResponse currentAcademicYearTermResponse = buildAcademicYearTermResponse(currentTerm);

        // Check if the current term is over to start next term
        if (currentTerm.getEndDate().after(new Date()) || currentTerm.equals(nextTerm)) {
            return currentAcademicYearTermResponse;
        }

        nextTerm.setIsCurrent(true);
        academicYearTermRepository.save(nextTerm);
        currentTerm.setIsCurrent(false);
        academicYearTermRepository.save(currentTerm);

        AcademicYearTermResponse nextAcademicYearTermResponse = buildAcademicYearTermResponse(nextTerm);

        ResponseDTO<List<AcademicYearTermResponse>> response = ResponseDTO.<List<AcademicYearTermResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Current and Next academic year terms successfully fetched.")
                .timestamp(LocalDateTime.now())
                .body(Arrays.asList(currentAcademicYearTermResponse, nextAcademicYearTermResponse))
                .build();

        // TODO: Send message to fees service to update fee arrears for new term
        rabbitMQMessageProducer.publish(
                response,
                internalExchange,
                internalFeesRoutingKey
        );

        return nextAcademicYearTermResponse;
    }

    public List<AcademicYearTermResponse> getAllAcademicYearTerms() {
        List<AcademicYearTerm> terms = academicYearTermRepository.findAll();
        return terms.stream()
                .map(this::buildAcademicYearTermResponse)
                .collect(Collectors.toList());
    }

    public AcademicYearTermResponse getAcademicYearTermById(Long yearId, Integer termId) {
        AcademicYearTermId id = new AcademicYearTermId(yearId, termId);
        AcademicYearTerm term = academicYearTermRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("A_YearTerm not found"));
        return buildAcademicYearTermResponse(term);
    }

    public AcademicYearTermResponse buildAcademicYearTermResponse(AcademicYearTerm academicYearTerm) {
        return AcademicYearTermResponse.builder()
                .termId(academicYearTerm.getId().getTermId())
                .yearId(academicYearTerm.getId().getYearId())
                .term(academicYearTerm.getTerm().getName())
                .year(academicYearTerm.getYear().getYear())
                .startDate(academicYearTerm.getStartDate())
                .endDate(academicYearTerm.getEndDate())
                .isCurrent(academicYearTerm.getIsCurrent())
                .build();
    }
}
