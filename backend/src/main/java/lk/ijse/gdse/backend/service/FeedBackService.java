package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.FeedBackDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedBackService {
    FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username);
    List<FeedBackDto> getAllFeedbacks();
    void deleteFeedback(Long id);
}
