package civilCapstone.contractB2B.global.entity;

import civilCapstone.contractB2B.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "estimate")
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private String motherId;

    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User client;

    @JoinColumn(name = "contractor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User contractor;

    @Column(nullable = false)
    private String field;

    @JoinColumn(name = "site_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address site;

    @Column(nullable = false)
    private String period;

    @Column(nullable = false)
    private String budget;

    @Column(name = "client_content", length = 200, nullable = false)
    private String clientContent;

    @Column(name = "contractor_content", length = 200, nullable = true)
    private String contractorContent;

    @Column(name="status", nullable = false)
    private EstimateStatus estimateStatus;

    public void setSite(Address address) {
        this.site = address;
    }

    public void setEstimateStatus(EstimateStatus estimateStatus) {
        this.estimateStatus = estimateStatus;
    }

    public void setContractorContent(String contractorContent) {
        this.contractorContent = contractorContent;
    }
}
