package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.UserDto;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUser() {
        List<User> list = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : list) {
            userDtos.add(modelMapper.map(user, UserDto.class));
        }
        return userDtos;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        User user = modelMapper.map(userDto, User.class);

        User saved = userRepository.save(user);
        UserDto response = modelMapper.map(saved, UserDto.class);
        return response;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        User user = modelMapper.map(userDto, User.class);

        User updated = userRepository.save(user);
        UserDto response = modelMapper.map(updated, UserDto.class);
        return response;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getUserByPage(int page, int size) {
        int offset = page * size;
        List<User> users = userRepository.findByUserPaginated(size, offset);
        return modelMapper.map(users, new TypeToken<List<UserDto>>() {}.getType());
    }

    @Override
    public int getTotalPages(int size) {
        int totalUsers = userRepository.getTotalUserCount();
        return (int) Math.ceil((double) totalUsers / size);
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        List<User>list=userRepository.findUserByUserNameContainingIgnoreCase(keyword);
        return modelMapper.map(list, new TypeToken<List<UserDto>>(){}.getType());
    }


}
