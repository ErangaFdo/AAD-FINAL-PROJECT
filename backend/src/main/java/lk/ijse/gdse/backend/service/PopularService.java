package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.PopularDto;
import lk.ijse.gdse.backend.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PopularService {
    void savePopular(PopularDto popularDto);
    List<PopularDto> getAllPopularIte();
    void updatePopular(PopularDto popularDto);
    void deletePopular(Long id);

}
