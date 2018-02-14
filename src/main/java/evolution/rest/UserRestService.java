package evolution.rest;

import evolution.dto.UserDTO;
import evolution.dto.UserSaveDTO;
import evolution.dto.UserUpdatePasswordDTO;
import evolution.dto.UserUpdateUsernameDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserRestService {

    ResponseEntity<List<UserDTO>> findAll();

    ResponseEntity<UserDTO> findOne(String username);

    ResponseEntity<HttpStatus> delete(String username);

    ResponseEntity<HttpStatus> deleteAll();

    ResponseEntity<UserDTO> createNewUser(UserSaveDTO userSaveDTO);

    ResponseEntity<UserDTO> updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO);

    ResponseEntity<UserDTO> updateUsername(UserUpdateUsernameDTO userUpdateUsernameDTO);

    ResponseEntity<UserDTO> deactivate(String username);

    ResponseEntity<UserDTO> active(String username);

    ResponseEntity<List<UserDTO>> deactivate(List<String> list);

    ResponseEntity<List<UserDTO>> active(List<String> list);
}
