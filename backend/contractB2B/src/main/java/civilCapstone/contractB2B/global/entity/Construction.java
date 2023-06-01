package civilCapstone.contractB2B.global.entity;

import civilCapstone.contractB2B.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "construction")
public class Construction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
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

    @Column(name = "content", nullable = false)
    private String contractContent;

    @Column(name = "status", nullable = false)
    private ConstructionStatus constructionStatus;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = true)
    private String rating;

    public void setStatus(ConstructionStatus constructionStatus) {
        this.constructionStatus = constructionStatus;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
