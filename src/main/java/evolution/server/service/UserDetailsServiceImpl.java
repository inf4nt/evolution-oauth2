package evolution.server.service;

import evolution.server.security.UserSecurity;
import evolution.server.model.User;
import evolution.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFuture = userRepository.findByUsername(username);

        if (!userFuture.isPresent()) {
            throw new UsernameNotFoundException("user by username " + username + " not found!");
        }
        User u = userFuture.get();

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        u.getRoles()
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
