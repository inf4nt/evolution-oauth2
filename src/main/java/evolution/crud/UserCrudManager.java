package evolution.crud;

import evolution.dto.UserDTO;
import evolution.dto.UserSaveDTO;
import evolution.dto.UserUpdatePasswordDTO;
import evolution.dto.UserUpdateUsernameDTO;

import java.util.List;
import java.util.Optional;

public interface UserCrudManager {

    boolean isExist(String username);

    UserDTO findOne(String username);

    List<UserDTO> findAllUser(boolean active);

    Optional<UserDTO> findByUsername(String username, boolean active);

    UserDTO createNewUser(UserSaveDTO userSaveDTO);

    UserDTO updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO);

    UserDTO updateUsername(UserUpdateUsernameDTO userUpdateUsernameDTO);

    UserDTO deactivateUser(String username);

    UserDTO activateUser(String username);

    List<UserDTO> deactivateUserList(List<String> username);

    List<UserDTO> activateUserList(List<String> username);

    void delete(String username);

    void deleteAll();
}
