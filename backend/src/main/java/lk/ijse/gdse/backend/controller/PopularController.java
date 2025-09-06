package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.PopularDto;
import lk.ijse.gdse.backend.dto.ProductDto;
import lk.ijse.gdse.backend.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/popular")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PopularController {
    private final PopularService popularService;
    private final String uploadDir = "uploads/";

    @PostMapping(value = "/addPopularItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addPopularItem(
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

            PopularDto popularDto = new PopularDto();
            popularDto.setName(name);
            popularDto.setCategory(category);
            popularDto.setDescription(description);
            popularDto.setPrice(price);
            popularDto.setImageUrl(fileName);

            popularService.savePopular(popularDto);

            return ResponseEntity.status(201).body("Popular item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllPopularIte() {
        List<PopularDto> popular = popularService.getAllPopularIte();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", popular)
        );
    }

    @PutMapping(value = "/updateItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upsertPopularItem(
            @RequestParam(value = "id", required = false) Long popularId,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            PopularDto popularDto = new PopularDto();

            if ( popularId!= null) {
                popularDto.setPopularId(popularId);
            }

            popularDto.setName(name);
            popularDto.setCategory(category);
            popularDto.setDescription(description);
            popularDto.setPrice(price);

            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                popularDto.setImageUrl(fileName);
            }

            popularService.updatePopular(popularDto);

            return ResponseEntity.status(200).body("Popular item saved successfully");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Operation failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deletePopularItem(@PathVariable Long id) {
        popularService.deletePopular(id);
        return ResponseEntity.ok(new ApiResponse(200, "Popular Item deleted successfully", null));
    }
}
