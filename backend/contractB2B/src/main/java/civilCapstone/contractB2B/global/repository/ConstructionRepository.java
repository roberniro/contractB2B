package civilCapstone.contractB2B.global.repository;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.global.entity.Construction;
import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConstructionRepository extends JpaRepository<Construction, Long> {

    List<Construction> findByClient(User user);

    boolean existsByClient(User user);

    Optional<Construction> findById(Long constructionId);

    boolean existsById(Long constructionId);
}
