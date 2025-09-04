package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.AuthDto;
import lk.ijse.gdse.backend.dto.AuthResponseDto;
import lk.ijse.gdse.backend.dto.RegisterDto;
import lk.ijse.gdse.backend.entity.Role;
import lk.ijse.gdse.backend.entity.User;
import lk.ijse.gdse.backend.repository.UserRepository;
import lk.ijse.gdse.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDto authenticate(AuthDto authDto) {
        User user = userRepository.findByUserName(authDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(authDto.getPassword(), user.getUserPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        String token = jwtUtil.generateToken(authDto.getUsername());
        System.out.println("Login successful for user: " + authDto.getUsername());
        System.out.println(" Role: " + user.getRole());
        return new AuthResponseDto(token);
    }

    public String register(RegisterDto registerDTO) {
        if (userRepository.findByUserName(registerDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already exist");
        }

        User user = User.builder()
                .userName(registerDTO.getUserName())
                .emailAddress(registerDTO.getEmailAddress())
                .userPassword(passwordEncoder.encode(registerDTO.getUserPassword()))
                .role(Role.valueOf(registerDTO.getRole().toUpperCase()))
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }
}
