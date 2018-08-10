package evolution.server.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ev_user")
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String username;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRoleReference> roles = new ArrayList<>();

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @Version
    @Column(columnDefinition = "bigint default 0")
    private Long version;
}