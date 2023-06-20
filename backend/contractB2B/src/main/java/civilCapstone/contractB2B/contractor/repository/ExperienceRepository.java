package civilCapstone.contractB2B.contractor.repository;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// 하청업체 경력 Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    boolean existsByContractor(Contractor contractor);

    List<Experience> findAllByContractor(Contractor contractor);
}
