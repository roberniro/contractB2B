package civilCapstone.contractB2B.estimate.repository;

import civilCapstone.contractB2B.estimate.entity.Estimate;
import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// 견적 Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    boolean existsByMotherId(String motherId);
    List<Estimate> findAllByContractor(User contractor);
    boolean existsByClient(User user);
    boolean existsByContractor(User user);

    List<Estimate> findAllByClient(User user);
}
