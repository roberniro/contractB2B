package civilCapstone.contractB2B.contractor.entity;

import civilCapstone.contractB2B.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
// 하청업체 평점 엔티티
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "contractor_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Contractor contractor;

    @JoinColumn(name = "experience_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Experience experience;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer rating;
}
