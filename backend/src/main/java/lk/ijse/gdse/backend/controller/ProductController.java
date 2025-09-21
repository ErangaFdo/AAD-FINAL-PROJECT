package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.ProductDto;
import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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

    // ✅ Use absolute path
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    private final ProductService productService;

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
            productDto.setImageUrl(fileName); // ✅ Only filename

            productService.saveProduct(productDto);
            return ResponseEntity.status(201).body("Product item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
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
                productDto.setImageUrl(fileName); // ✅ Only filename
            }

            productService.updateProduct(productDto);
            return ResponseEntity.status(200).body("Menu item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Operation failed: " + e.getMessage());
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<ProductDto> menus = productService.getAllProduct();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", menus)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse(200, "Menu deleted successfully", null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<ProductDto> products = productService.getProductByPage(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "OK", products));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = productService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

    @GetMapping("search/{keyword}")
    public ResponseEntity<ApiResponse> searchProducts(@PathVariable("keyword") String keyword) {
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Product found Successfully",
                        productDtos
                )
        );
    }




}
