package com.user.serviceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.payload.UserDto;
import com.user.repo.UserRepository;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public String authenticateUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user);
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
