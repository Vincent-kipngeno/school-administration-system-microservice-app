package com.sas.academic_terms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sas.academic_terms.config.AcademicYearsConfiguration;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.academic_terms.repository.AcademicTermRepository;
import com.sas.academic_terms.repository.AcademicYearRepository;
import com.sas.academic_terms.repository.AcademicYearTermRepository;
import com.sas.amqp.RabbitMQMessageProducer;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AcademicYearServiceTest {

    @Mock
    private AcademicYearRepository academicYearRepository;
    @Mock
    private AcademicTermRepository academicTermRepository;
    @Mock
    private AcademicYearTermRepository academicYearTermRepository;
    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;
    @Mock
    private AcademicYearsConfiguration academicYearsConfiguration;

    @InjectMocks
    private AcademicYearService academicYearService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenAddAcademicYear_thenSaveAndPublish() throws JsonProcessingException {
        AcademicYearRequest request = createAcademicYearRequest();
        AcademicYear savedYear = AcademicYear.builder()
                .id(1L)
                .year("2024")
                .startDate(request.getTerms().get(0).getStartDate())
                .endDate(request.getTerms().get(2).getEndDate())
                .build();

        when(academicYearRepository.saveAndFlush(any(AcademicYear.class))).thenReturn(savedYear);
        when(academicTermRepository.findById(1)).thenReturn(Optional.of(new AcademicTerm(1, "Term 1", null)));
        when(academicTermRepository.findById(2)).thenReturn(Optional.of(new AcademicTerm(2, "Term 2", null)));
        when(academicTermRepository.findById(3)).thenReturn(Optional.of(new AcademicTerm(3, "Term 3", null)));
        when(academicYearsConfiguration.getInternalExchange()).thenReturn("exchange");
        when(academicYearsConfiguration.getInternalGradeTermFeesRoutingKey()).thenReturn("routingKey");

        Long savedId = academicYearService.addAcademicYear(request);

        ArgumentCaptor<AcademicYear> academicYearCaptor = ArgumentCaptor.forClass(AcademicYear.class);
        verify(academicYearRepository).saveAndFlush(academicYearCaptor.capture());

        AcademicYear capturedAcademicYear = academicYearCaptor.getValue();

        // Check the captured AcademicYear details before it was saved
        assertThat(capturedAcademicYear.getYear()).isEqualTo(savedYear.getYear());
        assertThat(capturedAcademicYear.getStartDate()).isEqualTo(savedYear.getStartDate());
        assertThat(capturedAcademicYear.getEndDate()).isEqualTo(savedYear.getEndDate());


        // Inside your test method
        ArgumentCaptor<List<AcademicYearTerm>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(academicYearTermRepository).saveAll(argumentCaptor.capture());
        List<AcademicYearTerm> capturedArguments = argumentCaptor.getValue();

        // Now you can make assertions on `capturedArguments` as needed
        assertThat(capturedArguments).hasSize(3);

        assertThat(savedId).isNotNull();
        assertThat(savedId).isEqualTo(savedYear.getId());
        verify(academicYearRepository).saveAndFlush(any(AcademicYear.class));
        verify(academicYearTermRepository).saveAll(anyList());
        verify(rabbitMQMessageProducer).publish(any(ResponseDTO.class), eq("exchange"), eq("routingKey"));
    }

    @Test
    void whenGetAllAcademicYears_thenReturnAll() {
        List<AcademicYear> years = Arrays.asList(createAcademicYear(), createAcademicYear());
        when(academicYearRepository.findAll()).thenReturn(years);

        List<AcademicYearResponse> responses = academicYearService.getAllAcademicYears();
        assertThat(responses).hasSize(years.size());
    }

    private AcademicYear createAcademicYear() {
        AcademicYear year = new AcademicYear();
        year.setId(1L); // example ID
        year.setStartDate(new Date()); // example start date
        year.setEndDate(new Date()); // example end date
        year.setYear("2024"); // example year
        year.setYearTerms(new ArrayList<>()); // Initialize yearTerms to prevent NPE
        return year;
    }

    @Test
    void whenGetAcademicYearById_thenSuccess() {
        AcademicYear year = createAcademicYear();
        when(academicYearRepository.findById(1L)).thenReturn(Optional.of(year));

        AcademicYearResponse response = academicYearService.getAcademicYearById(1L);
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void whenGetAcademicYearById_thenNotFound() {
        when(academicYearRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> academicYearService.getAcademicYearById(1L));
    }

    private AcademicYearRequest createAcademicYearRequest() throws JsonProcessingException {
        String jsonRequest = "{\n" +
                "  \"yearId\": 2024,\n" +
                "  \"terms\": [\n" +
                "    {\n" +
                "      \"termId\": 1,\n" +
                "      \"startDate\": \"2024-01-10\",\n" +
                "      \"endDate\": \"2024-04-15\",\n" +
                "      \"gradeTermFees\": [\n" +
                "        {\"gradeId\": 1, \"fees\": 1000.00},\n" +
                "        {\"gradeId\": 2, \"fees\": 1500.00},\n" +
                "        {\"gradeId\": 3, \"fees\": 1800.00},\n" +
                "        {\"gradeId\": 4, \"fees\": 2000.00},\n" +
                "        {\"gradeId\": 5, \"fees\": 2200.00}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"termId\": 2,\n" +
                "      \"startDate\": \"2024-05-01\",\n" +
                "      \"endDate\": \"2024-08-20\",\n" +
                "      \"gradeTermFees\": [\n" +
                "        {\"gradeId\": 1, \"fees\": 1000.00},\n" +
                "        {\"gradeId\": 2, \"fees\": 1500.00},\n" +
                "        {\"gradeId\": 3, \"fees\": 1800.00},\n" +
                "        {\"gradeId\": 4, \"fees\": 2000.00},\n" +
                "        {\"gradeId\": 5, \"fees\": 2200.00}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"termId\": 3,\n" +
                "      \"startDate\": \"2024-09-05\",\n" +
                "      \"endDate\": \"2024-12-20\",\n" +
                "      \"gradeTermFees\": [\n" +
                "        {\"gradeId\": 1, \"fees\": 1000.00},\n" +
                "        {\"gradeId\": 2, \"fees\": 1500.00},\n" +
                "        {\"gradeId\": 3, \"fees\": 1800.00},\n" +
                "        {\"gradeId\": 4, \"fees\": 2000.00},\n" +
                "        {\"gradeId\": 5, \"fees\": 2200.00}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return objectMapper.readValue(jsonRequest, AcademicYearRequest.class);
    }
}


