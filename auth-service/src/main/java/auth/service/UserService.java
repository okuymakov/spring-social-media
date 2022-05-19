package auth.service;

import auth.dto.LoginRequest;
import auth.dto.RegisterRequest;
import auth.exception.InvalidUsernameOrPasswordException;
import auth.exception.UserAlreadyExistsException;
import auth.model.Role;
import auth.utils.JwtUtils;
import auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private final UserDetailsServiceImpl userDetailsService;


    public String login(LoginRequest loginRequest) {
        try {
            var usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password);
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return jwtUtils.generateJwtToken(loginRequest.username);
        } catch (AuthenticationException e) {
            throw new InvalidUsernameOrPasswordException("Invalid username/password supplied");
        }
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User already exist");
        }
        auth.model.User user = auth.model.User.builder()
                .username(registerRequest.getUsername())
                .password(encoder.encode(registerRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
        String jwt = jwtUtils.generateJwtToken(registerRequest.username);
        return jwt;
    }

    public void authenticate(String accessToken) {
        accessToken = accessToken.substring("Bearer ".length());
        if (accessToken != null && jwtUtils.validateJwtToken(accessToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user = (User) userDetailsService.loadUserByUsername(username);
            var usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
    }
}
