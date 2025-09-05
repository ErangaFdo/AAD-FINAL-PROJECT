package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUser();
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long id);
    List<UserDto> getUserByPage(int page, int size);
    int getTotalPages(int size);
    List<UserDto> searchUsers(String keyword);
}
