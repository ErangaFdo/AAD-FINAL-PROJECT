package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.DeliveryDto;
import lk.ijse.gdse.backend.entity.Delivery;
import lk.ijse.gdse.backend.entity.Order;
import lk.ijse.gdse.backend.repository.DeliveryRepository;
import lk.ijse.gdse.backend.repository.OrderRepository;
import lk.ijse.gdse.backend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            message.setTo("erangafdo1994@gmail.com");
            message.setSubject("New Delivery Assigned");
            message.setText("Delivery assigned to: " + deliveryDto.getFullName() +
                    "\nPhone: " + deliveryDto.getPhoneNumber() +
                    "\nAddress: " + deliveryDto.getAddress() +
                    "\nTrack Location: " + mapsLink);

            mailSender.send(message);

            return modelMapper.map(saved, DeliveryDto.class);
    }

    @Override
    public List<DeliveryDto> getAllDelivery() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<DeliveryDto> deliveryDtos = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            deliveryDtos.add(modelMapper.map(delivery, DeliveryDto.class));
        }
        return deliveryDtos;    }

    @Override
    public List<DeliveryDto> getDeliveryByPage(int page, int size) {
        int offset = page * size;
        List<Delivery> deliveries = deliveryRepository.findDeliveryPaginated(size, offset);
        return modelMapper.map(deliveries, new TypeToken<List<DeliveryDto>>() {}.getType());    }

    @Override
    public int getTotalPages(int size) {
        long totalDeliveries = deliveryRepository.getTotalDeliveryCount();
        return (int) Math.ceil((double) totalDeliveries / size);      }
}
