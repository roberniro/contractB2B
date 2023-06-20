package civilCapstone.contractB2B.contractor.repository;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
// 하청업체 Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    boolean existsByContractor(User user);

    Contractor findByContractor(User user);

}
