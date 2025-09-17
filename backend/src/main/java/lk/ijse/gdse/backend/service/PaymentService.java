package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.PaymentDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
}
