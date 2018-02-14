package evolution.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_role")
    @SequenceGenerator(name = "seq_user_role", sequenceName = "seq_user_role_id", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user = new User();

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String role;

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }
}
