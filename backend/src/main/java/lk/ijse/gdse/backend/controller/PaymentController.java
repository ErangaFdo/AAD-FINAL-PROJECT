package lk.ijse.gdse.backend.controller;


import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.PaymentDto;
import lk.ijse.gdse.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/payment")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> save(@RequestBody PaymentDto paymentDto) {
        PaymentDto savePayment  = paymentService.createPayment(paymentDto);
        return ResponseEntity.ok(
                new ApiResponse(201, "Payment Saved Successfully", savePayment)
        );
    }

//    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ApiResponse> getAllOrders() {
//        List<PaymentDto> paymentDtos = paymentService.getAllPayment();
//        return ResponseEntity.ok(
//                new ApiResponse(200, "OK", paymentDtos)
//        );
//    }
//    @GetMapping("/paginated")
//    public ResponseEntity<ApiResponse> getPaginated(
//            @RequestParam int page,
//            @RequestParam int size
//    ) {
//        List<PaymentDto> paymentDtos = paymentService.getPaymentByPage(page, size);
//        return ResponseEntity.ok(new ApiResponse(200, "OK", paymentDtos));
//    }
//
//    @GetMapping("/total-pages")
//    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
//        int totalPages = paymentService.getTotalPages(size);
//        return ResponseEntity.ok(totalPages);
//    }
}
