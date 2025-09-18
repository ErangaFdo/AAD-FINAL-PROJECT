package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.DeliveryDto;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);
}
