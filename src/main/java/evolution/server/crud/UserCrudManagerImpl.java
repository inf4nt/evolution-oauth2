package evolution.server.crud;

import evolution.server.common.Role;
import evolution.server.dto.UserDTO;
import evolution.server.dto.UserSaveDTO;
import evolution.server.dto.UserUpdatePasswordDTO;
import evolution.server.dto.UserUpdateUsernameDTO;
import evolution.server.model.User;
import evolution.server.model.UserRoleReference;
import evolution.server.repository.UserRepository;
import evolution.server.repository.UserRoleReferenceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCrudManagerImpl implements UserCrudManager {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRoleReferenceRepository userRoleReferenceRepository;

    @Autowired
    public UserCrudManagerImpl(UserRepository userRepository,
                               ModelMapper modelMapper,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               UserRoleReferenceRepository userRoleReferenceRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleReferenceRepository = userRoleReferenceRepository;
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
        User saved = userRepository.save(user);

        UserRoleReference r = new UserRoleReference();
        r.setUser(saved);
        r.setRole(Role.DEFAULT);

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
