package auth.controller;

import auth.dto.LoginRequest;
import auth.dto.RegisterRequest;
import auth.exception.InvalidUsernameOrPasswordException;
import auth.exception.NotValidJwtTokenException;
import auth.exception.UserAlreadyExistsException;
import auth.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        try {
            userService.authenticate(accessToken);
            return ResponseEntity.ok("Authentication was successful!");
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Authentication failed!");
        } catch (NotValidJwtTokenException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Access token is not valid!");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Something was wrong!");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = userService.login(loginRequest);
            var headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            return ResponseEntity
                    .ok().headers(headers)
                    .body("User logged in successfully!");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: User not found!");
        } catch (InvalidUsernameOrPasswordException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Invalid username/password supplied!");
        } catch (Exception e) {
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
            return ResponseEntity
                    .badRequest()
                    .body("Error: Something was wrong!");
        }
    }
}
