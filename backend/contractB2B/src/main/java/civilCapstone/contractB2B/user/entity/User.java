package civilCapstone.contractB2B.user.entity;

import civilCapstone.contractB2B.global.entity.Address;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
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
    private String name;

    @Column(length = 10, nullable = true)
    private String nip;

    @JoinColumn(name = "address_id", nullable = true)
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Column(length = 11, nullable = true)
    private String contact;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setAddress(Address address) {
        this.address = address;
    }
}
