package evolution.server.model;

import evolution.server.common.Role;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ev_user_role_reference")
@Data
public class UserRoleReference {

    @Id
    @SequenceGenerator(name = "user_role_reference_seq", sequenceName = "user_role_reference_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_reference_seq")
    @Column(columnDefinition = "bigint")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "varchar(255)", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private Role role;
}
