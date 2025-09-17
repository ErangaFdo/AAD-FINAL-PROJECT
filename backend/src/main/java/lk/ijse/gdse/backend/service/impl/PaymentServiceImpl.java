package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.PaymentDto;
import lk.ijse.gdse.backend.entity.Order;
import lk.ijse.gdse.backend.entity.Payment;
import lk.ijse.gdse.backend.repository.OrderRepository;
import lk.ijse.gdse.backend.repository.PaymentRepository;
import lk.ijse.gdse.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        return null;
    }

//    @Override
//    public PaymentDto createPayment(PaymentDto paymentDto) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String username = authentication.getName();
////
////        Order order = (Order) orderRepository.findTopByCustomer_UsernameOrderByOrderDatetimeDesc(username)
////                .orElseThrow(() -> new RuntimeException("No active order found for user: " + username));
////
////        Payment payment = modelMapper.map(paymentDto, Payment.class);
////        payment.setOrder(order);
////
////        Payment saved = paymentRepository.save(payment);
////
////        return modelMapper.map(saved, PaymentDto.class);
//    }

}
