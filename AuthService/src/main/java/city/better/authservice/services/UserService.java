package city.better.authservice.services;

import city.better.authservice.dtos.RegisterRequestDto;
import city.better.authservice.entities.User;
import city.better.authservice.mappers.UserMapper;
import city.better.authservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    public User createUser(RegisterRequestDto userDto) {
        User user = userMapper.toUser(userDto);

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
