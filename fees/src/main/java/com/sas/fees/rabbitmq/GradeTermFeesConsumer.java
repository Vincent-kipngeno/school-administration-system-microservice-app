package com.sas.fees.rabbitmq;

import com.sas.clients.ResponseDTO;
import com.sas.clients.academic_terms.AcademicYearRequest;
import com.sas.clients.academic_terms.AcademicYearTermResponse;
import com.sas.fees.service.GradeTermFeeService;
import com.sas.fees.service.TermFeeBalanceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GradeTermFeesConsumer {
    private final GradeTermFeeService gradeTermFeeService;

    @RabbitListener(queues = "${rabbitmq.queues.grade-term-fees}")
    public void consumer(ResponseDTO<AcademicYearRequest> responseDTO) {
        log.info("Consumed {} from queue", responseDTO);
        gradeTermFeeService.saveGradeTermFees(responseDTO.getBody());
    }
}
