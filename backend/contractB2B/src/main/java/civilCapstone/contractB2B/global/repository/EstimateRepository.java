package civilCapstone.contractB2B.global.repository;

import civilCapstone.contractB2B.global.entity.Estimate;
import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    Estimate findByContractorId(Long contractorId);
    Estimate findByClientId(Long clientId);
    Estimate findByContractorIdAndClientId(Long contractorId, Long clientId);
    List<Estimate> findAllByClient_Id(Long clientId);
    List<Estimate> findAllByMotherId(String motherId);
    boolean existsByClient_Id(Long id);
    boolean existsByMotherId(String motherId);

    List<Estimate> findAllByContractor_Id(Long contractorId);

    List<Estimate> findAllByContractor(User contractor);
}
