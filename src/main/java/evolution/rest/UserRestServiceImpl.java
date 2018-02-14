package evolution.rest;

import evolution.crud.UserCrudManager;
import evolution.dto.UserDTO;
import evolution.dto.UserSaveDTO;
import evolution.dto.UserUpdatePasswordDTO;
import evolution.dto.UserUpdateUsernameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRestServiceImpl implements UserRestService {

    private final UserCrudManager userCrudManager;

    @Autowired
    public UserRestServiceImpl(UserCrudManager userCrudManager) {
        this.userCrudManager = userCrudManager;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> list = userCrudManager.findAllUser(true);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Override
    @PreAuthorize("#username == authentication.name || hasRole('ADMIN')")
    public ResponseEntity<UserDTO> findOne(String username) {
        Optional<UserDTO> op = userCrudManager.findByUsername(username, true);
        if (op.isPresent()) {
            return ResponseEntity.ok(op.get());
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(String username) {
        userCrudManager.delete(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAll() {
        userCrudManager.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDTO> createNewUser(UserSaveDTO userSaveDTO) {
        boolean exist = userCrudManager.isExist(userSaveDTO.getUsername());
        if (exist) {
            return ResponseEntity.status(417).build();
        }
        return ResponseEntity.ok(userCrudManager.createNewUser(userSaveDTO));
    }

    @Override
    @PreAuthorize("#userUpdatePasswordDTO.getUsername() == authentication.name || hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        return ResponseEntity.ok(userCrudManager.updatePassword(userUpdatePasswordDTO));
    }

    @Override
    @PreAuthorize("#userUpdateUsernameDTO.getUsername() == authentication.name || hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUsername(UserUpdateUsernameDTO userUpdateUsernameDTO) {
        return ResponseEntity.ok(userCrudManager.updateUsername(userUpdateUsernameDTO));
    }

    @Override
    @PreAuthorize("#username == authentication.name || hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deactivate(String username) {
        return ResponseEntity.ok(userCrudManager.deactivateUser(username));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> active(String username) {
        return ResponseEntity.ok(userCrudManager.activateUser(username));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> deactivate(List<String> list) {
        return ResponseEntity.ok(userCrudManager.deactivateUserList(list));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> active(List<String> list) {
        return ResponseEntity.ok(userCrudManager.activateUserList(list));
    }
}
