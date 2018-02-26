package evolution.server.service;

import evolution.server.model.UserRoleReference;
import evolution.server.repository.UserRoleReferenceRepository;
import evolution.server.security.UserSecurity;
import evolution.server.model.User;
import evolution.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserRoleReferenceRepository userRoleReferenceRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                  UserRoleReferenceRepository userRoleReferenceRepository) {
        this.userRepository = userRepository;
        this.userRoleReferenceRepository = userRoleReferenceRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompletableFuture<List<UserRoleReference>> rolesFuture = userRoleReferenceRepository.findByUserUsernameAsync(username);
        CompletableFuture<User> userFuture = userRepository.findByUsernameAsync(username);

        CompletableFuture.allOf(rolesFuture, userFuture);

        if (userFuture == null) {
            throw new UsernameNotFoundException("user by username " + username + " not found!");
        }
        User u = userFuture.join();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        rolesFuture.join()
                .forEach(o -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + o.getRole())));

        return new UserSecurity(
                u.getUsername(),
                u.getPassword(),
                true,
                true,
                true,
                true,
                grantedAuthorities,
                u
        );
    }
}
