package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUser() {
        List<UserDto> user = userService.getAllUser ();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", user)
        );
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.saveUser(userDto);
        return ResponseEntity.ok(
                new ApiResponse(201, "Customer Saved Successfully", savedUser)
        );
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUser(userDto);
        return ResponseEntity.ok(
                new ApiResponse(201, "Customer Saved Successfully", updateUser)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse(200, "Customer deleted successfully", null));
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<UserDto> users = userService.getUserByPage(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "OK", users));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = userService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }

    @GetMapping("search/{keyword}")
    public ResponseEntity<ApiResponse> searchMenus(@PathVariable("keyword") String keyword) {
        List<UserDto> userDtos = userService.searchUsers(keyword);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "User found Successfully",
                        userDtos
                )
        );
    }

}
