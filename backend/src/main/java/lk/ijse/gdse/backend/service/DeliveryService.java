package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.DeliveryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto deliveryDto);
    List<DeliveryDto> getAllDelivery();
    List<DeliveryDto> getDeliveryByPage(int page, int size);
    int getTotalPages(int size);
}
