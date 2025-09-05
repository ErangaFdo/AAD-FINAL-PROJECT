package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void saveProduct(ProductDto productDto);
    List<ProductDto> getAllProduct();
    void updateProduct(ProductDto productDto);
    void deleteProduct(Long id);
}
