package evolution.server.service;

import evolution.client.command.UserCreateCommand;
import evolution.client.feign.UserFeignClient;
import evolution.server.common.Role;
import evolution.server.model.User;
import evolution.server.model.UserRoleReference;
import evolution.server.repository.UserRepository;
import evolution.server.repository.UserRoleReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DefaultData {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Environment env;

    private final UserRoleReferenceRepository userRoleReferenceRepository;

    private final UserFeignClient userFeignClient;

    @Autowired
    public DefaultData(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       Environment env,
                       UserRoleReferenceRepository userRoleReferenceRepository,
                       UserFeignClient userFeignClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
        this.userRoleReferenceRepository = userRoleReferenceRepository;
        this.userFeignClient = userFeignClient;
    }

    @PostConstruct
    public void init() {
        createDefaultUser();
    }

    private void createDefaultUser() {
        if (!userRepository.findByUsername(env.getProperty("evolution.default.defaultUserUsername")).isPresent()) {
            User user = new User();
            user.setUsername(env.getProperty("evolution.default.defaultUserUsername"));
            user.setPassword(bCryptPasswordEncoder.encode(env.getProperty("evolution.default.defaultUserPassword")));
            user.setActive(true);
            userRepository.save(user);

            UserRoleReference userRoleReference = new UserRoleReference();
            userRoleReference.setUser(user);
            userRoleReference.setRole(Role.ADMIN);
            userRoleReferenceRepository.save(userRoleReference);
        }

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("username_people" + i);
            user.setPassword(bCryptPasswordEncoder.encode("password"));
            user.setActive(true);
            userRepository.save(user);

            UserRoleReference userRoleReference = new UserRoleReference();
            userRoleReference.setUser(user);

            if (i % 2 == 0) {
                userRoleReference.setRole(Role.ADMIN);
            } else {
                userRoleReference.setRole(Role.USER);
            }
            userRoleReferenceRepository.save(userRoleReference);
        }

        List<User> all = userRepository.findAll();
        all.forEach(o -> CompletableFuture.runAsync(() -> userFeignClient.postUser(new UserCreateCommand(
                o.getId().toString(),
                o.getUsername()
        ))));
    }

}
