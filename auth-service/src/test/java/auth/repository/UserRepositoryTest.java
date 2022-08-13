package auth.repository;

import auth.model.Role;
import auth.model.User;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepo;

    @Test
    public void shouldReturnSavedUsers() {
        var user = User.builder()
                .username("user")
                .password("qwerty12345")
                .roles(Set.of(Role.USER))
                .build();
        userRepo.insert(user);
       var users = userRepo.findAll();
       var usernames = users.stream().map(User::getUsername).collect(Collectors.toList());
       assertEquals(List.of("user"), usernames);
    }
}