package com.sas.academic_terms.service;

import com.sas.academic_terms.config.AcademicYearsConfiguration;
import com.sas.academic_terms.entity.AcademicYearTermId;
import com.sas.academic_terms.repository.AcademicYearTermRepository;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearTermRequest;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.repository.AcademicTermRepository;
import com.sas.academic_terms.repository.AcademicYearRepository;
import com.sas.amqp.RabbitMQMessageProducer;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearResponse;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final AcademicTermRepository academicTermRepository;
    private final AcademicYearTermRepository academicYearTermRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final AcademicYearsConfiguration academicYearsConfiguration;

    public Long addAcademicYear(AcademicYearRequest request) {
        AcademicYear requestYear = AcademicYear.builder()
                .year(getAcademicYear(request.getTerms()))
                .startDate(request.getTerms().get(0).getStartDate())
                .endDate(request.getTerms().get(request.getTerms().size() - 1).getEndDate())
                .build();

        // Create AcademicYear entity
        AcademicYear academicYear = academicYearRepository.saveAndFlush(requestYear);

        // Map the terms from the request and set them in the AcademicYear
        List<AcademicYearTerm> academicYearTerms = request.getTerms().stream()
                .map(termRequest -> AcademicYearTerm.builder()
                            .id(new AcademicYearTermId(academicYear.getId(), termRequest.getTermId()))
                            .year(academicYear)
                            .term(academicTermRepository.findById(termRequest.getTermId())
                                    .orElseThrow(() -> new RuntimeException("Academic Term not found")))
                            .startDate(termRequest.getStartDate())
                            .endDate(termRequest.getEndDate())
                            .build())
                .collect(Collectors.toList());

        // Save the entire object graph in one go
        academicYearTermRepository.saveAll(academicYearTerms);

        request.setYearId(academicYear.getId());

        // Publish message to RabbitMQ
        rabbitMQMessageProducer.publish(
                ResponseDTO.<AcademicYearRequest>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK.getReasonPhrase())
                        .message("Add grade term fees.")
                        .timestamp(System.currentTimeMillis())
                        .body(request)
                        .build(),
                academicYearsConfiguration.getInternalExchange(),
                academicYearsConfiguration.getInternalGradeTermFeesRoutingKey()
        );

        return academicYear.getId();
    }

    private String getAcademicYear(List<AcademicYearTermRequest> terms) {
        // Assuming the terms are ordered by termId (1, 2, 3)
        Date startDateTerm1 = terms.get(0).getStartDate();
        Date endDateTerm3 = terms.get(2).getEndDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTerm1);
        int startYear = cal.get(Calendar.YEAR);

        cal.setTime(endDateTerm3);
        int endYear = cal.get(Calendar.YEAR);

        if (startYear == endYear) {
            return String.valueOf(startYear);
        } else {
            return startYear + "/" + endYear;
        }
    }

    public List<AcademicYearResponse> getAllAcademicYears() {
        return academicYearRepository.findAll()
                .stream()
                .map(this::buildAcademicYearResponse)
                .collect(Collectors.toList());
    }

    public AcademicYearResponse getAcademicYearById(Long id) {
        return buildAcademicYearResponse(
                academicYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicYear not found"))
        );
    }

    public AcademicYearResponse buildAcademicYearResponse(AcademicYear academicYear) {
        return AcademicYearResponse.builder()
                .id(academicYear.getId())
                .startDate(academicYear.getStartDate())
                .endDate(academicYear.getEndDate())
                .year(academicYear.getYear())
                .yearTerms(
                        academicYear.getYearTerms()
                        .stream()
                        .map(this::buildAcademicYearTermResponse)
                        .collect(Collectors.toList())
                ).build();
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

