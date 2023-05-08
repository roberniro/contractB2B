package civilCapstone.contractB2B.user.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;
    @Column(name = "name", length = 20, nullable = false)
    private String companyName;

    @Column(length = 10, nullable = false)
    private String nip;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 11, nullable = false)
    private String contact;
    @Enumerated(EnumType.STRING)
    private Role role;

}
