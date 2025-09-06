package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.FeedBackDto;
import lk.ijse.gdse.backend.entity.FeedBack;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.FeedBackRepository;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public FeedBackDto submitFeedback(FeedBackDto feedbackDto, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        FeedBack feedBack = new FeedBack();
        feedBack.setFullName(feedbackDto.getFullName());
        feedBack.setEmail(feedbackDto.getEmail());
        feedBack.setServices(feedbackDto.getServices());
        feedBack.setRatings(feedbackDto.getRatings());
        feedBack.setMessage(feedbackDto.getMessage());
        feedBack.setUser(user);

        feedBackRepository.save(feedBack);

        return feedbackDto;
    }

    @Override
    public List<FeedBackDto> getAllFeedbacks() {
        List<FeedBack> feedbacks = feedBackRepository.findAll();

        List<FeedBackDto>feedBackDtos = new ArrayList<>();
        for (FeedBack feedBack : feedbacks) {
            feedBackDtos.add(modelMapper.map(feedBack, FeedBackDto.class));

        }
        return feedBackDtos;
    }
}
