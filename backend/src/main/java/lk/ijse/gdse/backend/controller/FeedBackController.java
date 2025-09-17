package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.ApiResponse;
import lk.ijse.gdse.backend.dto.FeedBackDto;
import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> submitFeedback(
            @RequestBody FeedBackDto feedbackDto,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(401, "Please login first", null));
        }

        String username = authentication.getName();
        FeedBackDto savedFeedback = feedBackService.submitFeedback(feedbackDto, username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Feedback Submitted Successfully", savedFeedback));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse> getAllFeedbacks() {
        List<FeedBackDto> feedbackList = feedBackService.getAllFeedbacks();
        return ResponseEntity.ok(
                new ApiResponse(200, "OK", feedbackList)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable Long id) {
        feedBackService.deleteFeedback(id);
        return ResponseEntity.ok(new ApiResponse(200, "Feedback deleted successfully", null));
    }
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse> getPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        List<FeedBackDto> feedBacks = feedBackService.getFeedbackByPage(page, size);
        return ResponseEntity.ok(new ApiResponse(200, "OK", feedBacks));
    }

    @GetMapping("/total-pages")
    public ResponseEntity<Integer> getTotalPages(@RequestParam int size) {
        int totalPages = feedBackService.getTotalPages(size);
        return ResponseEntity.ok(totalPages);
    }
}
