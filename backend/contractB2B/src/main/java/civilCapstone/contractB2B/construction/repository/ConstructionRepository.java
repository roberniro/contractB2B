package civilCapstone.contractB2B.construction.repository;

import civilCapstone.contractB2B.construction.entity.Construction;
import civilCapstone.contractB2B.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// 공사 Repository
public interface ConstructionRepository extends JpaRepository<Construction, Long> {

    List<Construction> findByClient(User user);

    Optional<Construction> findById(Long constructionId);

    boolean existsById(Long constructionId);
}
