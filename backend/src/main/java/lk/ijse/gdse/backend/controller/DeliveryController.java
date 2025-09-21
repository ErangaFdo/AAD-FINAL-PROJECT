package lk.ijse.gdse.backend.controller;


import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.DeliveryDto;
import lk.ijse.gdse.backend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/delivery")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> save(@RequestBody DeliveryDto deliveryDto) {
        DeliveryDto saveDelivery  = deliveryService.createDelivery(deliveryDto);
        return ResponseEntity.ok(
                new ApiResponse(201, "Delivery Saved Successfully", saveDelivery)
        );
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllDelivery() {
        List<DeliveryDto> deliveryDtos = deliveryService.getAllDelivery();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", deliveryDtos)
        );
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<DeliveryDto> deliveryDtos = deliveryService.getDeliveryByPage(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "OK", deliveryDtos));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = deliveryService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

}
