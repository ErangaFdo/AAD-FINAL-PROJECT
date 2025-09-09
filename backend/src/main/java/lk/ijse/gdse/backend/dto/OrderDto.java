package lk.ijse.gdse.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String name;
    private double price;
    private String email;
    private String orderType;
    private int orderQty;
    private Date orderDatetime;
    private String status;
    private String notes;
    private Long userId;
}
