package civilCapstone.contractB2B.construction.entity;

import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "construction")
// 공사 엔티티
public class Construction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공사 id
    @Column(nullable = false)
    private String name; // 공사명
    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User client; // 공사의 원청

    @JoinColumn(name = "contractor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User contractor; // 공사의 하청

    @Column(nullable = false)
    private String field; // 공종

    @JoinColumn(name = "site_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address site; // 공사 현장

    @Column(nullable = false)
    private String period; // 공사 기간

    @Column(nullable = false)
    private String budget; // 공사 예산

    @Column(name = "content", nullable = false)
    private String contractContent; // 공사 계약 내용

    @Column(name = "status", nullable = false)
    private ConstructionStatus constructionStatus; // 공사 상태

    @Column(nullable = false)
    private String reason; // 계약 이유

    @Column(nullable = true)
    private String rating; // 평점

    public void setStatus(ConstructionStatus constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
