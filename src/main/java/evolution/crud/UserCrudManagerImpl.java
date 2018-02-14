package evolution.crud;

import evolution.dto.UserDTO;
import evolution.dto.UserSaveDTO;
import evolution.dto.UserUpdatePasswordDTO;
import evolution.dto.UserUpdateUsernameDTO;
import evolution.model.User;
import evolution.model.UserRole;
import evolution.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCrudManagerImpl implements UserCrudManager {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${evolution.default.role}")
    private String defaultRole;

    @Autowired
    public UserCrudManagerImpl(UserRepository userRepository,
                               ModelMapper modelMapper,
                               BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean isExist(String username) {
        return userRepository.exists(username);
    }

    @Override
    public UserDTO findOne(String username) {
        return modelMapper.map(userRepository.findOne(username), UserDTO.class);
    }

    @Override
    public List<UserDTO> findAllUser(boolean active) {
        return userRepository
                .findAllByActive(active)
                .stream()
                .map(o -> modelMapper.map(o, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findByUsername(String username, boolean active) {
        return userRepository
                .findByUsername(username, active)
                .map(o -> modelMapper.map(o, UserDTO.class));
    }

    @Override
    public UserDTO createNewUser(UserSaveDTO userSaveDTO) {
        User user = new User();
        user.setUsername(userSaveDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userSaveDTO.getPassword()));

        user.setUserRoleList(user.getUserRoleList()
                .stream()
                .peek(o -> {
                    o.setRole(defaultRole);
                    o.setUser(user);
                })
                .collect(Collectors.toList())
        );
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public UserDTO updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        User original = userRepository.findOne(userUpdatePasswordDTO.getUsername());
        original.setPassword(bCryptPasswordEncoder.encode(userUpdatePasswordDTO.getPassword()));
        User saved = userRepository.save(original);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public UserDTO updateUsername(UserUpdateUsernameDTO userUpdateUsernameDTO) {
        User original = userRepository.findOne(userUpdateUsernameDTO.getUsername());
        original.setUsername(userUpdateUsernameDTO.getNewUsername());
        User saved = userRepository.save(original);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public UserDTO deactivateUser(String username) {
        User original = userRepository.findOne(username);
        original.setActive(false);
        User saved = userRepository.save(original);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public UserDTO activateUser(String username) {
        User original = userRepository.findOne(username);
        original.setActive(true);
        User saved = userRepository.save(original);
        return modelMapper.map(saved, UserDTO.class);
    }

    @Override
    public List<UserDTO> deactivateUserList(List<String> username) {
        return username
                .stream()
                .map(o -> deactivateUser(o))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> activateUserList(List<String> username) {
        return username
                .stream()
                .map(o -> activateUser(o))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String username) {
        userRepository.delete(username);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
