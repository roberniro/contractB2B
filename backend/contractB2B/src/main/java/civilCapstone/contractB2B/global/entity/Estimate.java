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
@Table
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 10, nullable = true)
    private String motherId;

    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne
    private User client;

    @JoinColumn(name = "contractor_id", nullable = false)
    @ManyToOne
    private User contractor;

    @Column(length = 20, nullable = false)
    private String field;

    @JoinColumn(name = "site_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Address site;

    @Column(length = 20, nullable = false)
    private String period;

    @Column(length = 20, nullable = false)
    private String budget;

    @Column(name = "client_content", length = 200, nullable = false)
    private String clientContent;

    @Column(name = "contractor_content", length = 200, nullable = true)
    private String contractorContent;

    @Column(name="status", nullable = false)
    @ColumnDefault("PENDING")
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
