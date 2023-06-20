package civilCapstone.contractB2B.contractor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
// 하청업체의 경력사항 엔티티
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "contractor_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contractor contractor;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String site;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private String budget;

    @Column(nullable = false)
    private String content;

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }
}
