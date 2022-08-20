package com.kuymakov.socialmedia.auth.service;

import com.kuymakov.socialmedia.auth.dto.LoginRequest;
import com.kuymakov.socialmedia.auth.dto.RegisterRequest;
import com.kuymakov.socialmedia.auth.exception.InvalidUsernameOrPasswordException;
import com.kuymakov.socialmedia.auth.exception.UserAlreadyExistsException;
import com.kuymakov.socialmedia.auth.model.Role;
import com.kuymakov.socialmedia.auth.model.User;
import com.kuymakov.socialmedia.auth.repository.UserRepository;
import com.kuymakov.socialmedia.auth.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public String login(LoginRequest loginRequest) {
        try {
            var usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password);
            var auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            var roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            var claims = Map.of("roles", roles);
            logger.info("{} logged in successfully!", loginRequest.username);
            return jwtUtils.generateJwtToken(loginRequest.username, new HashMap<>(claims));
        } catch (AuthenticationException e) {
            logger.error("Invalid username/password supplied!");
            throw new InvalidUsernameOrPasswordException("Invalid username/password supplied");
        }
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            logger.error("{} already exist!", registerRequest.username);
            throw new UserAlreadyExistsException("User already exist");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(encoder.encode(registerRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
        var claims = Map.of("roles", (Object) user.getRoles());
        logger.info("{} registered successfully!", registerRequest.username);
        return jwtUtils.generateJwtToken(registerRequest.username, new HashMap<>(claims));
    }
}
