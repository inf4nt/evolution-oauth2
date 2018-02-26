package evolution.server.repository;

import evolution.server.model.UserRoleReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRoleReferenceRepository extends JpaRepository<UserRoleReference, Long> {

    List<UserRoleReference> findByUserUsername(String username);

    @Async
    @Query("select r from UserRoleReference r where r.user.username =:username")
    CompletableFuture<List<UserRoleReference>> findByUserUsernameAsync(@Param("username") String username);
}
