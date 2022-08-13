package auth.service;

import auth.dto.LoginRequest;
import auth.dto.RegisterRequest;
import auth.model.Role;
import auth.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureDataMongo
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    JwtUtils jwtUtils;

    @Test
    void shouldReturnRolesOfLoggedInUser() {
        var jwt = userService.login(new LoginRequest("username","qwerty12345"));
        var claim = jwtUtils.getClaims(jwt);
        var expected = List.of(Role.USER.name());
        assertEquals(expected, claim.get("roles"));
    }

    @Test
    void shouldReturnRolesOfRegisteredUser() {
        var jwt = userService.register(new RegisterRequest("username5","qwerty12345"));
        userService.login(new LoginRequest("username5","qwerty12345"));
        var claim = jwtUtils.getClaims(jwt);
        var expected = List.of(Role.USER.name());
        assertEquals(expected, claim.get("roles"));
    }
}