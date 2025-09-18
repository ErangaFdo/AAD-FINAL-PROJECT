package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayment();
}
