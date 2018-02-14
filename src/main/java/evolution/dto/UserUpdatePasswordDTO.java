package evolution.dto;

import lombok.Data;

@Data
public class UserUpdatePasswordDTO {

    private String username;

    private String password;
}
