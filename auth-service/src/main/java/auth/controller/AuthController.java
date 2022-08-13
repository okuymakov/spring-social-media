package auth.controller;

import auth.dto.LoginRequest;
import auth.dto.RegisterRequest;
import auth.exception.InvalidUsernameOrPasswordException;
import auth.exception.UserAlreadyExistsException;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = userService.login(loginRequest);
            var headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            return ResponseEntity
                    .ok().headers(headers)
                    .body("User logged in successfully!");
        } catch (InvalidUsernameOrPasswordException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Invalid username/password supplied!");
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body("Error: Something was wrong!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest signUpRequest) {
        try {
            String jwt = userService.register(signUpRequest);
            var headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            return ResponseEntity
                    .ok().headers(headers).body("User registered successfully!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: User is already exists!");
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body("Error: Something was wrong!");
        }
    }
}
