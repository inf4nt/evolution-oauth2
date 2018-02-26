package evolution.server.dto;

import lombok.Data;

@Data
public class UserUpdateUsernameDTO {

    private String username;

    private String newUsername;
}
