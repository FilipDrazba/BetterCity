package city.better.authservice.services;

import city.better.authservice.dtos.JwtValueDto;
import city.better.authservice.dtos.LoginRequestDto;
import city.better.authservice.dtos.RegisterRequestDto;
import city.better.authservice.dtos.UserInfoDto;
import city.better.authservice.entities.User;
import city.better.authservice.enums.Role;
import city.better.authservice.mappers.UserMapper;
import city.better.authservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public JwtValueDto register(RegisterRequestDto request) {
        String encodedPassword = encoder.encode(request.password());

        RegisterRequestDto userWithEncodedPasswordDto =
                RegisterRequestDto.builder()
                        .email(request.email())
                        .password(encodedPassword)
                        .role(request.role())
                        .build();

        User savedUser = userService.createUser(userWithEncodedPasswordDto);

        return jwtService.generateToken(savedUser);
    }

    public JwtValueDto authenticate(LoginRequestDto request) {
        User user = userService.getUserByEmail(request.email());

        if (!encoder.matches(request.password(), user.getPassword()))
            throw new BadCredentialsException("Incorrect login details");

        return jwtService.generateToken(user);
    }

    public UserInfoDto validateAndExtract(String token) {
        if (!jwtService.isTokenValid(token))
            throw new BadCredentialsException("Invalid request");

        String email = jwtService.extractEmail(token);
        User user = userService.getUserByEmail(email);

        return userMapper.toUserInfoDto(user);
    }

    public void verifyEmployee(Long id) {
        var user = userRepository
                .findById(id)
                .orElseThrow();

        user.setRole(Role.EMPLOYEE);

        userRepository.save(user);
    }
}
