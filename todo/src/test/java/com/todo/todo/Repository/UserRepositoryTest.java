package com.todo.todo.Repository;

import com.todo.todo.Entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void test_findUserByEmail() {
        String email = "test@example.com";

        User user = new User();
        user.setUsername("testUser");
        user.setEmail(email);
        user.setProvider("google");
        userRepository.save(user);


        Optional<User> savedUser = userRepository.findByEmail(email);

        assertEquals(savedUser.get().getUsername(), user.getUsername());
        assertEquals(savedUser.get().getEmail(), user.getEmail());
        assertEquals(savedUser.get().getProvider(), user.getProvider());
    }

    @Test
    void test_findUserByUsername() {
        String userName = "testUser";
        String email = "test@example.com";

        User user = new User();
        user.setUsername(userName);
        user.setEmail(email);
        user.setProvider("google");
        userRepository.save(user);


        Optional<User> savedUser = userRepository.findByUsername(userName);

        assertEquals(savedUser.get().getUsername(), user.getUsername());
        assertEquals(savedUser.get().getEmail(), user.getEmail());
        assertEquals(savedUser.get().getProvider(), user.getProvider());
    }

    @Test
    void test_findUserByEmailAndProvider() {
        String email = "test@example.com";
        String provider = "google";

        User user = new User();
        user.setUsername("testUser");
        user.setEmail(email);
        user.setProvider(provider);
        userRepository.save(user);


       Optional<User> savedUser = userRepository.findUserByEmailAndProvider(email,provider);

       assertEquals(savedUser.get().getUsername(), user.getUsername());
       assertEquals(savedUser.get().getEmail(), user.getEmail());
       assertEquals(savedUser.get().getProvider(), user.getProvider());
    }

}
