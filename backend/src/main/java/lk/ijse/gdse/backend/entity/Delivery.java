package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String date;
    private String time;

    private Double latitude;
    private Double longitude;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
