package civilCapstone.contractB2B.estimate.entity;

import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "estimate")
// 견적서 엔티티
public class Estimate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name; // 공사명
    @Column(nullable = true)
    private String motherId; // 원견적 id

    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User client; // 원청업체

    @JoinColumn(name = "contractor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User contractor; // 하청업체

    @Column(nullable = false)
    private String field; // 공종

    @JoinColumn(name = "site_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address site; // 현장

    @Column(nullable = false)
    private String period; // 기간

    @Column(nullable = false)
    private String budget; // 예산

    @Column(name = "client_content", length = 200, nullable = false)
    private String clientContent; // 원청업체 내용

    @Column(name = "contractor_content", length = 200, nullable = true)
    private String contractorContent; // 하청업체 내용

    @Column(name="status", nullable = false)
    private EstimateStatus estimateStatus; // 견적 상태

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
