package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderDto createOrder(OrderDto ordersDto);
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrderByPage(int page, int size);
    int getTotalPages(int size);
    void changeStatus(Long id);
}
