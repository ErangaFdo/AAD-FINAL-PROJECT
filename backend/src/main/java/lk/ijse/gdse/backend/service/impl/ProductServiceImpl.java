package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.ProductDto;
import lk.ijse.gdse.backend.entity.Product;
import lk.ijse.gdse.backend.repository.ProductRepository;
import lk.ijse.gdse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveProduct(ProductDto productDto) {
        Product product;

        if (productDto.getProductId() != null) {
            product = productRepository.findById(productDto.getProductId())
                    .orElse(new Product());
        } else {
            product = new Product();
        }

        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());

        productRepository.save(product);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product>products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(modelMapper.map(product, ProductDto.class));
        }
        return productDtos;
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        if (productDto.getProductId() == null) {
            saveProduct(productDto);
            return;
        }

        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Menu not found with ID: " + productDto.getProductId()));

        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());

        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
        productRepository.delete(product);
    }

}
