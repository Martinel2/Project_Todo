package com.todo.todo.Repository;

import com.todo.todo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailAndProvider(String email, String provider);
}
