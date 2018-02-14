package evolution.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private String username;

    private List<UserRoleDTO> userRoleList = new ArrayList<>();
}
