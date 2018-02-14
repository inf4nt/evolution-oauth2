package evolution.repository;

import evolution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from User u where u.username =:username and u.active =:active")
    Optional<User> findByUsername(@Param("username") String username, @Param("active") boolean active);

    @Query("select u from User u where u.active =:active")
    List<User> findAllByActive(@Param("active") boolean active);
}
