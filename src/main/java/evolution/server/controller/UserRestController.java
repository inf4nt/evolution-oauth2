package evolution.server.controller;

import evolution.server.dto.UserDTO;
import evolution.server.dto.UserSaveDTO;
import evolution.server.dto.UserUpdatePasswordDTO;
import evolution.server.dto.UserUpdateUsernameDTO;
import evolution.server.rest.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserRestController {

    private final UserRestService userRestService;

    @Autowired
    public UserRestController(UserRestService userRestService) {
        this.userRestService = userRestService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        return userRestService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findOne(@PathVariable String id) {
        return userRestService.findOne(id);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAll() {
        return userRestService.deleteAll();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable String id) {
        return userRestService.delete(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> post(@RequestBody UserSaveDTO userSaveDTO) {
        return userRestService.createNewUser(userSaveDTO);
    }

    @PutMapping(value = "/deactivate")
    public ResponseEntity<List<UserDTO>> deactivateForList(@RequestParam List<String> listUsername) {
        return userRestService.deactivate(listUsername);
    }

    @PutMapping(value = "/activate")
    public ResponseEntity<List<UserDTO>> activateForList(@RequestParam List<String> listUsername) {
        return userRestService.active(listUsername);
    }

    @PutMapping(value = "/deactivate/{id}")
    public ResponseEntity<UserDTO> deactivate(@PathVariable String id) {
        return userRestService.deactivate(id);
    }

    @PutMapping(value = "/activate/{id}")
    public ResponseEntity<UserDTO> activate(@PathVariable String id) {
        return userRestService.active(id);
    }

    @PutMapping(value = "/set_password")
    public ResponseEntity<UserDTO> updatePassword(@RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
        return userRestService.updatePassword(userUpdatePasswordDTO);
    }

    @PutMapping(value = "/set_username")
    public ResponseEntity<UserDTO> updateUsername(@RequestBody UserUpdateUsernameDTO userUpdateUsernameDTO) {
        return userRestService.updateUsername(userUpdateUsernameDTO);
    }
}
