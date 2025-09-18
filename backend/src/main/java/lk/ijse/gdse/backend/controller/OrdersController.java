package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.OrderDto;
import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class OrdersController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse>place(@RequestBody OrderDto ordersDto) {
        OrderDto place  = orderService.createOrder(ordersDto);
        return ResponseEntity.ok(
                new ApiResponse(201, "Order Saved Successfully", place)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderDto> order = orderService.getAllOrders();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", order)
        );
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<OrderDto> orders = orderService.getOrderByPage(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "OK", orders));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = orderService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

    @PatchMapping("status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable("id") Long id){
        orderService.changeStatus(id);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "order change Status update",
                        null
                )
        );
    }
}
