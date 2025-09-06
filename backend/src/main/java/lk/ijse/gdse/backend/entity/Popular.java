package lk.ijse.gdse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "popular")
public class Popular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long popularId;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
}
