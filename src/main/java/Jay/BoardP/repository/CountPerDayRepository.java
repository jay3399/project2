package Jay.BoardP.repository;

import Jay.BoardP.domain.CountPerDay;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountPerDayRepository extends JpaRepository<CountPerDay, Long> {

    List<CountPerDay> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
