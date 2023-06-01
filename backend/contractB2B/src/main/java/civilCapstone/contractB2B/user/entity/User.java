package civilCapstone.contractB2B.user.entity;

import civilCapstone.contractB2B.global.entity.Address;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = true)
    private String nip;

    @JoinColumn(name = "address_id", nullable = true)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @Column(nullable = true)
    private String contact;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setAddress(Address address) {
        this.address = address;
    }
}
