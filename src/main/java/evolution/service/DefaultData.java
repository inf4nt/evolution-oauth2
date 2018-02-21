package evolution.service;

import evolution.model.User;
import evolution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
public class DefaultData {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Environment env;

    @Autowired
    public DefaultData(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       Environment env) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @PostConstruct
    public void init() {
        createDefaultUser();
    }

    private void createDefaultUser() {
        if (!userRepository.exists(env.getProperty("evolution.default.defaultUserUsername"))) {
            User user = new User();
            user.setUsername(env.getProperty("evolution.default.defaultUserUsername"));
            user.setPassword(bCryptPasswordEncoder.encode(env.getProperty("evolution.default.defaultUserPassword")));
            user.setActive(true);

            userRepository.save(user);
        }

    }

}
