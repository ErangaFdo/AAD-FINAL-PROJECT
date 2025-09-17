package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;


    private String name;
    private double price;
    private String email;
    private String orderType;
    private int orderQty;
    private Date orderDatetime;
    private String status;
    private double total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}