package com.sas.academic_terms.service;

import com.sas.academic_terms.config.AcademicYearsConfiguration;
import com.sas.academic_terms.entity.AcademicTerm;
import com.sas.academic_terms.entity.AcademicYear;
import com.sas.academic_terms.entity.AcademicYearTermId;
import com.sas.academic_terms.repository.AcademicYearTermRepository;
import com.sas.academic_terms.entity.AcademicYearTerm;
import com.sas.amqp.RabbitMQMessageProducer;
import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AcademicYearTermServiceTest {

    @Mock
    private AcademicYearTermRepository academicYearTermRepository;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Mock
    private AcademicYearsConfiguration academicYearsConfiguration;

    @InjectMocks
    private AcademicYearTermService academicYearTermService;

    private AcademicYearTerm term1, term2, term3;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Captor
    private ArgumentCaptor<ResponseDTO<List<AcademicYearTermResponse>>> responseCaptor;

    @BeforeEach
    void setup() throws Exception {
        AcademicYear year = AcademicYear.builder()
                .year("2024")
                .build();
        term1 = new AcademicYearTerm(new AcademicYearTermId(1L, 1),year, new AcademicTerm(1, "Term 1", null), sdf.parse("2024-01-10"), sdf.parse("2024-04-15"), true);
        term2 = new AcademicYearTerm(new AcademicYearTermId(1L, 2),year, new AcademicTerm(2, "Term 2", null), sdf.parse("2024-05-01"), sdf.parse("2024-08-20"), false);
        term3 = new AcademicYearTerm(new AcademicYearTermId(1L, 3),year, new AcademicTerm(3, "Term 3", null), sdf.parse("2024-09-05"), sdf.parse("2024-12-20"), false);
    }

    @Test
    void whenGetCurrentAcademicYearTerm_thenReturnCorrectResponse() {
        when(academicYearTermRepository.findByIsCurrentTrue()).thenReturn(Optional.of(term1));
        when(academicYearTermRepository.findFirstByStartDateGreaterThanOrderByStartDateAsc(term1.getEndDate()))
                .thenReturn(Optional.of(term2));

        when(academicYearsConfiguration.getInternalExchange()).thenReturn("expected_exchange");
        when(academicYearsConfiguration.getInternalFeesRoutingKey()).thenReturn("expected_routing_key");

        assertThat(term1.getIsCurrent()).isTrue();
        assertThat(term2.getIsCurrent()).isFalse();
        AcademicYearTermResponse response = academicYearTermService.getCurrentAcademicYearTerm();
        assertThat(response.getTermId()).isEqualTo(term2.getId().getTermId());
        assertThat(term1.getIsCurrent()).isFalse();
        assertThat(term2.getIsCurrent()).isTrue();

        // Setup an ArgumentCaptor
        // ArgumentCaptor<ResponseDTO<List<AcademicYearTermResponse>>> responseCaptor = ArgumentCaptor.forClass(ResponseDTO.class);

        // Capture the arguments
        verify(rabbitMQMessageProducer).publish(
                responseCaptor.capture(),
                eq("expected_exchange"),
                eq("expected_routing_key")
        );

        // Assert specific fields in the captured ResponseDTO
        ResponseDTO<List<AcademicYearTermResponse>> capturedResponse = responseCaptor.getValue();
        assertThat(capturedResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(capturedResponse.getStatus()).isEqualTo(HttpStatus.OK.getReasonPhrase());
        assertThat(capturedResponse.getMessage()).contains("successfully fetched");
    }

    @Test
    void whenGetAllAcademicYearTerms_thenReturnAllTerms() {
        when(academicYearTermRepository.findAll()).thenReturn(Arrays.asList(term1, term2, term3));
        List<AcademicYearTermResponse> responses = academicYearTermService.getAllAcademicYearTerms();
        assertThat(responses).hasSize(3);
    }

    @Test
    void whenGetAcademicYearTermById_thenReturnCorrectTerm() {
        when(academicYearTermRepository.findById(new AcademicYearTermId(1L, 1))).thenReturn(Optional.of(term1));
        AcademicYearTermResponse response = academicYearTermService.getAcademicYearTermById(1L, 1);
        assertThat(response.getYearId()).isEqualTo(1L);
        assertThat(response.getTermId()).isEqualTo(1);
    }

    @Test
    void whenSaveAcademicYearTerm_thenSuccess() {
        when(academicYearTermRepository.saveAndFlush(any(AcademicYearTerm.class))).thenReturn(term1);
        AcademicYearTerm savedTerm = academicYearTermService.save(term1);
        assertThat(savedTerm).isNotNull();
        verify(academicYearTermRepository).saveAndFlush(term1);
    }

    @Test
    void whenGetCurrentAcademicYearTerm_andNoCurrentTerm_thenExceptionThrown() {
        when(academicYearTermRepository.findByIsCurrentTrue()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            academicYearTermService.getCurrentAcademicYearTerm();
        });
    }

    @Test
    void whenGetAllAcademicYearTerms_andNoTermsExist_thenEmptyListReturned() {
        when(academicYearTermRepository.findAll()).thenReturn(Collections.emptyList());
        List<AcademicYearTermResponse> responses = academicYearTermService.getAllAcademicYearTerms();
        assertThat(responses).isEmpty();
    }

    @Test
    void whenGetAcademicYearTermById_andTermNotFound_thenExceptionThrown() {
        when(academicYearTermRepository.findById(new AcademicYearTermId(2024L, 1))).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            academicYearTermService.getAcademicYearTermById(2024L, 1);
        });
    }
}