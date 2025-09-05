package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.ProductDto;
import lk.ijse.gdse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
      private final ProductService productService;
      private final String uploadDir = "uploads/";

    @PostMapping(value = "/addItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProductItem(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            ProductDto productDto = new ProductDto();
            productDto.setName(name);
            productDto.setCategory(category);
            productDto.setDescription(description);
            productDto.setPrice(price);
            productDto.setImageUrl(fileName);

            productService.saveProduct(productDto);

            return ResponseEntity.status(201).body("Product item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<ProductDto> menus = productService.getAllProduct();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", menus)
        );
    }

    @PutMapping(value = "/updateItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upsertProductItem(
            @RequestParam(value = "id", required = false) Long productId,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            ProductDto productDto = new ProductDto();

            if (productId != null) {
                productDto.setProductId(productId);
            }

            productDto.setName(name);
            productDto.setCategory(category);
            productDto.setDescription(description);
            productDto.setPrice(price);

            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                productDto.setImageUrl(fileName);
            }

            productService.updateProduct(productDto);

            return ResponseEntity.status(200).body("Menu item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Operation failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse(200, "Menu deleted successfully", null));
    }
}
