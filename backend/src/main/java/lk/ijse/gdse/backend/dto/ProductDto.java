package lk.ijse.gdse.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    private Long productId;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
}