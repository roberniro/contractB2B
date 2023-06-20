package civilCapstone.contractB2B.contractor.repository;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
// 하청업체 평점 Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllByContractor(Contractor contractor);
}
