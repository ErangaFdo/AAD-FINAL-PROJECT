package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.OrderDto;
import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.entity.Order;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.OrderRepository;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(OrderDto ordersDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        double total = ordersDto.getPrice() * ordersDto.getOrderQty();
        ordersDto.setTotal(total);

        // DTO → Entity
        Order order = modelMapper.map(ordersDto, Order.class);
        order.setUser(user);


        order.setTotal(total);// ensure entity also gets total
        order.setStatus("pending");

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Entity → DTO
       OrderDto savedDto = modelMapper.map(savedOrder, OrderDto.class);
        savedDto.setUserId(user.getUserId());

        return savedDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(modelMapper.map(order, OrderDto.class));
        }
        return orderDtos;
    }

}

