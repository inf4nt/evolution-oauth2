package evolution.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ev_user")
@Data
public class User {

    @Id
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String username;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @Version
    @Column(columnDefinition = "bigint default 0")
    private Long version;;
}
