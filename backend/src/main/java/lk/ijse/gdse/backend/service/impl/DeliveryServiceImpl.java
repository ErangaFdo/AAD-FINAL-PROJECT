package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.DeliveryDto;
import lk.ijse.gdse.backend.entity.Delivery;
import lk.ijse.gdse.backend.entity.Order;
import lk.ijse.gdse.backend.repository.DeliveryRepository;
import lk.ijse.gdse.backend.repository.OrderRepository;
import lk.ijse.gdse.backend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final JavaMailSender mailSender;


        @Override
        public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Order order = (Order) orderRepository.findTopByUser_UserNameOrderByOrderDatetimeDesc(username)
                    .orElseThrow(() -> new RuntimeException("No active order found for user: " + username));

            Delivery delivery = modelMapper.map(deliveryDto, Delivery.class);
            delivery.setOrder(order);

            Delivery saved = deliveryRepository.save(delivery);

            String mapsLink = "https://www.google.com/maps?q=" + deliveryDto.getLatitude() + "," + deliveryDto.getLongitude();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("erangafdo1994@gmail.com");
            message.setTo("chaminduchirantha10@gmail.com");
            message.setSubject("New Delivery Assigned");
            message.setText("Delivery assigned to: " + deliveryDto.getFullName() +
                    "\nPhone: " + deliveryDto.getPhoneNumber() +
                    "\nAddress: " + deliveryDto.getAddress() +
                    "\nTrack Location: " + mapsLink);

            mailSender.send(message);

            return modelMapper.map(saved, DeliveryDto.class);

    }
}
