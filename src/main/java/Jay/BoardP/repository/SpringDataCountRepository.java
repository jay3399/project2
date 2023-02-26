package Jay.BoardP.repository;

import Jay.BoardP.domain.CountPerDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCountRepository extends JpaRepository<CountPerDay, Long> {

}
