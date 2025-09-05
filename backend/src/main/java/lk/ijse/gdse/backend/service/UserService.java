package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getAllUser();
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteCustomer(Long id);
}
